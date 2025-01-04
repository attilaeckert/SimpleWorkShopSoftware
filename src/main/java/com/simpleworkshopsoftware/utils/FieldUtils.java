package com.simpleworkshopsoftware.utils;

import com.simpleworkshopsoftware.base.Database;
import com.simpleworkshopsoftware.configuration.ConfigManager;
import com.simpleworkshopsoftware.controller.ControllerConnector;
import com.simpleworkshopsoftware.controller.PopupWindowsController;
import com.simpleworkshopsoftware.controller.PopupWindowsController.DialogType;
import com.simpleworkshopsoftware.controller.tableControllers.WorkOrderTableController;
import com.simpleworkshopsoftware.dto.WorkOrderTableDTO;
import com.simpleworkshopsoftware.entities.Car;
import com.simpleworkshopsoftware.entities.Customer;
import com.simpleworkshopsoftware.entities.WorkOrder;
import com.simpleworkshopsoftware.validators.InputValidator;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.configuration2.Configuration;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * The FieldUtils class provides various utility methods for handling fields
 * related to customers, cars, work orders, and calculations within the application.
 * It includes methods for setting fields, clearing fields, validating input,
 * and performing calculations related to labor and parts costs.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class FieldUtils {
    private static final ControllerConnector CONNECTOR = ControllerConnector.getInstance();
    private static final String DEFAULT_STYLE = null;
    private static final String NUMBER_FORMAT_ERROR = "Nem megfelelő szám formátum";
    private static final NumberFormat FORMAT = NumberFormat.getInstance(Locale.GERMANY);


    /**
     * Retrieves the default labor cost from the configuration file
     * to let other methods calculate with it.
     *
     * @return the default labor cost as a String
     */
    private static String getDefaultLaborCost() {
        Configuration config = ConfigManager.getInstance().getConfig();
        return config.getString("workshop.laborcost");
    }

    /**
     * Sets the customer fields in the provided text fields and choice box
     * based on the customer data.
     *
     * @param cb the ChoiceBox for customer type selection
     * @param value1 the value to select for companies
     * @param value2 the value to select for individual customers
     * @param fields1 array of TextField for companies
     * @param fields2 array of TextField for individual customer fields
     */
    public static void setCustomerFields(Customer cust, ChoiceBox<String> cb, String value1, String value2,
                                         TextField[] fields1, TextField[] fields2) {
        if (!cust.getTaxNumber().isEmpty()) {
            cb.getSelectionModel().select(value1);
            fields1[0].setText(cust.getLastName());
            fields1[1].setText(cust.getAddress());
            fields1[2].setText(cust.getEmail());
            fields1[3].setText(cust.getPhonenumber());
            fields1[4].setText(cust.getTaxNumber());
        } else {
            cb.getSelectionModel().select(value2);
            fields2[0].setText(cust.getLastName());
            fields2[1].setText(cust.getFirstName());
            fields2[2].setText(cust.getAddress());
            fields2[3].setText(cust.getEmail());
            fields2[4].setText(cust.getPhonenumber());
        }
    }

    public static void setCarFields(Car car, TextField[] carFields, ChoiceBox<String> cb) {
        carFields[0].setText(car.getBrand());
        carFields[1].setText(car.getModel());
        carFields[2].setText(car.getVinNumber());
        carFields[3].setText(car.getEngine());
        carFields[4].setText(car.getYear());
        carFields[5].setText(car.getLicensePlateNum());
        carFields[6].setText(car.getRoadCertificate().toString());
        carFields[7].setText(String.valueOf(car.getKilometers()));
        cb.setValue(car.getFuelType());
    }

    public static void clearFields(TextField[] fields) {
        for (TextField field : fields) {
            if (!field.getText().trim().isEmpty()) {
                field.clear();
            }
            field.setStyle(DEFAULT_STYLE);
        }
    }

    public static void clearArea(TextArea txa) {
        txa.clear();
    }

    public static void clearChoiceBox(ChoiceBox<String> cb) {
        cb.setValue(null);
        cb.setStyle(null);
    }

    public static void setWorkOrderFields(TextField[] fields, WorkOrderTableController controller) {
        WorkOrder wo = CONNECTOR.getWorkOrderDto().getWo();
        fields[3].setText(wo.getDiscountPercent());             // discountPercentTf
        fields[4].setText(wo.getPartsCost());                   // partsPriceSumTf
        fields[5].setText(wo.getLaborCost());                   // laborCostTf
        fields[6].setText(wo.getOverallCost());                 // overallCostTf
        controller.createDtoFromData(wo.getTableData());
    }

    /**
     * Checks the value of the kilometer text field to ensure it is valid.
     * Displays a warning dialog if the value is invalid.
     *
     * @param kmTextField the TextField containing the km value
     * @param oldKm the previous km value for validation
     * @return the km value if valid, null if invalid
     */
    public static String checkKm(TextField kmTextField, int oldKm) {
        String km = kmTextField.getText();
        if (km.isEmpty()) {
            PopupWindowsController.showDialog("Kérlek add meg a km számláló értékét!", DialogType.WARNING);
            return null;
        } else if (Integer.parseInt(km) < oldKm) {
            PopupWindowsController.showDialog
                    ("A km számláló értéke nem lehet kevesebb mint az előző regisztrációnál!", DialogType.WARNING);
            return null;
        }
        return km;
    }

    /**
     * Updates the km value for a car in the database.
     *
     * @param kmTextField the TextField containing the new km value
     * @param id the ID of the car to update
     * @param oldKm the previous km value for validation
     * @param connector the ControllerConnector instance
     * @return true if the update was successful, false otherwise
     */
    public static boolean setKm(TextField kmTextField, int id, int oldKm, ControllerConnector connector) {
        String km = FieldUtils.checkKm(kmTextField, oldKm);
        if (km == null) return false;
        try {
            Database.getCarRepository().updateKm(id, km);
            connector.customerOrCarAddedOrChangedProperty().set(true);
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Hiba a km állás mentése közben", DialogType.WARNING, e);
        }
        return true;
    }

    /**
     * Calculates the total cost of parts and labor and sets the total in the provided TextField.
     *
     * @param parts the TextField containing the parts cost
     * @param labor the TextField containing the labor cost
     * @param total the TextField to set the total cost
     */
    public static void totalPartsAndLabor(TextField parts, TextField labor, TextField total) {
        double sum = 0;
        double partsCost = 0;
        double laborCost = 0;
        try {
            if (!parts.getText().isBlank()) {
                partsCost = Double.parseDouble(parts.getText());
            }
            if (!labor.getText().isBlank()) {
                laborCost = Double.parseDouble(labor.getText());
            }
            sum = partsCost + laborCost;
        } catch (NumberFormatException e) {
            PopupWindowsController.showDialog(NUMBER_FORMAT_ERROR, DialogType.WARNING, e);
        }
        total.setText(String.format("%.0f", sum));
    }

    /**
     * Sets the unit price to the default labor cost if the job field changing(text entered).
     *
     * @param tableJob the TextField for the job name
     * @param tableUnitPrice the TextField for the unit price
     */
    public static void setUnitPriceToDefaultLaborCost(TextField tableJob, TextField tableUnitPrice) {
        if (!tableJob.getText().isBlank()) {
            tableUnitPrice.setText(getDefaultLaborCost());
        } else {
            tableUnitPrice.setText("");
        }
    }

    /**
     * Calculates the total labor cost from the work order table and sets it in the provided TextField.
     *
     * @param workOrderTable the TableView containing work order data
     * @param laborCost the TextField to set the total labor cost
     */
    public static void totalOfLabor(TableView<WorkOrderTableDTO> workOrderTable,
                                    TextField laborCost) {
        double sum = 0;
        for (WorkOrderTableDTO dto : workOrderTable.getItems()) {
            if (!dto.getJobName().isBlank()) {
                try {
                    sum += Double.parseDouble(dto.getUnitPrice());
                } catch (NumberFormatException e) {
                    PopupWindowsController.showDialog(NUMBER_FORMAT_ERROR, DialogType.WARNING, e);
                    return;
                }
            }
        }
        laborCost.setText(String.format("%.0f", sum));
    }

    /**
     * Reduces the parts price based on the discount percentage and updates the total parts cost.
     * If the discount Tf is blank, it calculates with 0, means no discount.
     *
     * @param workOrderTable the TableView containing work order data
     * @param discountPercent the TextField containing the discount percentage
     * @param partsCost the TextField to set the total parts cost
     */
    public static void calculatePartsPrice(TableView<WorkOrderTableDTO> workOrderTable,
                                           TextField discountPercent, TextField partsCost) {
        InputValidator validator = new InputValidator();
        double sum = 0;
        double percentage = 0;
        double originalPrice = 0;
        validator.addValidation(discountPercent, val -> val.matches("\\d\\d?"));
        if (validator.validate()) {
            if (workOrderTable.getItems().isEmpty()) {
                return;
            }
            try {
                percentage = Double.parseDouble(discountPercent.getText()) / 100.0;
            } catch (NumberFormatException e) {
                PopupWindowsController.showDialog(NUMBER_FORMAT_ERROR, DialogType.WARNING, e);
            }
        }
        for (WorkOrderTableDTO dto : workOrderTable.getItems()) {
            if (!dto.getPartName().isBlank()) {
                try {
                    originalPrice = Double.parseDouble(dto.getUnitPrice());
                } catch (NumberFormatException e) {
                    PopupWindowsController.showDialog(NUMBER_FORMAT_ERROR, DialogType.WARNING, e);
                }
                double newPrice = originalPrice * (1 - percentage);
                sum += newPrice;
            }
        }
        partsCost.setText(String.format("%.0f", sum));
    }

    /**
     * Multiplies the quantity by the unit price and returns the result as a formatted string.
     *
     * @param tableQuantity the TextField containing the quantity
     * @param tableUnitPrice the TextField containing the unit price
     * @return the result of the multiplication as a formatted string
     */
    public static String multiplyWithQuantity(TextField tableQuantity, TextField tableUnitPrice) throws ParseException {
        double sum = FORMAT.parse(
                tableQuantity.getText()).doubleValue() * Double.parseDouble(tableUnitPrice.getText());
        return String.format("%.0f", sum);
    }
}
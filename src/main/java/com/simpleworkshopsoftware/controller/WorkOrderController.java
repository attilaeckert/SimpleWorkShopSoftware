package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.base.Database;
import com.simpleworkshopsoftware.controller.PopupWindowsController.DialogType;
import com.simpleworkshopsoftware.controller.tableControllers.WorkOrderTableController;
import com.simpleworkshopsoftware.dto.WorkOrderTableDTO;
import com.simpleworkshopsoftware.entities.WorkOrder;
import com.simpleworkshopsoftware.factories.WorkOrderFactory;
import com.simpleworkshopsoftware.repo.WorkOrderRepository;
import com.simpleworkshopsoftware.utils.FieldUtils;
import com.simpleworkshopsoftware.validators.InputValidator;
import com.simpleworkshopsoftware.validators.WorkOrderInputValidator;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
/**
 * This class is responsible for the interactions of the work order UI.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrderController implements Initializable {

    @FXML
    private Label kmLabel, firstTxALabel, secondTxALabel;

    @FXML
    private Button createInternalWorkOrderButton, createRequestButton, createWorkOrderButton,
            finishedServicesButton, addToTableButton, removeFromTableButton;

    @FXML
    private TextArea firstTxA, secondTxA;

    @FXML
    private TextField expectedDateTf, overallCostTf, laborCostTf,
            discountPercentTf, partsCostTf, kmTf,
            noteTf, tableJobNameTf, tablePartNameTf,
            tableQuantityTf, tableUnitPriceTf, technicianTf;

    @FXML
    private VBox requestFirstVBox, requestSecondVBox,
            workOrderFirstVBox, workOrderSecondVBox;

    @FXML
    private ComboBox<String> workOrderOrRequestComboBox, unitChoice;

    @FXML
    private TableView<WorkOrderTableDTO> workOrderTable;

    private final ControllerConnector connector = ControllerConnector.getInstance();
    private final WorkOrderRepository workRepo = Database.getWorkOrderRepository();
    private WorkOrderTableController tableController;
    private InputValidator validator;
    private TextField[] fields;
    private TextField[] tableFields;
    private VBox[] vBoxes;
    private static final String UNIT_PART = "db";
    private static final String UNIT_FLUID = "l";
    private static final String UNIT_WORK = "h";
    private int selectedCarKm;
    private Integer selectedCarId;
    private WorkOrder selectedWorkOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validator = new WorkOrderInputValidator();
        initializeFields();
        initializeTable();
        initializeComboBoxes();
        initializeButtons();
        initializeListeners();
    }

    private void initializeListeners() {
        connector.customerSelectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setIdAndKm();
            }
        });
        connector.selectedCustomerIdProperty().addListener((observable, oldId, newId) -> {
            // Also trigger update if not the same customer selected
            if (connector.isCustomerSelected() && !Objects.equals(newId, oldId)) {
                setIdAndKm();
            }
        });
        connector.workOrderOrRequestSelectedProperty().addListener((obs, oldValue, newValue) -> {
            fillWorkOrderFields();
            setWorkOrderIdAndState();
        });
        connector.selectedWorkOrderIdProperty().addListener((observable, oldId, newId) -> {
            // Trigger update if a workOrder is selected and the ID has changed
            if (connector.workOrderOrRequestSelectedProperty().getValue() && !Objects.equals(newId, oldId)) {
                fillWorkOrderFields();
                setWorkOrderIdAndState();
            }
        });
        // if discount percent given, it updates the parts, and total costs
        discountPercentTf.textProperty().addListener((obs, oldValue, newValue) -> {
                FieldUtils.totalPartsAndLabor(partsCostTf, laborCostTf, overallCostTf);
                FieldUtils.calculatePartsPrice(workOrderTable, discountPercentTf, partsCostTf);
        });
        // calculates the sum of labor cost
        laborCostTf.textProperty().addListener((obs, oldValue, newValue) -> {
            FieldUtils.totalPartsAndLabor(partsCostTf, laborCostTf, overallCostTf);
        });
        partsCostTf.textProperty().addListener((obs, oldValue, newValue) -> {
            FieldUtils.totalPartsAndLabor(partsCostTf, laborCostTf, overallCostTf);
        });
        // if a new row added to the tableview
        workOrderTable.getItems().addListener((ListChangeListener<WorkOrderTableDTO>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() && !workOrderTable.getItems().isEmpty()) {
                    FieldUtils.totalOfLabor(workOrderTable, laborCostTf);
                    FieldUtils.calculatePartsPrice(workOrderTable, discountPercentTf, partsCostTf);
                }
            }
        });
        // if any text typed in this Tf, then it disables the parts Tf, and sets the unit and loads the default labor cost
        tableJobNameTf.textProperty().addListener((obs, oldValue, newValue) -> {
            tablePartNameTf.setEditable(false);
            unitChoice.setValue(UNIT_WORK);
            FieldUtils.setUnitPriceToDefaultLaborCost(tableJobNameTf, tableUnitPriceTf);
            if (newValue.isEmpty()) {
                tablePartNameTf.setEditable(true);
                unitChoice.setValue(null);
            }
        });
        // if any text typed in this Tf, then it disables the job name Tf
        tablePartNameTf.textProperty().addListener((obs, oldValue, newValue) -> {
            tableJobNameTf.setEditable(false);
            if (newValue.isEmpty()) {
                tableJobNameTf.setEditable(true);
            }
        });
    }
    /**
     * Initializes the behavior of buttons in the UI.
     * - The "Finished Services" and "Create Work Order" button is disabled
     * if a work order or request is not selected or if a work order is already selected.
     * - The "Create Request" button is disabled if a car ID is not selected.
     * - The "Add To Table" button is disabled if any of the following conditions are met:
     * - tableUnitPriceTf text field is empty
     * - tableQuantityTf text field is empty
     * - unitChoice ComboBox is empty (null)
     * - both tableJobNameTf and tablePartNameTf text fields are empty
     * - The "Remove From Table" button is disabled if there is no selected item in the workOrderTable.
     */
    private void initializeButtons() {
        finishedServicesButton.disableProperty()
                .bind(connector.workOrderOrRequestSelectedProperty().not()
                        .or(connector.workOrderSelectedProperty()));
        createWorkOrderButton.disableProperty()
                .bind(connector.workOrderOrRequestSelectedProperty().not()
                        .or(connector.workOrderSelectedProperty()));
        createRequestButton.disableProperty()
                .bind(connector.selectedCarIdProperty().isNull());
        addToTableButton.disableProperty()
                .bind(tableUnitPriceTf.textProperty().isEmpty()
                        .or(tableQuantityTf.textProperty().isEmpty())
                        .or(unitChoice.valueProperty().isNull())  // Check if ComboBox is empty (null)
                        .or(tableJobNameTf.textProperty().isEmpty()
                                .and(tablePartNameTf.textProperty().isEmpty())));
        removeFromTableButton.disableProperty()
                .bind(workOrderTable.getSelectionModel().selectedItemProperty().isNull()
                );
    }
/**
 * This method populates the items for the workOrderOrRequestComboBox and unitChoice ComboBoxes.
 * It also sets up an action listener for the workOrderOrRequestComboBox to update the text and visibility
 * of certain labels based on the selected item.
 */
    private void initializeComboBoxes() {
        final String workOrder = "Munkalap";
        final String request = "Munkafelvevő lap";
        final String requestKmLabelText = "Km óra állása érkezéskor";
        final String workOrderKmLabelText = "Km óra állása távozáskor";
        final String requestFirstTxALabelText = "Hiba leírása";
        final String requestSecondTxALabelText = "Várható javítása";
        final String workOrderFirstTxALabelText = "Elvégzett javítások";
        final String workOrderSecondTxALabelText = "Várható javítások a jövőben";
        workOrderOrRequestComboBox.getItems().addAll(workOrder, request);
        // set the labels text based on the selection
        workOrderOrRequestComboBox.setOnAction(event -> {
            String selection = workOrderOrRequestComboBox.getSelectionModel().getSelectedItem();
            if (selection == null) {
                firstTxALabel.setVisible(false);
                secondTxALabel.setVisible(false);
            }
            boolean isWorkOrderSelected = Objects.equals(selection, workOrder);
            kmLabel.setText(isWorkOrderSelected ? workOrderKmLabelText : requestKmLabelText);
            firstTxALabel.setText(isWorkOrderSelected ? workOrderFirstTxALabelText : requestFirstTxALabelText);
            secondTxALabel.setText(isWorkOrderSelected ? workOrderSecondTxALabelText : requestSecondTxALabelText);
            firstTxALabel.setVisible(true);
            secondTxALabel.setVisible(true);
            showVBoxesContent(isWorkOrderSelected, vBoxes);

        });
        unitChoice.getItems().addAll(UNIT_PART, UNIT_FLUID, UNIT_WORK);
    }

    private void initializeTable() {
        tableController = new WorkOrderTableController(workOrderTable);
    }

    private void initializeFields() {
        this.fields = new TextField[]{expectedDateTf, kmTf, noteTf, discountPercentTf,
                partsCostTf, laborCostTf, overallCostTf, technicianTf};
        this.tableFields = new TextField[]{tableJobNameTf, tablePartNameTf,tableQuantityTf, tableUnitPriceTf};
        this.vBoxes = new VBox[]{requestFirstVBox, requestSecondVBox,
                workOrderFirstVBox, workOrderSecondVBox};
    }
    /**
     * Sets the selected car's ID and kilometers based on the default view's table selection.
     */
    private void setIdAndKm() {
        this.selectedCarKm = connector.getCustCarDto().getCar().getKilometers();
        this.selectedCarId = connector.getCustCarDto().getCar().getId();

    }
    /**
     * Sets the selected work order's ID and state based on the default views table selection.
     */
    private void setWorkOrderIdAndState() {
        if (connector.getWorkOrderDto().getWo() != null) {
            this.selectedWorkOrder = connector.getWorkOrderDto().getWo();
        }
    }
    /**
     * Populate work order fields with data if the default view's table has a selection.
     */
    private void fillWorkOrderFields() {
        if (connector.getWorkOrderOrRequestSelected()) {
            FieldUtils.setWorkOrderFields(fields, tableController);
        } else {
            clearWorkOrderFields();
            firstTxALabel.setVisible(false);
            secondTxALabel.setVisible(false);
        }
    }

    private void clearWorkOrderFields() {
        tableController.removeAllData();
        FieldUtils.clearFields(fields);
        FieldUtils.clearArea(firstTxA);
        FieldUtils.clearArea(secondTxA);
        workOrderOrRequestComboBox.setValue(null);
    }

    private void showVBoxesContent(boolean isWorkOrder, VBox[] vBoxes) {
        for (VBox v : vBoxes) {
            if (v.getId().startsWith("w")) {  // Checks the fxid for Work Order
                v.setVisible(isWorkOrder);
            } else if (v.getId().startsWith("r")) {
                v.setVisible(!isWorkOrder);
            }
        }
    }

    @FXML
    void onRemoveFromTableButtonClicked(ActionEvent event) {
        WorkOrderTableDTO selectedItem = workOrderTable.getSelectionModel().getSelectedItem();
        workOrderTable.getItems().remove(selectedItem);
    }
    /**
     * This method performs input validation, and it calculates the multiplied unit price,
     * or return, when it fails. Then creates a new WorkOrderTableDTO object using
     * the provided job name, or part name, quantity, unit, and multiplied unit price.
     * The new data is added to the table using the TableController, and the text fields are cleared.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    void onAddToTableButtonClicked(ActionEvent event) {
        validator.addValidation(tableUnitPriceTf, val -> val.matches("\\d+"));
        validator.addValidation(tableQuantityTf, val -> val.matches("^\\d+(,\\d+)?$"));
        if (validator.validate()) {
            String multipliedUnitPrice;
            try {
                multipliedUnitPrice = FieldUtils.multiplyWithQuantity(tableQuantityTf, tableUnitPriceTf);
            } catch (ParseException e) {
                PopupWindowsController.showDialog(
                        "Nem sikerült az érték mező kiszámítása, kérlek ellenőrizd a megadott számokat", DialogType.WARNING);
                return;
            }
            WorkOrderTableDTO dto = new WorkOrderTableDTO(
                    tableJobNameTf.getText(),
                    tablePartNameTf.getText(),
                    tableQuantityTf.getText(),
                    unitChoice.getValue(),
                    multipliedUnitPrice
            );
            tableController.addTableRow(dto);
            FieldUtils.clearFields(tableFields);
        }
    }
    /**
     * This method creates a new work order of type 'MF' (request) for the selected car.
     * It first checks if a car is selected, and if not, shows a warning dialog.
     * Then, it tries to find the last ID of a 'MF' work order in the current year.
     * This previous ID needed for the unique title creation.
     * After this, it tries to create and save the work order.
     * If any error occurs during the process, it shows an error dialog with the appropriate message.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    void onCreateRequestButtonClicked(ActionEvent event) {
        String message = "A munkalap mentése sikeres volt";
        if (selectedCarId == null) {
            PopupWindowsController.showDialog(
                    "Kérlek válaszd ki újra az autót, amihez munkalapot szeretnél társítani",
                    DialogType.WARNING);
        } else {
            try {
                int workOrderId = workRepo.findLastIdInCurrentYear(WorkOrder.WorkOrderType.MF, LocalDate.now().getYear());
                //  no passed requestTitle
                createWorkOrder(message, workOrderId, "", WorkOrder.WorkOrderType.MF);
            } catch (SQLException e) {
                PopupWindowsController.showDialog("Nem sikerült a munkafelvevő lap mentése", DialogType.WARNING, e);
            }
        }
    }
    /**
     * This method creates a new work order of type 'ML' (work order) associated with the selected type 'MF' request.
     * It first checks if a work order is selected instead of a request, and if so, shows a warning dialog.
     * Then, it tries to create and save the work order, and then it closes the status of the selected request.
     * If any error occurs during the process, it shows an error dialog with the appropriate message.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    void onCreateWorkOrderButtonClicked(ActionEvent event) {
        String message = "A munkalap mentése sikeres volt";
        if (selectedWorkOrder.isClosed()) {
            PopupWindowsController.showDialog("Ehhez a munkafelvevő laphoz már készült munkalap," +
                    " kérlek válassz másik munkafelvevő lapot, vagy készíts újat.", DialogType.WARNING);
        } else {
            try {
                createWorkOrder(message, selectedWorkOrder.getId(), selectedWorkOrder.getTitle(), WorkOrder.WorkOrderType.ML);
                // close the associated request also
                workRepo.updateClosed(selectedWorkOrder.getId(), true, selectedWorkOrder.getType());
            } catch (SQLException e) {
                PopupWindowsController.showDialog("Nem sikerült a munkalap mentése.", DialogType.WARNING, e);
            }
        }
    }
    /**
     * Creates a new work order of the specified type associated with the given ID.
     * @param message The message to be displayed in a dialog after successful work order creation.
     * @param workOrderId The ID of the last work order needed for the unique title creation.
     * @param requestTitle The title of the existing request. Needed to generate the work order title.
     * If it's a request creation, it's an empty string.
     * @param type The type of the new work order (either MF for request or ML for work order).
     * @throws SQLException If an error occurs while trying to save the new work order to the database.
     */
    private void createWorkOrder(
            String message, int workOrderId, String requestTitle, WorkOrder.WorkOrderType type) throws SQLException {
        String data = tableController.retrieveDataFromTable();
        WorkOrderFactory workOF = new WorkOrderFactory();
        WorkOrder wo = workOF.makeWorkOrder(type, workOrderId, requestTitle,
                selectedCarId, data, fields, firstTxA, secondTxA);
        if (processAndSaveWorkOrder(selectedCarId, selectedCarKm, wo)) {
            clearWorkOrderFields();
            hideLabels();
            PopupWindowsController.showDialog(message, DialogType.INFORMATION);
        }
    }

    private void hideLabels() {
        firstTxALabel.setVisible(false);
        secondTxALabel.setVisible(false);
    }

    private boolean processAndSaveWorkOrder(int carId, int oldKm, WorkOrder wo) throws SQLException {
        // if the previous work order creation failed, this value is null
        if (wo == null) {
            PopupWindowsController.showDialog("Kérlek javítsd a beviteli mezőket",
                    DialogType.WARNING);
            return false;
        }
        // checks if the typed kilometer is less than the previous one, then sets it
        if (!FieldUtils.setKm(kmTf, carId, oldKm, connector)) {
            return false;
        }
        workRepo.insert(wo);
        return true;
    }

    /**
     * Opens a popup window where the user can check the fields of the finished services.
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void onFinishedServicesButtonClicked(ActionEvent event) {
        ServiceIntervalsStageController serviceController = new ServiceIntervalsStageController();
        serviceController.ServiceDonePopupStage(selectedCarId, selectedCarKm, connector);
    }
}

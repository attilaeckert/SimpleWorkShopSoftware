package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.base.Database;
import com.simpleworkshopsoftware.controller.PopupWindowsController.DialogType;
import com.simpleworkshopsoftware.entities.Car;
import com.simpleworkshopsoftware.entities.Customer;
import com.simpleworkshopsoftware.factories.CarFactory;
import com.simpleworkshopsoftware.factories.CustomerFactory;
import com.simpleworkshopsoftware.mapper.DTOMapper;
import com.simpleworkshopsoftware.repo.CarRepository;
import com.simpleworkshopsoftware.repo.CustomerRepository;
import com.simpleworkshopsoftware.utils.FieldUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is responsible for the interactions of the customer UI.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */

public class CustomerController implements Initializable {

    @FXML
    private Button newCarButton, newCustomerButton, deleteCarButton
            ,modifyCarButton, modifyCustomerButton, deleteCustomerButton;

    @FXML
    private TextField phoneTf, vinTf, yearTf, compTaxNumTf, addressTf,
                        brandTf, compAddressTf, compEmailTf, compNameTf,
                        compPhoneTf, emailTf, engineTf, firstNameTf,
                        kmTf, lastNameTf, licenseTf, licensevalTf,
                        modelTf;

    @FXML
    private VBox companyModVbox;

    @FXML
    private VBox personModVbox;

    @FXML
    private ListView<Car> customerCarListView;

    @FXML
    private ChoiceBox<String> customerTypeChoice;

    @FXML
    private ChoiceBox<String> fuelTypeChoice;

    private final String CompanyChoice = "Cég";
    private final String PersonChoice = "Magánszemély";
    private TextField[] companyFields;
    private TextField[] personalFields;
    private TextField[] carFields;
    private TextField[] allFields;
    private final ControllerConnector connector = ControllerConnector.getInstance();
    private final CarRepository carRepo = Database.getCarRepository();
    private final CustomerRepository custRepo = Database.getCustomerRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeFields();
        initializeListeners();
        initializeButtons();
        customerTypeChoiceInitialize();
        fuelTypeChoiceInitialize();
    }
    /**
     * The method sets up the following bindings:
     * - The "New Customer" button is disabled if a customer is already selected or if no customer type is chosen.
     * - The "Modify Customer" button is disabled if no customer type is chosen.
     * - The "Delete Customer" button is disabled if no customer is selected.
     * - The "New Car" button is disabled if no customer is selected or if a car is already selected.
     * - The "Modify Car" and "Delete Car" buttons are disabled if no car is selected.
     */
    private void initializeButtons() {
        newCustomerButton.disableProperty()
                .bind(connector.customerSelectedProperty()
                .or(customerTypeChoice.valueProperty().isNull()));
        modifyCustomerButton.disableProperty()
                .bind(customerTypeChoice.valueProperty().isNull());
        deleteCustomerButton.disableProperty()
                .bind(connector.customerSelectedProperty().not());
        newCarButton.disableProperty()
                .bind(connector.customerSelectedProperty().not()
                        .or(customerCarListView.getSelectionModel().selectedItemProperty().isNotNull()));
        modifyCarButton.disableProperty()
                .bind(customerCarListView.getSelectionModel().selectedItemProperty().isNull());
        deleteCarButton.disableProperty()
                .bind(customerCarListView.getSelectionModel().selectedItemProperty().isNull());
    }
    /**
     * Initializes listeners for various properties to update the UI based on changes in customer and car selections.
     * This method sets up listeners to respond to changes in the selected customer and car, updating the UI accordingly.
     * It ensures that the customer details and car fields are filled when a selection is made or changed.
     */
    private void initializeListeners() {
        connector.customerSelectedProperty().addListener((observable, oldValue, newValue) -> fillCustomerDetails());
        connector.selectedCustomerIdProperty().addListener((observable, oldId, newId) -> {
            // Also trigger update if not the same customer selected
            if (connector.isCustomerSelected() && !Objects.equals(newId, oldId)) {
                fillCustomerDetails();
                }
        });
        customerCarListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // trigger update if a car selected
                fillCarFields(customerCarListView.getSelectionModel().getSelectedItem());
                fuelTypeChoice.setValue(newValue.getFuelType());
            }
        });
    }

    private void initializeFields() {
        companyFields = new TextField[] {compNameTf, compAddressTf, compEmailTf, compPhoneTf, compTaxNumTf};
        personalFields = new TextField[] {lastNameTf, firstNameTf, addressTf, emailTf, phoneTf};
        carFields = new TextField[]{brandTf, modelTf, vinTf, engineTf, yearTf, licenseTf, licensevalTf, kmTf};
        allFields = new TextField[]{phoneTf, vinTf, yearTf, compTaxNumTf, addressTf,
                brandTf, compAddressTf, compEmailTf, compNameTf,
                compPhoneTf, emailTf, engineTf, firstNameTf,
                kmTf, lastNameTf, licenseTf, licensevalTf,
                modelTf};
    }

    private void fuelTypeChoiceInitialize() {
        fuelTypeChoice.getItems().addAll("Benzin", "Dízel", "Hybrid", "Elektromos", "Gáz");
    }
    /**
     * Initializes the customer type choice box with options for selecting the type of customer.
     * It sets an event handler that updates the UI when the customer type is changed.
     */
    private void customerTypeChoiceInitialize() {
        customerTypeChoice.getItems().addAll(CompanyChoice, PersonChoice);
        customerTypeChoice.setOnAction((event) -> {
            if (customerTypeChoice.getSelectionModel().getSelectedItem().equals(CompanyChoice)) {
                companyModVbox.setVisible(true);
                personModVbox.setVisible(false);
            } else {
                personModVbox.setVisible(true);
                companyModVbox.setVisible(false);
            }
        });
    }
    /**
     * Updates the UI with the details of the currently selected customer.
     * If a customer is selected, it fills the customer fields and sets the list of cars associated with the customer.
     * If no customer is selected, it clears the customer fields and the car list view.
     */
    private void fillCustomerDetails() {
        if (connector.isCustomerSelected()) {
            fillCustomerFields();
                setCustomerCarListView(connector.getSelectedCustomerId());
        } else {
            clearCustomerFields();
            customerCarListView.getItems().clear();
            clearCarFields();
        }
    }

    private void refreshListView() {
        setCustomerCarListView(connector.getSelectedCustomerId());
    }
    /**
     * Sets the list of cars associated with a specific customer in the ListView.
     * If the customer ID is valid, it retrieves the cars from the repository and displays them.
     *
     * @param id the ID of the customer whose cars are to be displayed. If null, a placeholder is shown.
     */
    private void setCustomerCarListView(Integer id){
        if (id != null) {
            ObservableList<Car> cars = null;
            try {
                cars = DTOMapper.toObservableList(carRepo.findByCustomerId(id));
            } catch (SQLException e) {
                PopupWindowsController.showDialog("Nem sikerült beállítani az autók listáját", DialogType.WARNING, e);
            }
            customerCarListView.setItems(cars);
        } else {
            customerCarListView.setPlaceholder(new Text(("Nincs megjeleníthető autó")));
        }
    }

    private void clearCustomerFields() {
            FieldUtils.clearFields(allFields);
        FieldUtils.clearChoiceBox(customerTypeChoice);
    }

    private void clearCarFields() {
        FieldUtils.clearFields(carFields);
        FieldUtils.clearChoiceBox(fuelTypeChoice);
    }
    /**
     * Populates the customer fields in the UI with the details of the currently selected customer.
     * It retrieves the customer data from the connector and uses utility methods to set the fields.
     */
    private void fillCustomerFields() {
        // the current selected customer
        Customer cust = connector.getCustCarDto().getCust();
        FieldUtils.setCustomerFields(cust, customerTypeChoice, CompanyChoice, PersonChoice,
                companyFields, personalFields);
    }

    private void fillCarFields(Car car) {
        FieldUtils.setCarFields(car, carFields, fuelTypeChoice);
    }

    private Customer createCustomer() {
        boolean isCompany = customerTypeChoice.getValue().equals(CompanyChoice);
        CustomerFactory customerFactory = new CustomerFactory();
        Customer cust;
        if (isCompany) {
            cust = customerFactory.makeCustomer(true, companyFields);
        } else {
            cust = customerFactory.makeCustomer(false, personalFields);
        }
        return cust;
    }

    @FXML
    void onNewCustomerButtonClick(ActionEvent event) {
        try {
            processCustomer();
        } catch (Exception e) {
            PopupWindowsController.showDialog("Nem sikerült az ügyfél mentése",
                    DialogType.WARNING, e);
        }
    }

    @FXML
    void onModifyCustomerButtonClick(ActionEvent event) {
        try {
            processCustomer();
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Nem sikerült az ügyfél frissítése",
                    DialogType.WARNING, e);
        }
    }
    
    @FXML
    void onDeleteCustomerButtonClick(ActionEvent event) {
        boolean shouldDelete = PopupWindowsController.deleteConfirmation("Biztosan törölni szeretnéd a kiválasztott ügyfelet?");
        if (shouldDelete) {
            Integer idToDelete = connector.getCustCarDto().getCust().getId();
            try {
                custRepo.delete(idToDelete);
            } catch (SQLException e) {
                PopupWindowsController.showDialog("Nem sikerült az ügyfél törlése", DialogType.WARNING, e);
            }
            PopupWindowsController.showDialog("Az ügyfél törlése sikeres volt.", DialogType.INFORMATION);
        }
    }

    /**
     * Processes customer information by either inserting a new customer or updating an existing one.
     * It validates the customer fields and displays appropriate messages based on the operation's success.
     *
     * @throws SQLException if a database access error occurs or the SQL operation fails.
     */
    private void processCustomer() throws SQLException {
        Integer id = connector.getSelectedCustomerId();
        String message = "Kérlek javítsd a beviteli mezőket";
        Customer cust = createCustomer();
        if (cust == null) {
            PopupWindowsController.showDialog(message,
                    DialogType.WARNING);
            return;
        }
        if (id == null) {
            message = "Az ügyfél mentése sikeres volt";
                custRepo.insert(cust);
                cust.setId(custRepo.findGeneratedId()); //retrieve the generated id from the database and set it

        } else {
            message = "Az ügyfél adatainak módosítása sikeres volt";
            cust.setId(id);
            custRepo.update(cust);
        }
        clearCustomerFields();
        connector.customerOrCarAddedOrChangedProperty().set(true);
        PopupWindowsController.showDialog(message,
                DialogType.INFORMATION);
    }

    @FXML
    void onNewCarButtonClick(ActionEvent event) {
        try {
            processCar();
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Nem sikerült az autó mentése",
                    DialogType.WARNING, e);
        }
        refreshListView();
    }

    @FXML
    void onModifyCarButtonClick(ActionEvent event) {
        try {
            processCar();
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Nem sikerült az autó adatainak frissítése",
                    DialogType.WARNING, e);
        }
        refreshListView();
    }

    @FXML
    void onDeleteCarButtonClick(ActionEvent event) {
        boolean shouldDelete = PopupWindowsController.deleteConfirmation("Biztosan törölni szeretnéd a kiválasztott autót?");
        if (shouldDelete) {
            Integer idToDelete = customerCarListView.getSelectionModel().getSelectedItem().getId();
            try {
                carRepo.delete(idToDelete);
            } catch (SQLException e) {
                PopupWindowsController.showDialog("Nem sikerült az autó törlése", DialogType.WARNING, e);
            }
            PopupWindowsController.showDialog("Az autó törlése sikeres volt.", DialogType.INFORMATION);
            refreshListView();
        }
    }

    /**
     * Processes the car information by either inserting a new car or updating an existing one.
     * It validates the car fields and displays appropriate messages based on the operation's success.
     *
     * @throws SQLException if a database access error occurs or the SQL operation fails.
     */
    private void processCar() throws SQLException {
        String message = "Kérlek javítsd a beviteli mezőket";
        CarFactory carFactory = new CarFactory();
        Car car = carFactory.makeCar(carFields, fuelTypeChoice);
        if (car == null) {
            PopupWindowsController.showDialog(message,
                    DialogType.WARNING);
            return;
        }
        int selectedIndex = customerCarListView.getSelectionModel().getSelectedIndex();
        Integer carId = null;
        if (selectedIndex >= 0) {
            carId = customerCarListView.getItems().get(selectedIndex).getId();
        }
        // insert
        if (carId == null) {
            message = "Az autó mentése sikeres volt";
            car.setCustomerId(connector.getSelectedCustomerId());
            carRepo.insert(car);
            // update
        } else {
            car.setId(carId);
            carRepo.update(car);
            message = "Az autó adatainak módosítása sikeres volt";
        }
        clearCarFields();
        connector.customerOrCarAddedOrChangedProperty().set(true);
        PopupWindowsController.showDialog(message,
                DialogType.INFORMATION);
    }
}
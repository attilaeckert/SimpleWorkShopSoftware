package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.base.Database;
import com.simpleworkshopsoftware.controller.tableControllers.CarWorkOrderTableController;
import com.simpleworkshopsoftware.controller.tableControllers.CustomerCarTableController;
import com.simpleworkshopsoftware.dto.CustomerCarDTO;
import com.simpleworkshopsoftware.dto.PdfDTO;
import com.simpleworkshopsoftware.dto.WorkOrderDTO;
import com.simpleworkshopsoftware.pdfGenerator.PdfGenerator;
import com.simpleworkshopsoftware.pdfGenerator.RequestPdfGenerator;
import com.simpleworkshopsoftware.pdfGenerator.WorkOrderPdfGenerator;
import com.simpleworkshopsoftware.pdfGenerator.WorkSheetPdfGenerator;
import com.simpleworkshopsoftware.utils.TextUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
/**
 * This class is responsible for the interactions of the default or main UI.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class DefaultController implements Initializable {

    @FXML
    private TableView<CustomerCarDTO> customerCarTable;

    @FXML
    private TableView<WorkOrderDTO> carWorkOrderTable;

    @FXML
    private TextArea carWorkOrderTa;

    @FXML
    private Label defaultTextAreaLabel, defaultTextFlowLabel;

    @FXML
    private Button createPdfButton, newKmButton,
            serviceIntervalsButton, serviceWorkOrderPdfButton;

    @FXML
    private TextFlow serviceDueTextFlow;

    @FXML
    private TextField searchTf;

    private final Label carWorkOrderTablePlaceHolder = new Label("Nincs megjeleníthető munkalap");
    private final Label customerCarTablePlaceHolder = new Label("Nincs megjeleníthető ügyfél");
    private final ControllerConnector connector = ControllerConnector.getInstance();
    private CustomerCarTableController customerCarTableController;
    private CarWorkOrderTableController carWorkOrderTableController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        defaultTextAreaLabel.setVisible(true);
        defaultTextFlowLabel.setVisible(true);
        initializeTables();
        initializeListeners();
        initializeButtons();
    }

    private void initializeTables() {
        customerCarTableController = new CustomerCarTableController(customerCarTable, searchTf);
        customerCarTable.setPlaceholder(customerCarTablePlaceHolder);
        carWorkOrderTableController = new CarWorkOrderTableController(carWorkOrderTable);
        carWorkOrderTable.setPlaceholder(carWorkOrderTablePlaceHolder);
        getListAndPopulateFirstTable();
        applySearchBarToFirstTable();
    }
    /**
     * Initializes the behavior of buttons in the UI.
     * This method sets the disable properties of buttons based on the state of the application,
     * specifically the selected items and properties set by the ControllerConnector.
     * - The "Create PDF" button is disabled if no work order or request is selected
     * - The "Service Work Order PDF" button is disabled  if no work order is selected
     * - The "New Kilometer" and "Service Intervals" buttons are disabled  if no car is selected
     */
    private void initializeButtons() {
        createPdfButton.disableProperty().bind(connector.workOrderOrRequestSelectedProperty().not());
        serviceWorkOrderPdfButton.disableProperty()
                .bind(connector.workOrderSelectedProperty()
                        .or(connector.workOrderOrRequestSelectedProperty().not()));
        newKmButton.disableProperty().bind(connector.selectedCarIdProperty().isNull());
        serviceIntervalsButton.disableProperty().bind(connector.selectedCarIdProperty().isNull());
    }

    private void initializeListeners() {
        connector.customerOrCarAddedOrChangedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                refreshFirstTable();
                connector.customerOrCarAddedOrChangedProperty().set(false);
                connector.setCustomerAndCarLabels();
            }
        });
        connector.workOrderOrRequestSelectedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                carWorkOrderTa.setText("");
                carWorkOrderTableController.removalAllData();
            }
        });
        carWorkOrderTa.textProperty().addListener((obs, oldValue, newValue) -> {
            defaultTextAreaLabel.setVisible(newValue == null || newValue.isEmpty());
        });
        connector.customerSelectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                refreshFirstTable();
                carWorkOrderTableController.removalAllData();
            }
        });
        connector.selectedCarIdProperty().addListener((observable, oldId, newId) -> {
            // Also trigger update if not the same customer selected
            if (connector.isCustomerSelected() && !Objects.equals(newId, oldId)) {
                TextUtils.calculateServiceDue(connector, serviceDueTextFlow);
                defaultTextFlowLabel.setVisible(false);
            } else if (newId == null) {
                TextUtils.clearServiceDueTextFlow(serviceDueTextFlow);
                defaultTextFlowLabel.setVisible(true);
            }
        });
    }
    /**
     * Handles the mouse click event on the first table.
     * This method retrieves the selected DTO from the table and updates
     * the corresponding data in the controller connector class.
     * It also resets the selected work order and populates the second table
     * with relevant data if a customer has a car.
     *
     * @param event The MouseEvent that triggered this action.
     */
    @FXML
    void getCustomerCarItem(MouseEvent event) {
        int selectedIndex = customerCarTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            //  reset the selected workOrder
            connector.setWorkOrderDto(null);
            CustomerCarDTO selectedDto = customerCarTable.getItems().get(selectedIndex);
            Integer selectedCarId = selectedDto.getCar().getId();
            // Check if the selected row is the same as the last selection
            if (connector.getCustCarDto() == null || !selectedDto.equals(connector.getCustCarDto())) {
                // Update the dto with the new selection
                connector.setCustCarDto(selectedDto);
            } else {
                //  reset the selected workOrder
                connector.setWorkOrderDto(null);
                // reset the selection, to allow to re-select the same row again
                connector.setCustCarDto(null);
                connector.setCustCarDto(selectedDto);
            }
            // Load the data in the other table
            if (selectedCarId != null) {
                getListAndPopulateSecondTable(selectedCarId);
            }
        }
    }

    private void refreshFirstTable() {
        getListAndPopulateFirstTable();
        applySearchBarToFirstTable();
    }

    private void getListAndPopulateFirstTable() {
        try {
            List<CustomerCarDTO> tableList = Database.getCustomerCarDTORepository().findAll();
            customerCarTableController.populateTable(tableList);
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Nem sikerült betölteni az ügyfél táblát",
                    PopupWindowsController.DialogType.WARNING, e);
        }
    }

    private void applySearchBarToFirstTable() {customerCarTableController.setSearchBar();}

    private void getListAndPopulateSecondTable(Integer selectedCarId) {
        try {
            List<WorkOrderDTO> tableList = Database.getWorkOrderDTORepository().findAll(selectedCarId);
            carWorkOrderTableController.populateTable(tableList);
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Nem sikerült betölteni a munkalapok táblát",
                    PopupWindowsController.DialogType.WARNING, e);
        }
    }
    /**
     * Handles the mouse click event on the second table.
     * This method retrieves the selected DTO from the second table and updates
     * the WorkOrderDTO in the controller connector class.
     * It also fills the text area with the selected work order data.
     *
     * @param event The MouseEvent that triggered this action.
     */
    @FXML
    void getCarWorkOrderItem(MouseEvent event) {
        int index = carWorkOrderTable.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            WorkOrderDTO dto = carWorkOrderTable.getItems().get(index);
            Integer id = dto.getWo().getId();
            if (id != null) {
                connector.setWorkOrderDto(dto);
                carWorkOrderTa.setText(TextUtils.fillTextArea(connector.getWorkOrderDto().getWo()));
            }
        }
    }
    /**
     * This method creates a PdfDTO object with customer, car, and work order information,
     * initializes a generator based on the selected work order,
     * and then calls handlePdfCreation to generate the PDF.
     *
     * @param event The ActionEvent triggered by clicking the Service Work Order PDF button.
     */
    @FXML
    void onCreatePdfButtonClicked(ActionEvent event) {
        PdfDTO dto = new PdfDTO(connector.getCustCarDto().getCust(),
                connector.getCustCarDto().getCar(),
                connector.getWorkOrderDto().getWo());
        PdfGenerator generator = connector.decideIsWorkOrder()
                ? new WorkOrderPdfGenerator()
                : new RequestPdfGenerator();
        handlePdfCreation(generator, dto);
    }
    /**
     * This method creates a PdfDTO object with customer, car, and work order information,
     * initializes a WorkSheetPdfGenerator, and then calls handlePdfCreation to generate the PDF
     * for internal use in the work shop.
     *
     * @param event The ActionEvent triggered by clicking the Service Work Order PDF button.
     */
    @FXML
    void onServiceWorkOrderPdfButtonClicked(ActionEvent event) {
        PdfDTO dto = new PdfDTO(connector.getCustCarDto().getCust(),
                connector.getCustCarDto().getCar(),
                connector.getWorkOrderDto().getWo());
        WorkSheetPdfGenerator generator = new WorkSheetPdfGenerator();
        handlePdfCreation(generator, dto);
    }
    /**
     * Handles the creation of a PDF document based on the selected work order or request.
     * This method takes a PdfGenerator instance and a PdfDTO object as parameters.
     * It attempts to create a PDF document using the provided generator and data.
     * If an exception occurs during the PDF creation process, it shows a warning dialog
     * with an appropriate error message.
     * After the PDF creation, it shows an information dialog with the file path of the saved PDF.
     *
     * @param generator The PdfGenerator instance to be used for creating the PDF.
     * @param dto The PdfDTO object containing the necessary data for the PDF creation.
     */
    private void handlePdfCreation(PdfGenerator generator, PdfDTO dto) {
        try {
            generator.createPdf(dto);
            PopupWindowsController.showDialog("PDF mentve a következő helyre:\n" +
                    dto.getFilePath(), PopupWindowsController.DialogType.INFORMATION);
        } catch (Exception e) {
            PopupWindowsController.showDialog(
                    "Nem sikerült létrehozni a PDF dokumentumot",
                    PopupWindowsController.DialogType.WARNING, e);
        }
    }

    @FXML
    void onNewKmButtonClicked(ActionEvent event) {
        PopupWindowsController.newKmWindow(connector);
        TextUtils.calculateServiceDue(connector, serviceDueTextFlow);
    }

    @FXML
    void onServiceIntervalsButtonClicked(ActionEvent event) throws SQLException {
        ServiceIntervalsStageController serviceController = new ServiceIntervalsStageController();
        serviceController.ServicePeriodsPopupStage();
    }
}
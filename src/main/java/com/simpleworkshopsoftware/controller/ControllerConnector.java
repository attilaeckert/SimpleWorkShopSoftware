package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.dto.CustomerCarDTO;
import com.simpleworkshopsoftware.dto.WorkOrderDTO;
import com.simpleworkshopsoftware.entities.WorkOrder;
import javafx.beans.property.*;

/**
 * The ControllerConnector class is a singleton that serves as a bridge between the controllers.
 * It manages the state of selected customer and car information;
 * as well as work orders, showing the user the selection by three labels.
 * Each label shows the selected customer, car, or work order, or a default value.
 * It provides properties that can be bound to UI components
 * such as buttons, and labels, or it triggers a listener.
 * The CustomerCarDTO represents the selected customer, and its car,
 * while the WorkOrderDTO represents the selected work order.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class ControllerConnector {

    private static ControllerConnector instance = null;
    private final StringProperty customerDisplayLabelText;
    private final StringProperty carDisplayLabelText;
    private final StringProperty workOrderDisplayLabelText;
    private final ObjectProperty<Integer> selectedCustomerId;
    private final ObjectProperty<Integer> selectedCarId;
    private final BooleanProperty customerSelected;
    private final ObjectProperty<Integer> selectedWorkOrderId;
    private final BooleanProperty workOrderOrRequestSelected;
    private final BooleanProperty workOrderSelected;
    private final BooleanProperty customerOrCarAddedOrChanged;
    private final String defaultCustomerDisplayLabelText = "Ügyfél neve";
    private final String defaultCarDisplayLabelText = "Autó adatai";
    private final String defaultWorkOrderDisplayLabelText = "Munkalap száma";
    private CustomerCarDTO custCarDto;
    private WorkOrderDTO workOrderDTO;

    private ControllerConnector() {
        carDisplayLabelText = new SimpleStringProperty(defaultCarDisplayLabelText);
        customerDisplayLabelText = new SimpleStringProperty(defaultCustomerDisplayLabelText);
        workOrderDisplayLabelText = new SimpleStringProperty(defaultWorkOrderDisplayLabelText);
        selectedCustomerId = new SimpleObjectProperty<>(null);
        selectedCarId = new SimpleObjectProperty<>(null);
        customerSelected = new SimpleBooleanProperty(false);
        selectedWorkOrderId = new SimpleObjectProperty<>(null);
        workOrderOrRequestSelected = new SimpleBooleanProperty(false);
        workOrderSelected = new SimpleBooleanProperty(false);
        customerOrCarAddedOrChanged = new SimpleBooleanProperty(false);
        this.custCarDto = null;
        this.workOrderDTO = null;
    }

    public static ControllerConnector getInstance() {
        if (instance == null) {
            instance = new ControllerConnector();
        }
        return instance;
    }

    /**
     * Sets the CustomerCarDTO and updates the relevant properties.
     * If the given CustomerCarDTO is null, it means that the user hit the unselect button.
     *
     * @param custCarDto the CustomerCarDTO to set
     */
    public void setCustCarDto(CustomerCarDTO custCarDto) {
        this.custCarDto = custCarDto;
        if (custCarDto != null) {
            customerSelectedProperty().set(true);
            selectedCustomerIdProperty().set(custCarDto.getCust().getId());
            selectedCarIdProperty().set(custCarDto.getCar().getId());
        } else {
            customerSelectedProperty().set(false);
            selectedCustomerIdProperty().set(null);
            selectedCarIdProperty().set(null);
        }
        setCustomerAndCarLabels();
    }

    /**
     * Updates the display labels for customer and car based on the current CustomerCarDTO.
     */
    public void setCustomerAndCarLabels() {
        this.customerDisplayLabelText.set
                (this.custCarDto != null ? this.custCarDto.getCust().toString() : defaultCustomerDisplayLabelText);
        this.carDisplayLabelText.set
                (this.custCarDto != null && this.custCarDto.getCar().getId() != null
                        ? this.custCarDto.getCar().toString() : defaultCarDisplayLabelText);
    }

    public CustomerCarDTO getCustCarDto() {
        return custCarDto;
    }

    public WorkOrderDTO getWorkOrderDto() {
        return workOrderDTO;
    }

    /**
     * Sets the WorkOrderDTO and updates the relevant properties.
     * If the given WorkOrderdto is null, it means that the user hit the unselect button.
     *
     * @param woDto the WorkOrderDTO to set
     */
    public void setWorkOrderDto(WorkOrderDTO woDto) {
        if (woDto != null) {
            this.workOrderDTO = woDto;
            selectedWorkOrderIdProperty().set(workOrderDTO.getWo().getId());
            workOrderOrRequestSelectedProperty().set(true);
            workOrderSelected.set(decideIsWorkOrder());
            this.workOrderDisplayLabelText.set
                    (workOrderDTO.getWo().getId() != null ? workOrderDTO.getWo().getTitle() : defaultWorkOrderDisplayLabelText);
        } else {
            // Reset to default values if dto is null (unselect button clicked)
            selectedWorkOrderIdProperty().set(null);
            workOrderOrRequestSelectedProperty().set(false);
            workOrderSelected.set(false);
            this.workOrderDisplayLabelText.set(defaultWorkOrderDisplayLabelText);
        }
    }

    public boolean decideIsWorkOrder() {
        return workOrderDTO.getWo().getType().equals(WorkOrder.WorkOrderType.ML);
    }

    public String getCustomerDisplayLabelText() {
        return customerDisplayLabelText.get();
    }

    public StringProperty customerDisplayLabelTextProperty() {
        return customerDisplayLabelText;
    }

    public void setCustomerDisplayLabelText(String customerDisplayLabelText) {
        this.customerDisplayLabelText.set(customerDisplayLabelText);
    }

    public String getCarDisplayLabelText() {
        return carDisplayLabelText.get();
    }

    public StringProperty carDisplayLabelTextProperty() {
        return carDisplayLabelText;
    }

    public void setCarDisplayLabelText(String carDisplayLabelText) {
        this.carDisplayLabelText.set(carDisplayLabelText);
    }

    public String getWorkOrderDisplayLabelText() {
        return workOrderDisplayLabelText.get();
    }

    public StringProperty workOrderDisplayLabelTextProperty() {
        return workOrderDisplayLabelText;
    }

    public void setWorkOrderDisplayLabelText(String workOrderDisplayLabelText) {
        this.workOrderDisplayLabelText.set(workOrderDisplayLabelText);
    }

    public Integer getSelectedCustomerId() {
        return selectedCustomerId.get();
    }

    public ObjectProperty<Integer> selectedCustomerIdProperty() {
        return selectedCustomerId;
    }

    public void setSelectedCustomerId(Integer selectedCustomerId) {
        this.selectedCustomerId.set(selectedCustomerId);
    }

    public Integer getSelectedCarId() {
        return selectedCarId.get();
    }

    public ObjectProperty<Integer> selectedCarIdProperty() {
        return selectedCarId;
    }

    public boolean isCustomerSelected() {
        return customerSelected.get();
    }

    public BooleanProperty customerSelectedProperty() {
        return customerSelected;
    }

    public void setCustomerSelected(boolean customerSelected) {
        this.customerSelected.set(customerSelected);
    }

    public Integer getSelectedWorkOrderId() {
        return selectedWorkOrderId.get();
    }

    public ObjectProperty<Integer> selectedWorkOrderIdProperty() {
        return selectedWorkOrderId;
    }

    public String getDefaultCarDisplayLabelText() {
        return defaultCarDisplayLabelText;
    }

    public String getDefaultCustomerDisplayLabelText() {
        return defaultCustomerDisplayLabelText;
    }

    public boolean getWorkOrderOrRequestSelected() {
        return workOrderOrRequestSelected.get();
    }

    public BooleanProperty workOrderOrRequestSelectedProperty() {
        return workOrderOrRequestSelected;
    }

    public void setWorkOrderOrRequestSelected(boolean workOrderOrRequestSelected) {
        this.workOrderOrRequestSelected.set(workOrderOrRequestSelected);
    }

    public boolean getWorkOrderSelected() {
        return workOrderSelected.get();
    }

    public BooleanProperty workOrderSelectedProperty() {
        return workOrderSelected;
    }

    public boolean getCustomerOrCarAddedOrChanged() {
        return customerOrCarAddedOrChanged.get();
    }

    public BooleanProperty customerOrCarAddedOrChangedProperty() {
        return customerOrCarAddedOrChanged;
    }

    public void setCustomerOrCarAddedOrChanged(boolean customerOrCarAddedOrChanged) {
        this.customerOrCarAddedOrChanged.set(customerOrCarAddedOrChanged);
    }
}

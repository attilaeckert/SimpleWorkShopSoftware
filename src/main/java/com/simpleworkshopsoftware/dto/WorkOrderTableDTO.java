package com.simpleworkshopsoftware.dto;

import javafx.beans.property.SimpleStringProperty;
/**
 * This class represents the data in the work order view's TableView.
 * It is part of the created work order.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrderTableDTO {

    private final SimpleStringProperty jobName;
    private final SimpleStringProperty partName;
    private final SimpleStringProperty quantity;
    private final SimpleStringProperty unit;
    private final SimpleStringProperty unitPrice;

    public WorkOrderTableDTO(String jobName, String partName, String quantity,
                             String unit, String unitPrice) {
        this.jobName = new SimpleStringProperty(jobName);
        this.partName = new SimpleStringProperty(partName);
        this.quantity = new SimpleStringProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
        this.unitPrice = new SimpleStringProperty(unitPrice);
    }

    public String getJobName() {
        return jobName.get();
    }

    public SimpleStringProperty jobNameProperty() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName.set(jobName);
    }

    public String getPartName() {
        return partName.get();
    }

    public SimpleStringProperty partNameProperty() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName.set(partName);
    }

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getUnitPrice() {
        return unitPrice.get();
    }

    public SimpleStringProperty unitPriceProperty() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }
}

package com.simpleworkshopsoftware.dto;

import com.simpleworkshopsoftware.base.IdHolder;
import com.simpleworkshopsoftware.entities.WorkOrder;
import javafx.beans.property.SimpleStringProperty;
/**
 * This class represents the data in the default view's second TableView.
 * It implements the IdHolder interface, although it doesn't have an ID.
 * IdHolder works as a wrapper here, to simplify repository creation.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrderDTO implements IdHolder<Integer> {

    private SimpleStringProperty kilometers;
    private SimpleStringProperty date;
    private SimpleStringProperty title;
    private WorkOrder wo;

    public WorkOrderDTO() {}

    public WorkOrderDTO(String kilometers,
                        String date, String title, WorkOrder wo) {
        this.kilometers = new SimpleStringProperty(kilometers);
        this.date = new SimpleStringProperty(date);
        this.title = new SimpleStringProperty(title);
        this.wo = wo;
    }

    public String getKilometers() {
        return kilometers.get();
    }

    public SimpleStringProperty kilometersProperty() {
        return kilometers;
    }

    public void setKilometers(String kilometers) {
        this.kilometers.set(kilometers);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public WorkOrder getWo() {
        return wo;
    }

    public void setWo(WorkOrder wo) {
        this.wo = wo;
    }

    @Override
    public Integer getId() {
        return wo != null ? wo.getId() : null;
    }
}

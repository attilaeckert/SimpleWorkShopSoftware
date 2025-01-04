package com.simpleworkshopsoftware.controller.tableControllers;

import com.simpleworkshopsoftware.dto.WorkOrderDTO;
import com.simpleworkshopsoftware.mapper.DTOMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
/**
 * The CarWorkOrderTableController class manages the display of work orders
 * in a TableView for cars. It is responsible for adding columns to the table,
 * populating the table with data, and clearing the table data.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CarWorkOrderTableController {
    private final TableView<WorkOrderDTO> table;
    private ObservableList<WorkOrderDTO> data;

    public CarWorkOrderTableController(TableView<WorkOrderDTO> table) {
        this.table = table;
        this.data = FXCollections.observableArrayList();
        addColumns();
        setTable();
    }

    private void addColumns() {
        final TableColumn<WorkOrderDTO, String> kmCol = new TableColumn<>("Km");
        kmCol.setCellValueFactory(new PropertyValueFactory<>("kilometers"));

        final TableColumn<WorkOrderDTO, String> dateCol = new TableColumn<>("Dátum");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        final TableColumn<WorkOrderDTO, String> titleCol = new TableColumn<>("Munkalap neve");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        this.table.getColumns().addAll(kmCol, dateCol, titleCol);
    }

    private void setTable() {
        this.table.setItems(this.data);
    }

    public void populateTable(List<WorkOrderDTO> list) {
        this.data = DTOMapper.toObservableList(list);
        this.table.setItems(this.data);
    }

    public void removalAllData() {
        this.data.clear();
        setTable();
    }
}
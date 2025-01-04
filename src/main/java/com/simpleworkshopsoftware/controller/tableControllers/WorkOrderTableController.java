package com.simpleworkshopsoftware.controller.tableControllers;

import com.simpleworkshopsoftware.controller.PopupWindowsController;
import com.simpleworkshopsoftware.dto.WorkOrderTableDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
/**
 * The WorkOrderTableController class manages the display of work order data
 * in a TableView. It provides functionality for adding rows, removing all data,
 * and retrieving data from the table. The controller is responsible for setting
 * up the table columns and handling the conversion of table data to and from
 * a string format.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrderTableController {
    private final TableView<WorkOrderTableDTO> table;
    private final ObservableList<WorkOrderTableDTO> data;
    private final String fieldDelimiter = ";";
    private final String rowDelimiter = "\n";

    public WorkOrderTableController(TableView<WorkOrderTableDTO> table) {
        this.table = table;
        this.data = FXCollections.observableArrayList();
        addColumns();
        setTable();
    }

    private void addColumns() {
        TableColumn<WorkOrderTableDTO, String> jobNameCol = new TableColumn<>("Munka megnevezése");
        jobNameCol.setPrefWidth(150);
        jobNameCol.setCellValueFactory(cellData -> cellData.getValue().jobNameProperty());
        jobNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<WorkOrderTableDTO, String> partNameCol = new TableColumn<>("Alkatrész (cikkszám)");
        partNameCol.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        partNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        partNameCol.setPrefWidth(150);

        final TableColumn<WorkOrderTableDTO, String> quantityCol = new TableColumn<>("Mennyiség");
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn());

        final TableColumn<WorkOrderTableDTO, String> unitCol = new TableColumn<>("Egység");
        unitCol.setCellValueFactory(cellData -> cellData.getValue().unitProperty());

        final TableColumn<WorkOrderTableDTO, String> unitPriceCol = new TableColumn<>("Érték");
        unitPriceCol.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());
        unitPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());

        this.table.getColumns().addAll(jobNameCol, partNameCol, quantityCol, unitCol, unitPriceCol);
    }

    private void setTable() {
        this.table.setItems(this.data);
    }

    public void addTableRow(WorkOrderTableDTO dto) {
        table.getItems().add(dto);
    }

    public void removeAllData() {
        this.data.clear();
    }

    /**
     * Retrieves data from the TableView and formats it as a string.
     * Each field is separated by a specified field delimiter, and each
     * row is separated by a row delimiter.
     *
     * @return a string representation of the table data
     */
    public String retrieveDataFromTable() {
        StringBuilder builder = new StringBuilder();
        for (WorkOrderTableDTO dto : table.getItems()) {
            builder.append(dto.getJobName()).append(fieldDelimiter)
                   .append(dto.getPartName()).append(fieldDelimiter)
                   .append(dto.getQuantity()).append(fieldDelimiter)
                   .append(dto.getUnit()).append(fieldDelimiter)
                   .append(dto.getUnitPrice()).append(fieldDelimiter)
                   .append(rowDelimiter);
        }
        return builder.toString();
    }

    /**
     * Creates WorkOrderTableDTO objects from the provided string data.
     * The string is split into rows and fields, and each field is used
     * to create a new WorkOrderTableDTO. The data is then set in the table.
     *
     * @param data the string representation of the table data
     */
    public void createDtoFromData(String data) {
        this.data.clear();
        if (!data.isEmpty()) {
            String[] rows = data.split(rowDelimiter);
            for (String row : rows) {
                String[] fields = row.split(fieldDelimiter);
                if (fields.length != 5) {
                    PopupWindowsController.showDialog("Nem sikerült menteni a munkalapot",
                        PopupWindowsController.DialogType.ALERT,
                        new Exception("Hiba a táblázat soraiban"));
                }
                WorkOrderTableDTO dto = new WorkOrderTableDTO(fields[0], fields[1], fields[2],
                        fields[3], fields[4]);
                this.data.add(dto);
            }
        }
        setTable();
    }
}
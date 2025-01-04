package com.simpleworkshopsoftware.controller.tableControllers;

import com.simpleworkshopsoftware.dto.CustomerCarDTO;
import com.simpleworkshopsoftware.mapper.DTOMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
/**
 * The CustomerCarTableController class manages the display of customer car
 * information in a TableView. It is responsible for adding columns to the table,
 *  * populating the table with data, and clearing the table data.
 *  It provides functionality for searching and
 * filtering the displayed data based on user input in a search field.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CustomerCarTableController {
    private final TableView<CustomerCarDTO> table;
    private ObservableList<CustomerCarDTO> data;
    private final TextField searchTf;

    public CustomerCarTableController(TableView<CustomerCarDTO> table, TextField searchTf) {
        this.table = table;
        this.data = FXCollections.observableArrayList();
        this.searchTf = searchTf;
        addColumns();
    }

    /**
     * Sets up the search bar functionality. It filters the displayed DTO
     * based on the user's input in the search TextField. The filtering
     * is done on last name, first name, car brand, and license plate.
     */
    public void setSearchBar() {
        FilteredList<CustomerCarDTO> filteredData = new FilteredList<>(data, p -> true);
        searchTf.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customerCarDTO -> {
                if (newValue.isBlank()) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (customerCarDTO.getLastName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (customerCarDTO.getFirstName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (customerCarDTO.getBrand().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return customerCarDTO.getLicensePlate().toLowerCase().contains(searchKeyword);
                }
            });
        });
        SortedList<CustomerCarDTO> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(this.table.comparatorProperty());
        this.table.setItems(sortedData);
    }

    private void addColumns() {
        final TableColumn<CustomerCarDTO, String> lastNameCol = new TableColumn<>("Vezetéknév | Cégnév");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        final TableColumn<CustomerCarDTO, String> firstNameCol = new TableColumn<>("Keresztnév");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        final TableColumn<CustomerCarDTO, String> carCol = new TableColumn<>("Autó");
        carCol.setCellValueFactory(new PropertyValueFactory<>("brand"));

        final TableColumn<CustomerCarDTO, String> licenseCol = new TableColumn<>("Rendszám");
        licenseCol.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));

        this.table.getColumns().addAll(lastNameCol, firstNameCol, carCol, licenseCol);
    }

    public void populateTable(List<CustomerCarDTO> list) {
        this.data = DTOMapper.toObservableList(list);
        this.table.setItems(this.data);
    }
}

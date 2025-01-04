package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;
/**
 * The CenterViewLoader class is responsible for loading different panes
 * (views) to the main BorderPanes center pane. It initializes the default view, customer
 * view, work order view, and settings view by loading their respective
 * FXML files.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CenterViewLoader {
    private final Pane defaultPane;
    private final Pane customerPane;
    private final Pane settingsPane;
    private final Pane workOrderPane;

    public CenterViewLoader() throws IOException {
        this.defaultPane = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("defaultView.fxml")));
        this.customerPane = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("customerView.fxml")));
        this.workOrderPane = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("workOrderView.fxml")));
        this.settingsPane = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("settingsView.fxml")));
    }

    public Pane getDefaultPane() {
        return defaultPane;
    }

    public Pane getCustomerPane() {
        return customerPane;
    }

    public Pane getSettingsPane() {
        return settingsPane;
    }

    public Pane getWorkOrderPane() {
        return workOrderPane;
    }
}
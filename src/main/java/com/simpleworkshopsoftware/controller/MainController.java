
package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.utils.PieChartUtils;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**
 * This class is responsible for the interactions of the BorderPanes fixed UI components.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class MainController implements Initializable {

    @FXML
    private Label carDisplayLabel,customerDisplayLabel , workOrderDisplayLabel;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button customerButton, mainButton,
            unselectButton , settingsButton,
            workOrderButton;

    private CenterViewLoader centerView;

    @FXML
    private PieChart pieChart;

    public String getCustomerDisplayLabel() {
        return customerDisplayLabel.getText();
    }

    public void setCustomerDisplayLabel(String text) {
        customerDisplayLabel.setText(text);
    }

    public void setCarDisplayLabel(String text) {
        carDisplayLabel.setText(text);
    }

    public String getCarDisplayLabelText() {
        return carDisplayLabel.getText();
    }

    ControllerConnector connector = ControllerConnector.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            PieChartUtils.setPieChart(pieChart);
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Nem sikerült betölteni a diagramot",
                    PopupWindowsController.DialogType.WARNING);
        }
        // sets the labels
        connector.carDisplayLabelTextProperty()
                .addListener((observable, oldValue, newValue) -> {
                    carDisplayLabel.setText(newValue);
                });
        carDisplayLabel.setText(connector.getCarDisplayLabelText());
        connector.customerDisplayLabelTextProperty().addListener((observable, oldValue, newValue) -> {
            customerDisplayLabel.setText(newValue);
        });
        customerDisplayLabel.setText(connector.getCustomerDisplayLabelText());
        connector.workOrderDisplayLabelTextProperty()
                .addListener((observable, oldValue, newValue) -> {
                    workOrderDisplayLabel.setText(newValue);
                });
        workOrderDisplayLabel.setText(connector.getWorkOrderDisplayLabelText());
        initializeButtonEffects();
        try {
            centerView = new CenterViewLoader();
            rootPane.setCenter(centerView.getDefaultPane());
        } catch (IOException e) {
            PopupWindowsController.showDialog(
                    "Nem sikerült beállítani a képernyőt, az fxml fájl sérült, vagy nem található!",
                    PopupWindowsController.DialogType.ALERT, e);;
        }
    }

    private void initializeButtonEffects() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setHeight(35.0);
        dropShadow.setWidth(35.0);
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        customerButton.setOnMouseEntered(e -> customerButton.setEffect(dropShadow));
        customerButton.setOnMouseExited(e -> customerButton.setEffect(null));
        mainButton.setOnMouseEntered(e -> mainButton.setEffect(dropShadow));
        mainButton.setOnMouseExited(e -> mainButton.setEffect(null));
        settingsButton.setOnMouseEntered(e -> settingsButton.setEffect(dropShadow));
        settingsButton.setOnMouseExited(e -> settingsButton.setEffect(null));
        workOrderButton.setOnMouseEntered(e -> workOrderButton.setEffect(dropShadow));
        workOrderButton.setOnMouseExited(e -> workOrderButton.setEffect(null));
    }

    private void setTransitionEffect(Pane centerPane) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), centerPane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private boolean shouldChange(Pane centerPane) {
        return !centerPane.equals(rootPane.getCenter());
    }

    @FXML
    protected void onMainButtonClick(ActionEvent event) {
        Pane centerPane = centerView.getDefaultPane();
        if (shouldChange(centerPane)) {
            rootPane.setCenter(centerPane);
            setTransitionEffect(centerPane);
        }
        try {
            PieChartUtils.refreshPieChart(pieChart);
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Nem sikerült frissíteni a diagramot",
                    PopupWindowsController.DialogType.WARNING);
        }
    }

    @FXML
    protected void onCustomerButtonClick(ActionEvent event) {
        Pane centerPane = centerView.getCustomerPane();
        if (shouldChange(centerPane)) {
            rootPane.setCenter(centerPane);
            setTransitionEffect(centerPane);
        }
    }

    @FXML
    protected void onWorkOrderButtonClick(ActionEvent event) {
        Pane centerPane = centerView.getWorkOrderPane();
        if (shouldChange(centerPane)) {
            rootPane.setCenter(centerPane);
            setTransitionEffect(centerPane);
        }
    }

    @FXML
    protected void onSettingsButtonClick(ActionEvent event) {
        Pane centerPane = centerView.getSettingsPane();
        if (shouldChange(centerPane)) {
            rootPane.setCenter(centerPane);
            setTransitionEffect(centerPane);
        }
    }

    @FXML
    void onUnselectButtonClick(ActionEvent event) {
        connector.setCustCarDto(null);
        connector.setWorkOrderDto(null);
    }
}
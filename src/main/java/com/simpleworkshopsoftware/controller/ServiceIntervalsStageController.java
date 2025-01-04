package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.base.Database;
import com.simpleworkshopsoftware.controller.PopupWindowsController.DialogType;
import com.simpleworkshopsoftware.entities.ServicePeriod;
import com.simpleworkshopsoftware.utils.FieldUtils;
import com.simpleworkshopsoftware.validators.InputValidator;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Controller for managing service intervals in the application through popup windows.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class ServiceIntervalsStageController {

    private final Integer carId;
    private final Integer oldKm;
    private final Button SAVE_BUTTON = new Button("Mentés");
    private final Label[] LABELS = new Label[]{
            new Label("Olaj és szűrő"), new Label("Levegő szűrő"),
            new Label("Utastér szűrő"), new Label("Üzemanyag szűrő"),
            new Label("Váltó olaj"), new Label("Hajtómű olaj"),
            new Label("Fék folyadék"), new Label("Vezérlés"),
            new Label("Hosszbordás szíj"), new Label("Hűtőfolyadék")};

    public ServiceIntervalsStageController() {
        ControllerConnector CONNECTOR = ControllerConnector.getInstance();
        this.carId = CONNECTOR.getCustCarDto().getCar().getId();
        this.oldKm = CONNECTOR.getCustCarDto().getCar().getKilometers();
    }
    /**
     * Displays the service periods popup stage for creating new service periods for the selected car.
     *
     * @throws SQLException if a database error occurs.
     */
        public void ServicePeriodsPopupStage() throws SQLException {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Szerviz csere periódusok");

            String postFix = " periódus km / hó";

            TextField[] textFields = new TextField[LABELS.length * 2];
            for (int i = 0; i < LABELS.length; i++) {
                textFields[i * 2] = new TextField();
                textFields[i * 2].setId(LABELS[i].getText());
                textFields[i * 2 + 1] = new TextField();
            }
            //  autofill the textfields if the car already has periods
            List<ServicePeriod> existingPeriods = Database.getCarServiceRepository()
                    .findAll(ControllerConnector
                            .getInstance()
                            .getSelectedCarId());
            if (!existingPeriods.isEmpty()) {
                for (int i = 0; i < existingPeriods.size(); i++) {
                        textFields[i * 2].setText(String.valueOf(existingPeriods.get(i).getKilometers()));
                        textFields[i * 2 + 1].setText(String.valueOf(existingPeriods.get(i).getMonths()));
                }
            }

            GridPane gridPane = setGridPane();

            ColumnConstraints labelColumn = new ColumnConstraints();
            labelColumn.setPercentWidth(60);

            ColumnConstraints kilometersColumn = new ColumnConstraints();
            kilometersColumn.setPercentWidth(25);

            ColumnConstraints monthsColumn = new ColumnConstraints();
            monthsColumn.setPercentWidth(15);

            gridPane.getColumnConstraints().addAll(
                    labelColumn,
                    kilometersColumn,
                    monthsColumn
            );

            for (int i = 0; i < LABELS.length; i++) {
                Label columnText = new Label(LABELS[i].getText() + postFix);
                gridPane.add(columnText, 0, i);
                gridPane.add(textFields[i * 2], 1, i);
                gridPane.add(textFields[i * 2 + 1], 2, i);
            }
            gridPane.add(SAVE_BUTTON, 0, LABELS.length, 3, 1);
            Button updateButton = new Button("Módosítás");
            gridPane.add(updateButton, 1, LABELS.length, 3, 1);

            SAVE_BUTTON.setOnAction(event -> {
                List<ServicePeriod> servicePeriods = getServicePeriodsWhenServiceNotDone(textFields);
                if (servicePeriods == null) return;
                try {
                    Database.getCarServiceRepository().insert(servicePeriods, carId);
                } catch (SQLException e) {
                    PopupWindowsController.showDialog("Ennek az autónak már vannak beállított periódusai, kérlek kattints a módosítás gombra," +
                            " ha módosítani szeretnéd", PopupWindowsController.DialogType.WARNING);
                    return;
                }
                popupStage.close();
                PopupWindowsController.showDialog("Sikeres mentés", DialogType.INFORMATION);
            });

            updateButton.setOnAction(event -> {
                List<ServicePeriod> servicePeriods = getServicePeriodsWhenServiceNotDone(textFields);
                if (servicePeriods == null) return;
                try {
                        Database.getCarServiceRepository().updateServicePeriods(servicePeriods, carId);
                    } catch (SQLException e) {
                        PopupWindowsController.showDialog("A mentés nem sikerült", DialogType.ALERT, e);
                        return;
                    }
                popupStage.close();
                PopupWindowsController.showDialog("Sikeres mentés", DialogType.INFORMATION);
            });

            Scene scene = new Scene(gridPane, 460, 430);
            popupStage.setScene(scene);
            popupStage.show();
        }
    /**
     * Displays the service done popup stage for updating the service periods.
     *
     * @param id the car ID.
     * @param oldKm the old kilometers of the car.
     * @param connector the {@link ControllerConnector} instance for accessing car data.
     */
    public void ServiceDonePopupStage(int id, int oldKm, ControllerConnector connector) {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Csere periódusok");

            CheckBox[] checkBoxes = new CheckBox[LABELS.length];
            for (int i = 0; i < LABELS.length; i++) {
                checkBoxes[i] = new CheckBox();
                checkBoxes[i].setId(LABELS[i].getText());
            }

        GridPane gridPane = setGridPane();

        ColumnConstraints labelColumn = new ColumnConstraints();
            labelColumn.setPercentWidth(50);
            labelColumn.setHalignment(HPos.CENTER);

            ColumnConstraints checkBoxesColumn = new ColumnConstraints();
            checkBoxesColumn.setPercentWidth(50);
            checkBoxesColumn.setHalignment(HPos.CENTER);

            gridPane.getColumnConstraints().addAll(
                    labelColumn,
                    checkBoxesColumn
            );

            for (int i = 0; i < LABELS.length; i++) {
                gridPane.add(LABELS[i], 0, i);
                gridPane.add(checkBoxes[i], 1, i);
            }

            Label kmTfLabel = new Label("Km óra állása");
            TextField kmTf = new TextField();
            gridPane.add(kmTfLabel, 0, LABELS.length, 3, 1);
            gridPane.add(kmTf, 0, LABELS.length + 1,3,1);
            gridPane.add(SAVE_BUTTON, 1, LABELS.length + 2, 3, 1);
            GridPane.setHalignment(kmTfLabel, HPos.CENTER);
            GridPane.setHalignment(kmTf, HPos.CENTER);
            GridPane.setHalignment(SAVE_BUTTON, HPos.CENTER);;

            SAVE_BUTTON.setOnAction(event -> {
                List<ServicePeriod> servicePeriods = getServicePeriods(kmTf, checkBoxes);
                if (servicePeriods == null) return;
                FieldUtils.setKm(kmTf, id, oldKm, connector);
                try {
                        Database.getCarServiceRepository().updateLastService(servicePeriods);
                    } catch (SQLException e) {
                        PopupWindowsController.showDialog("Nem sikerült a mentés", DialogType.ALERT, e);
                    }
                    popupStage.close();
                    PopupWindowsController.showDialog("Sikeres mentés", DialogType.INFORMATION);
            });

            Scene scene = new Scene(gridPane, 300, 420);
            popupStage.setScene(scene);
            popupStage.show();
        }

    private GridPane setGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.getStylesheets().add("style.css");
        gridPane.getStyleClass().add("grid-pane");
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        return gridPane;
    }
    /**
     * Retrieves service periods when the service is not done based on user input.
     *
     * @param textFields an array of text fields containing input values.
     * @return a list of {@link ServicePeriod} instances, or null if validation fails.
     */
    private List<ServicePeriod> getServicePeriodsWhenServiceNotDone(TextField[] textFields) {
        InputValidator validator = new InputValidator();
        List<ServicePeriod> servicePeriods = new ArrayList<>();
        for (TextField tf : textFields) {
            validator.addValidation(tf, val -> val.matches("\\d+"));
        }
        if (!validator.validate()) {
            return null;
        }
        for (int i = 0; i < textFields.length; i += 2) {
            try {
                int kilometers = Integer.parseInt(textFields[i].getText());
                int months = Integer.parseInt(textFields[i + 1].getText());
                servicePeriods.add(ServicePeriod.createWhenServiceNotDone
                        (carId, textFields[i].getId(), kilometers, months, oldKm));
            } catch (Exception e) {
                PopupWindowsController.showDialog("Nem megfelelő szám formátum", DialogType.WARNING);
            }
        }
        return servicePeriods;
    }
    /**
     * Retrieves service periods based on user input and selected checkboxes.
     *
     * @param kmTf the text field containing the kilometer reading.
     * @param checkBoxes an array of checkboxes representing service tasks.
     * @return a list of {@link ServicePeriod} instances, or null if validation fails.
     */
    private List<ServicePeriod> getServicePeriods(TextField kmTf, CheckBox[] checkBoxes) {
        InputValidator validator = new InputValidator();
        validator.addValidation(kmTf, val -> val.matches("\\d+"));
        if (!validator.validate()) {
            return null;
        }
        List<ServicePeriod> servicePeriods = new ArrayList<>();
        String km = FieldUtils.checkKm(kmTf, oldKm);
        if (km == null) return null;
        for (CheckBox checkBox : checkBoxes) {
            try {
                boolean check = checkBox.isSelected();
                if (check) {
                    servicePeriods.add(ServicePeriod.createWhenServiceDone
                            (carId, checkBox.getId(), Integer.parseInt(km), LocalDate.now()));
                }
            } catch (Exception e) {
                PopupWindowsController.showDialog("Nem megfelelő szám formátum", DialogType.WARNING);
            }
        }
        if (servicePeriods.isEmpty()) {
            PopupWindowsController.showDialog
                    ("Kérlek jelöld be az elvégzett szervizekhez tartozó ablakot", DialogType.WARNING);
            return null;
        }
        return servicePeriods;
    }
}

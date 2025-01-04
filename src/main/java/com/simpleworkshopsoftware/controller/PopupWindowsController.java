package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.utils.FieldUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Controller for managing popup windows in the application such as new km entry,
 * delete confirmation window, and dialog windows.
 * Provides methods for showing dialogs, handling confirmation windows, and logging exceptions.
 *
 *  @author Attila Eckert
 *  @date 12/27/2024
 *  @version 1.0
 */
public class PopupWindowsController {

    public static boolean deleteConfirmation(String message) {
        BooleanProperty result = new SimpleBooleanProperty(false);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Kérlek válassz");
        Label warningLabel = new Label(message);

        Button yesButton = new Button("Igen");
        yesButton.setOnAction(event -> {
            result.set(true);
            popupStage.close();
        });
        Button noButton = new Button("Nem");
        noButton.setOnAction(event -> popupStage.close());

        HBox buttonLayout = new HBox(10, yesButton, noButton);
        buttonLayout.setSpacing(20);
        buttonLayout.setAlignment(Pos.CENTER);
        VBox popupLayout = new VBox(20, warningLabel, buttonLayout);
        popupLayout.setSpacing(10);
        popupLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Scene popupScene = new Scene(popupLayout, 350, 200);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
        return result.get();
    }

    public static void newKmWindow(ControllerConnector connector) {
        int oldKm = connector.getCustCarDto().getCar().getKilometers();
        int id = connector.getCustCarDto().getCar().getId();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Új km felvétele");
        Label label = new Label("Km számláló új értéke:");
        TextField kmTextField = new TextField();
        Button saveButton = new Button("Ment");
        VBox vbox = new VBox(10, label, kmTextField, saveButton);
        kmTextField.setMaxWidth(100);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox);
        popupStage.setScene(scene);
        saveButton.setOnAction(e -> {
            if (!FieldUtils.setKm(kmTextField, id, oldKm, connector)) {
               return;
            }
            popupStage.close();
        });
        popupStage.showAndWait();
    }

    public enum DialogType {
        ALERT, WARNING, INFORMATION
    }

    public static void showDialog(String message, DialogType type, Exception e) {

        if (e != null) {
            logException(e);
        }
        switch (type) {
            case ALERT -> createDialog(message, Alert.AlertType.ERROR, "Hiba");
            case WARNING -> createDialog(message, Alert.AlertType.WARNING, "Hiba");
            case INFORMATION -> createDialog(message, Alert.AlertType.INFORMATION, "Sikeres mentés");
        }
    }

    public static void showDialog(String message, DialogType type) {

        switch (type) {
            case ALERT -> createDialog(message, Alert.AlertType.ERROR, "Hiba");
            case WARNING -> createDialog(message, Alert.AlertType.WARNING, "Hiba");
            case INFORMATION -> createDialog(message, Alert.AlertType.INFORMATION, "Sikeres mentés");
        }
    }

    private static void createDialog(String message, Alert.AlertType alertType, String title) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setHeight(200);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Logs an exception to a daily log file.
     *
     * @param exception the exception to be logged.
     */
    private static void logException(Exception exception) {
        try {
            Path logDir = Paths.get("log");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            String logFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
            Path logFilePath = logDir.resolve(logFileName);

            try (FileWriter fileWriter = new FileWriter(logFilePath.toFile(), true);
                 PrintWriter printWriter = new PrintWriter(fileWriter)) {

                printWriter.println("Timestamp: " + LocalDateTime.now());
                printWriter.println("Exception: " + exception.getClass().getName());
                printWriter.println("Message: " + exception.getMessage());
                printWriter.println("Stack Trace:");
                exception.printStackTrace(printWriter);
                printWriter.println("----------------------------------------------------");
            }

        } catch (IOException e) {
            createDialog("Váratlan hiba történt, és nem sikerült fájlba menteni", Alert.AlertType.ERROR, "Hiba");
        }
    }
}

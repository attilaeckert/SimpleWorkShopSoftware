package com.simpleworkshopsoftware.controller;

import com.simpleworkshopsoftware.configuration.ConfigManager;
import com.simpleworkshopsoftware.utils.FieldUtils;
import com.simpleworkshopsoftware.validators.SettingsInputValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import org.apache.commons.configuration2.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
/**
 * This class is responsible for the interactions of the settings UI.
 * Provides functionality for configuring workshop details,
 * set labor to calculate with, saving paths to the database
 * and Pdf files, add a company logo image, and resetting settings.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class SettingsController implements Initializable {

    @FXML
    private TextFlow companyDetailsTextFlow;

    @FXML
    private ImageView imageView;

    @FXML
    private Button ChooseImageButton, setButton,
            textAreaContentButton, pdfSaveDirectoryButton,
            databaseSaveDirectoryButton, deleteSettingsButton;

    @FXML
    private Label pdfSavePathLabel, databaseSavePathLabel;

    @FXML
    private AnchorPane settingsPane;

    @FXML
    private TextField setCompanyAddressTf, setCompanyEmailTf,
            setCompanyNameTf, setCompanyPhoneTf, setCompanyTaxnumTf,
            setLaborCostTf, setCompanyBankAccountTf, workShopCapacityTf;

    @FXML
    private TextArea textForWorkOrders;

    private String actualCompanyName;
    private String actualCompanyDetails;
    private String actualLabor;
    private String workShopCapacity;
    private String actualPdfFooterNote;
    private String pdfSavePath;
    private String databaseSavePath;
    private ConfigManager configManager;
    private Configuration config;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configManager = ConfigManager.getInstance();
        config = ConfigManager.getInstance().getConfig();
        initializeConfigFields(config);
        setSavePathLabels();
        setTextFlow();
        setTextArea();
        File imageFile = new File("config/logo.png");
        loadImage(imageFile.toURI().toString());
    }

    private void setSavePathLabels() {
        databaseSavePathLabel.setText(databaseSavePath);
        pdfSavePathLabel.setText(pdfSavePath);
    }

    private void setTextArea() {
        textForWorkOrders.setText(actualPdfFooterNote);
    }
    /**
     * Populates the company details text flow with formatted text.
     */
    private void setTextFlow() {
        Text text = new Text(actualCompanyName + "\n"
                + actualCompanyDetails + "\n Óradíj: "
                + actualLabor + "Ft" + "\n Szerviz kapacitás: "
                + workShopCapacity + " hely");
        text.setFill(Color.WHITE);
        companyDetailsTextFlow.setTextAlignment(TextAlignment.CENTER);
        companyDetailsTextFlow.getChildren().add(text);
    }

    private void initializeConfigFields(Configuration config) {
        this.actualCompanyName = config.getString("workshop.name");
        this.actualCompanyDetails = config.getString("workshop.details");
        this.actualLabor = config.getString("workshop.laborcost");
        this.workShopCapacity = config.getString("workshop.capacity");
        this.actualPdfFooterNote = config.getString("pdf.footernote");
        this.pdfSavePath = config.getString("pdf.savepath");
        this.databaseSavePath = config.getString("database.savepath");
    }
    /**
     * Updates workshop settings in the config file, and re-initializes
     * the properties file, finally clears the fields.
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void onSetButtonClicked(ActionEvent event) {
        TextField[] fields = new TextField[]{
                setCompanyNameTf, setCompanyAddressTf, setCompanyPhoneTf,
                setCompanyEmailTf, setCompanyTaxnumTf, setCompanyBankAccountTf,
        setLaborCostTf, workShopCapacityTf};
        SettingsInputValidator validator = new SettingsInputValidator();
        if (validator.validateSettingsFields(fields)) {
            String newCompanyName = setCompanyNameTf.getText();
            String newCompanyDetails =
                            setCompanyAddressTf.getText() +
                            "\n" +
                            setCompanyPhoneTf.getText() +
                            "\n" +
                            setCompanyEmailTf.getText().toLowerCase() +
                            "\n" +
                            setCompanyTaxnumTf.getText() +
                            "\n" +
                            setCompanyBankAccountTf.getText() +
                            "\n" +
                            workShopCapacityTf.getText();
            String newLabor = setLaborCostTf.getText();
            String newCapacity = workShopCapacityTf.getText();
            config.setProperty("workshop.name", newCompanyName);
            config.setProperty("workshop.details", newCompanyDetails);
            config.setProperty("workshop.laborcost", newLabor);
            config.setProperty("workshop.capacity", newCapacity);
            configManager.saveConfig();
            config = ConfigManager.getInstance().getConfig();
            initializeConfigFields(config);
            companyDetailsTextFlow.getChildren().clear();
            setTextFlow();
            FieldUtils.clearFields(fields);
        }
    }

    @FXML
    void onDatabaseSaveDirectoryButtonClicked(ActionEvent Event) {
        configManager.chooseDirectory("Melyik könyvtárba legyenek mentve a táblázat adatai?", "database.savepath");
        initializeConfigFields(config);
        setSavePathLabels();
    }

    @FXML
    void onDeleteSettingsButtonClicked(ActionEvent Event) {
        String configFilePath = "config/app.properties";
        String imageFilePath = "config/logo.png";
        File configFile = new File(configFilePath);
        File imageFile = new File(imageFilePath);
        try {
                boolean shouldDelete = PopupWindowsController.deleteConfirmation("Biztosan törölni szeretnéd a beállításokat?");
                if (shouldDelete) {
                    Files.delete(configFile.toPath());
                    if (imageFile.exists()) {
                        Files.delete(imageFile.toPath());
                    }
                    PopupWindowsController.showDialog("A beállítások sikeresen vissza lettek állítva, kérlek indítsd újra az alkalmazást!",
                            PopupWindowsController.DialogType.INFORMATION);
                }
        } catch (IOException e) {
            PopupWindowsController.showDialog("Nem sikerült visszaállítani a beállításokat", PopupWindowsController.DialogType.WARNING);
        }
    }

    @FXML
    void onPdfSaveDirectoryButtonClicked(ActionEvent Event) {
        configManager.chooseDirectory("Melyik könyvtárba legyenek mentve a Pdf munkalapok?", "pdf.savepath");
            initializeConfigFields(config);
            setSavePathLabels();
    }

    @FXML
    void onTextAreaContentButtonClicked(ActionEvent event) {
        config.setProperty("pdf.footernote", textForWorkOrders.getText());
        configManager.saveConfig();
        config = ConfigManager.getInstance().getConfig();
        initializeConfigFields(config);
    }

    @FXML
    void onChooseImageButtonClick(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Válaszd ki a képet");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files",
                            "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            File selectedFile = fileChooser.showOpenDialog(settingsPane.getScene().getWindow());
            if (selectedFile != null) {
                String imagePath = selectedFile.toURI().toString();
                try {
                    loadImage(imagePath);
                    saveImage(selectedFile);
                } catch (IOException e) {
                    PopupWindowsController.showDialog("Nem sikerült menteni a képet",
                            PopupWindowsController.DialogType.WARNING, e);
                }
            }
    }

    private void saveImage(File selectedFile) throws IOException {
        String imagePath = "config";
        File destinationFile = new File(imagePath, "logo.png");
        Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void loadImage(String imagePath) {
        Image image = new Image(imagePath);
        imageView.setImage(image);
    }
}


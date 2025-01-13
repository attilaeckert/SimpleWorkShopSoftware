package com.simpleworkshopsoftware.configuration;

import com.simpleworkshopsoftware.controller.PopupWindowsController;
import com.simpleworkshopsoftware.controller.PopupWindowsController.DialogType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
/**
 * The ConfigManager class is a singleton responsible for managing the application's
 * configuration settings stored in a properties file. It provides methods to load,
 * create, save, and retrieve configuration properties, as well as to choose
 * directories for file saving.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class ConfigManager {
    private static ConfigManager instance;
    private Configuration config;
    private final String configFilePath = "config/app.properties";
    private String savePath;
    private String workShopName;
    private String workShopDetails;
    private String footerNote;

    private ConfigManager() {
        loadConfiguration();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    /**
     * Loads the configuration from the properties file. If the file does not exist,
     * it creates a default configuration and shows a warning dialog to the user.
     */
    public void loadConfiguration() {
        Configurations configurations = new Configurations();
        File configFile = new File(configFilePath);
        try {
            if (!configFile.exists()) {
                Files.createDirectories(configFile.getParentFile().toPath());
                createDefaultConfig(configFile);
                PopupWindowsController.showDialog(
                        "A konfigurációs fájl nem található, kérlek add meg a beállításokban a kívánt paramétereket",
                        DialogType.WARNING);
            }
            this.config = configurations.properties(new File(configFilePath));
            this.savePath = config.getString("pdf.savepath");
            this.workShopName = config.getString("workshop.name");
            this.workShopDetails = config.getString("workshop.details");
            this.footerNote = config.getString("pdf.footernote");
        } catch (IOException | ConfigurationException e) {
            PopupWindowsController.showDialog("Hiba történt a konfigurációs fájl létrehozásában",
                    DialogType.ALERT, e);
        }
    }
    /**
     * Creates a default configuration file with predefined settings.
     *
     * @param configFile the configuration file to create
     * @throws ConfigurationException if there is an error with the configuration
     * @throws IOException if there is an error writing to the file
     */
    public void createDefaultConfig(File configFile) throws ConfigurationException, IOException {
        final String defaultCompanyName = "Autoszerviz Kft";
        final String defaultCompanyDetails =  """
            7632 Pécs Teszt utca 63.
            +36301234567
            auto@szerviz.hu
            Adószám: 11111111-1-11
            Bankszámlaszám: 12345678-87654321-00000000
            """;
        final String defaultLabor = "15000";
        final String defaultCapacity = "15";
        final String emptySavePath = "";
        final String defaultPdfFooterNote =
                "A megrendelt és vállalt szolgáltatás feltételeit a vállalási szabályzat tartalmazza.\n" +
                "Az elvégzett munkák az ágazati szabványoknak " +
                "és rendeleteknek megfelelnek, amiért garanciát vállalunk. A garancia időtartama 6 hónap.";
        PropertiesConfiguration defaultConfig = new PropertiesConfiguration();
        defaultConfig.setProperty("workshop.name", defaultCompanyName);
        defaultConfig.setProperty("workshop.details", defaultCompanyDetails.trim());
        defaultConfig.setProperty("workshop.laborcost", defaultLabor);
        defaultConfig.setProperty("workshop.capacity", defaultCapacity);
        defaultConfig.setProperty("pdf.footernote", defaultPdfFooterNote);
        defaultConfig.setProperty("pdf.savepath", emptySavePath);
        defaultConfig.setProperty("database.savepath", emptySavePath);
        Writer writer = new FileWriter(configFile);
        defaultConfig.write(writer);
    }

    public Configuration getConfig() {
        return config;
    }

    public void saveConfig() {
        try (Writer writer = new FileWriter(configFilePath)) {
            if (config instanceof PropertiesConfiguration) {
                ((PropertiesConfiguration) config).write(writer);
                PopupWindowsController.showDialog("Sikeres mentés a konfigurációs fájlba, kérlek indítsd újra az alkalmazást!",
                        DialogType.INFORMATION);
            }
        } catch (ConfigurationException | IOException e) {
            PopupWindowsController.showDialog("Nem sikerült a konfigurációs fájl mentése",
                    DialogType.WARNING);
        }
    }

    public String getSavePath() {
        return savePath;
    }

    public String getWorkShopName() {
        return workShopName;
    }

    public String getWorkShopDetails() {
        return workShopDetails;
    }

    public String getFooterNote() {
        return footerNote;
    }

    public void chooseDirectory(String title, String property) {
        String directoryPath;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            directoryPath = selectedDirectory.getAbsolutePath();
            config.setProperty(property, directoryPath);
            saveConfig();
        }
    }
}

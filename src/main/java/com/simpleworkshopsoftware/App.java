package com.simpleworkshopsoftware;

import com.simpleworkshopsoftware.base.Database;
import com.simpleworkshopsoftware.configuration.ConfigManager;
import com.simpleworkshopsoftware.controller.PopupWindowsController;
import com.simpleworkshopsoftware.controller.PopupWindowsController.DialogType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.configuration2.Configuration;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
/**
 * App class of the JavaFX application that connects to an H2 embedded database.
 * It initializes the configuration for database connection, displays a
 * directory chooser if the database, and pdf save path is not set in the config file,
 * and handles exceptions related to database connectivity and FXML loading.
 * The application starts by loading the main view fxml file.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class App extends Application {

    private Connection conn = null;
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final Configuration config = ConfigManager.getInstance().getConfig();
    private String databasePath = "";
    private String pdfSavePath = "";

    @Override
    public void start(Stage stage) throws SQLException {
        initializePathsFromConfig(config);
        if (databasePath.isBlank()) {
            configManager.chooseDirectory("Melyik könyvtárba legyenek mentve a táblázat adatai?",
                    "database.savepath");
        }
        if (pdfSavePath.isBlank()) {
            configManager.chooseDirectory("Melyik könyvtárba legyenek mentve a Pdf munkalapok?",
                    "pdf.savepath");
        }
        initializePathsFromConfig(config);
        try {
            String urlPrefix = "jdbc:h2:file:";
            String user = "sa";
            String password = "";
            conn = DriverManager.getConnection(urlPrefix + databasePath, user, password);
            Database.setConnection(conn);
            Database.getInstance();
            Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("mainView.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            URL resource = getClass().getResource("/images/App-logo.png");
            if (resource != null) {
                Image logo = new Image(resource.toExternalForm());
                stage.getIcons().add(logo);
            }
            String appName = "Simple Workshop Software";
            stage.setTitle(appName);
            stage.show();
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Adatbázis kommunikációs hiba, nem tudok csatlakozni az adatbázishoz",
                    DialogType.ALERT, e) ;
            System.exit(0);
        } catch (IOException e) {
            conn.close();
            PopupWindowsController.showDialog("Hiba a betöltéskor, nem találom az fxml fájlt.",
                    DialogType.ALERT, e) ;
            System.exit(0);
        }
    }

    private void initializePathsFromConfig(Configuration config) {
        this.databasePath = config.getString("database.savepath");
        this.pdfSavePath = config.getString("pdf.savepath");
    }

    @Override
    public void stop() throws Exception {
        conn.close();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
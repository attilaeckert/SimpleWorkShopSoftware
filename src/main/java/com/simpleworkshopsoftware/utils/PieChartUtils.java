package com.simpleworkshopsoftware.utils;

import com.simpleworkshopsoftware.base.Database;
import com.simpleworkshopsoftware.configuration.ConfigManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.SQLException;
/**
 * The PieChartUtils class provides utility methods for managing and
 * updating pie chart related to the workshop's capacity and current load.
 * It includes methods for retrieving chart data and setting or refreshing the pie chart.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class PieChartUtils {
    private static PieChartUtils instance;
    private static final ConfigManager configManager = ConfigManager.getInstance();
    private static final int capacity = configManager.getConfig().getInt("workshop.capacity");

    public static ObservableList<PieChart.Data> getChartData() throws SQLException {
        int currentLoad = Database.getWorkOrderRepository().findByStatus(false);
        int remaining = capacity - currentLoad;
        return FXCollections.observableArrayList(
                new PieChart.Data("Teljes férőhelyek", remaining),
                new PieChart.Data("Autók a telephelyen", currentLoad)
        );
    }

    public static void setPieChart(PieChart pieChart) throws SQLException {
        ObservableList<PieChart.Data> pieData = getChartData();
        pieChart.setData(pieData);
        pieChart.getStylesheets().add("style.css");
    }

    public static void refreshPieChart(PieChart pieChart) throws SQLException {
        ObservableList<PieChart.Data> pieData = getChartData();
        pieChart.setData(pieData);
    }
}
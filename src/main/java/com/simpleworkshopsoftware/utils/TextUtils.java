package com.simpleworkshopsoftware.utils;

import com.simpleworkshopsoftware.controller.ControllerConnector;
import com.simpleworkshopsoftware.entities.ServicePeriod;
import com.simpleworkshopsoftware.entities.WorkOrder;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
/**
 * The TextUtils class provides utility methods for calculating service due dates,
 * clearing text area, and a text flow in the default view,
 * and formatting text for work orders in the application.
 * It includes methods to calculate the number of days until the next service,
 * display service periods, and fill a text area with formatted work order details.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class TextUtils {

    /**
     * Calculates the service due dates for the customer's car and updates the
     * TextFlow in the default view with the relevant information, including the remaining
     * days until the next road certificate inspection and any upcoming service periods.
     *
     * @param connector the ControllerConnector instance to access customer and car data
     * @param serviceDueTextFlow the TextFlow to display service due information
     */
    public static void calculateServiceDue(ControllerConnector connector, TextFlow serviceDueTextFlow) {
        clearServiceDueTextFlow(serviceDueTextFlow);
        Text roadCertificateText = new Text("Műszaki vizsgáig hátralevő napok: ");
        roadCertificateText.setStyle("-fx-font-weight: bold");
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), connector.getCustCarDto().getCar().getRoadCertificate());
        Text daysText = new Text(remainingDays + "\n");
        if (remainingDays < 0) {
            daysText.setStyle("-fx-fill: red;");
        } else {
            daysText.setStyle("-fx-fill: green;");
        }
        List<ServicePeriod> services = connector.getCustCarDto().getCar().getServicePeriods();
        if (services.isEmpty()) {
            Text emptyText = new Text("\n\nEhhez az autóhoz nincs beállított szerviz periódus.\n" +
                    "A szerviz periódusok gombra kattintva a felugró ablakban beállíthatod a szerviz periódusokat.");
            serviceDueTextFlow.setTextAlignment(TextAlignment.CENTER);
            serviceDueTextFlow.getChildren().addAll(roadCertificateText, daysText, emptyText);
            return;
        }
        serviceDueTextFlow.getChildren().addAll(roadCertificateText, daysText);
        serviceDueTextFlow.setTextAlignment(TextAlignment.LEFT);
        int newKm = connector.getCustCarDto().getCar().getKilometers();
        LocalDate now = LocalDate.now();
        services.sort((sp1, sp2) -> {
            int remainingKm1 = sp1.getKilometers() - (newKm - sp1.getLastServiceKilometers());
            int remainingKm2 = sp2.getKilometers() - (newKm - sp2.getLastServiceKilometers());
            if (remainingKm1 != remainingKm2) {
                return Integer.compare(remainingKm1, remainingKm2);
            }
            long remainingMonths1 = sp1.getMonths() - ChronoUnit.MONTHS.between(sp1.getLastServiceDate(), now);
            long remainingMonths2 = sp2.getMonths() - ChronoUnit.MONTHS.between(sp2.getLastServiceDate(), now);
            return Long.compare(remainingMonths1, remainingMonths2);
        });
        for (ServicePeriod sp : services) {
            int monthsThreshold = sp.getMonths();
            LocalDate oldDate = sp.getLastServiceDate();
            long monthsDifference = ChronoUnit.MONTHS.between(oldDate, now);
            int serviceDueInMonths = (int) (monthsThreshold - monthsDifference);
            int oldKm = sp.getLastServiceKilometers();
            int kilometersThreshold = sp.getKilometers();
            int kmDifference = newKm - oldKm;
            int serviceDueInKm = kilometersThreshold - kmDifference;
            Text serviceText = new Text(sp.getServiceType() + ": ");
            serviceText.setStyle("-fx-font-weight: bold; -fx-fill: black;");
            Text monthsText = new Text();
            Text kmText = new Text();
            if (monthsDifference > monthsThreshold) {
                monthsText.setText(String.format(" %d hó ", serviceDueInMonths));
                monthsText.setStyle("-fx-fill: red;");
            } else if (monthsDifference < monthsThreshold) {
                monthsText.setText(String.format(" %d hó ", serviceDueInMonths));
                monthsText.setStyle("-fx-fill: green;");
            }
            if (kmDifference >= kilometersThreshold) {
                kmText.setText(String.format(" %d km", serviceDueInKm));
                kmText.setStyle("-fx-fill: red;");
            } else {
                kmText.setText(String.format(" %d km", serviceDueInKm));
                kmText.setStyle("-fx-fill: green;");
            }
            serviceDueTextFlow.getChildren().addAll(serviceText, monthsText, kmText, new Text("\n"));
        }
    }

    public static void clearServiceDueTextFlow(TextFlow serviceDueTextFlow) {
        serviceDueTextFlow.getChildren().clear();
    }

    /**
     * Fills the text area in the default view with formatted information from the provided work order.
     *
     * @param wo the WorkOrder object containing the details to be displayed
     * @return a formatted string representation of the work order details
     */
    public static String fillTextArea(WorkOrder wo) {
        boolean isWorkOrder = wo.getTitle().startsWith("ML");
        StringBuilder builder = new StringBuilder();
        if (!wo.getTableData().isEmpty()) {
            String[] rows = wo.getTableData().split("\n");
            for (String row : rows) {
                String[] fields = row.split(";");
                if (!fields[0].isEmpty()) {
                    builder.append("Elvégzett munka :\n")
                            .append(fields[0]).append(" ")
                            .append(fields[2]).append(" (")
                            .append(fields[3]).append(") ")
                            .append("Egységár: ")
                            .append(fields[4]).append(" Ft")
                            .append("\n");
                } else if (!fields[1].isEmpty()) {
                    builder.append("Felhasznált anyag, alkatrész :\n")
                            .append(fields[1]).append(" ")
                            .append(fields[2]).append(" (")
                            .append(fields[3]).append(") ")
                            .append("Egységár: ")
                            .append(fields[4]).append(" Ft")
                            .append("\n");
                }
            }
        }
        return textAreaContent(wo, isWorkOrder, builder);
    }

    /**
     * Constructs the text area content based on the work order details and whether
     * it is a work order or not.
     *
     * @param wo the WorkOrder object containing the details
     * @param isWorkOrder a boolean indicating if the work order is a repair order
     * @param builder the StringBuilder containing formatted work and parts information
     * @return a formatted string containing the complete work order details
     */
    private static String textAreaContent(WorkOrder wo, boolean isWorkOrder, StringBuilder builder) {
        String data = "";
        if (isWorkOrder) {
            data += "Elvégzett javítások:\n" +
                    wo.getIssueOrRepair() +
                    "\n----------------------------------------\n" +
                    "Várható javítások a jövőben:\n" +
                    wo.getExpectedOrNextRepairs() +
                    "\n----------------------------------------\n" +
                    ("Munkát végezte:\n") +
                    wo.getTechnician() +
                    "\n----------------------------------------\n";
        } else {
            data += "Hiba leírása:\n" +
                    wo.getIssueOrRepair() +
                    "\n----------------------------------------\n" +
                    "Várható javítása:\n" +
                    wo.getExpectedOrNextRepairs() +
                    "\n----------------------------------------\n";
        }
        data += "Megjegyzés:\n" +
                wo.getNote() +
                "\n----------------------------------------\n" +
                builder +
                "Alkatrész kedvezmény mértéke: "
                + wo.getDiscountPercent() + "%" +
                "\n" +
                "Alkatrészek összesen: " +
                wo.getPartsCost() + " Ft" +
                "\n" +
                "Munkadíj: " +
                wo.getLaborCost() + " Ft" +
                "\n" +
                "Összesen: " +
                wo.getOverallCost() + " Ft";
        return data;
    }
}

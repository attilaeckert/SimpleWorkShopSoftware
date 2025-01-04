package com.simpleworkshopsoftware.pdfGenerator;

import com.simpleworkshopsoftware.configuration.ConfigManager;
import com.simpleworkshopsoftware.entities.Car;
import com.simpleworkshopsoftware.entities.Customer;
import com.simpleworkshopsoftware.entities.WorkOrder;

import java.util.ArrayList;
import java.util.List;
/**
 * This class, PdfService, is responsible for generating PDF-related information for work orders.
 * It utilizes configuration settings to retrieve company details and formats various
 * information related to customers, cars, and work orders for display in PDFs.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class PdfService {

    private List<String[]> workTableData;
    private List<String[]> partsTableData;
    ConfigManager config = ConfigManager.getInstance();

    public String getCompanyName() {
        return config.getWorkShopName();
    }

    public String getCompanyDetails() {
        return config.getWorkShopDetails();
    }

    public void setTableInformation(WorkOrder wo) {
        this.workTableData = new ArrayList<>();
        this.partsTableData = new ArrayList<>();
        if (!wo.getTableData().isEmpty()) {
            String[] rows = wo.getTableData().split("\n");
            for (String row : rows) {
                String[] fields = row.split(";");
                if (!fields[0].isEmpty()) {
                    this.workTableData.add(new String[]{fields[0], fields[2], fields[3], fields[4]});
                } else if (!fields[1].isEmpty()) {
                    this.partsTableData.add(new String[]{fields[1], fields[2], fields[3], fields[4]});
                }
            }
        }
    }

    public String getCustomerInformation(Customer cust) {
        return String.join("\n",
                cust.getLastName() + " " + cust.getFirstName(),
                cust.getAddress(),
                cust.getPhonenumber(),
                cust.getEmail(),
                cust.getTaxNumber());
    }

    public String getCarModel(Car car) {
        return  String.join(" ",
                car.getBrand(),
                car.getModel(),
                car.getLicensePlateNum());
    }

    public String getCarInformation(Car car) {
        String km = PdfUtils.kmNumberFormat(car.getKilometers());
        return  String.join("\n",
                "Alvázszám: " + car.getVinNumber(),
                "Motorkód: " + car.getEngine(),
                "Km óra állása: " + km);
    }

    public String getFilePath(String documentNumber) {
        return config.getSavePath()+ "/" + documentNumber + ".pdf";
    }

    public String getTailText() { return config.getFooterNote(); }

    public String getNoteFromWorkOrder(WorkOrder wo) {
        return wo.getNote();
    }

    public String getDateFromWorkOrder(WorkOrder wo) {
        return String.valueOf(wo.getDate());
    }

    public String getExpectedDateFromWorkOrder(WorkOrder wo) {
        return String.valueOf(wo.getExpectedDate());
    }

    public String getDiscountFromWorkOrder(WorkOrder wo) {
        return wo.getDiscountPercent();
    }

    public String getPartsCostFromWorkOrder(WorkOrder wo) {
        return PdfUtils.numberFormat(wo.getPartsCost());
    }

    public String getLaborCostFromWorkOrder(WorkOrder wo) {
        return PdfUtils.numberFormat(wo.getLaborCost());
    }

    public String getOverallCostFromWorkOrder(WorkOrder wo) {
        return PdfUtils.numberFormat(wo.getOverallCost());
    }

    public String getWithoutTaxFromWorkOrder(WorkOrder wo) {
        return PdfUtils.priceWithoutTax(wo.getOverallCost());
    }

    public String getTitleFromWorkOrder(WorkOrder wo) {
        return wo.getTitle();
    }

    public String getIssueOrRepairFromWorkOrder(WorkOrder wo) {
        return wo.getIssueOrRepair();
    }

    public String getExpectedOrNextRepairsFromWorkOrder(WorkOrder wo) {
        return wo.getExpectedOrNextRepairs();
    }

    public List<String[]> getWorkTableData(WorkOrder wo) {
        return workTableData;
    }

    public List<String[]> getPartsTableData(WorkOrder wo) {
        return partsTableData;
    }
}

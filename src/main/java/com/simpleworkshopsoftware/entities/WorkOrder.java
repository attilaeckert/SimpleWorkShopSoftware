package com.simpleworkshopsoftware.entities;

import com.simpleworkshopsoftware.base.IdHolder;

import java.time.LocalDate;
/**
 * Represents a work order entity that holds detailed information about
 * a car's repair or maintenance task.
 * * It provides a static factory method to create an empty work order.
 * Implements {@link IdHolder} to uniquely identify a work order by its ID.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrder implements IdHolder<Integer> {

    public enum WorkOrderType {
        ML, MF
    }

    private Integer id;
    private WorkOrderType type;
    private String title;
    private LocalDate date;
    private LocalDate expectedDate;
    private int year;
    private String issueOrRepair;
    private String expectedOrNextRepairs;
    private int kilometers;
    private String tableData;
    private String note;
    private String discountPercent;
    private String partsCost;
    private String laborCost;
    private String overallCost;
    private String technician;
    private boolean closed;
    private Integer carId;

    public WorkOrder(Integer id, WorkOrderType type, String title, LocalDate date,
                     LocalDate expectedDate, int year, String issueOrRepair, String expectedOrNextRepairs,
                     int kilometers, String tableData, String note, String discountPercent,
                     String partsCost, String laborCost, String overallCost, String technician,
                     boolean closed, Integer carId) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.date = date;
        this.expectedDate = expectedDate;
        this.year = year;
        this.issueOrRepair = issueOrRepair;
        this.expectedOrNextRepairs = expectedOrNextRepairs;
        this.kilometers = kilometers;
        this.tableData = tableData;
        this.note = note;
        this.discountPercent = discountPercent;
        this.partsCost = partsCost;
        this.laborCost = laborCost;
        this.overallCost = overallCost;
        this.technician = technician;
        this.closed = closed;
        this.carId = carId;
    }

    public static WorkOrder getEmptyWorkOrder(boolean closed) {
        return new WorkOrder(null, null, "", LocalDate.now(),
                LocalDate.now(), LocalDate.now().getYear(),"", "",
                0, "", "", "", "",
                "", "", "", closed, null);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WorkOrderType getType() {
        return type;
    }

    public void setType(WorkOrderType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(LocalDate expectedDate) {
        this.expectedDate = expectedDate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIssueOrRepair() {
        return issueOrRepair;
    }

    public void setIssueOrRepair(String issueOrRepair) {
        this.issueOrRepair = issueOrRepair;
    }

    public String getExpectedOrNextRepairs() {
        return expectedOrNextRepairs;
    }

    public void setExpectedOrNextRepairs(String expectedOrNextRepairs) {
        this.expectedOrNextRepairs = expectedOrNextRepairs;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(String kilometers) {
        this.kilometers = Integer.parseInt(String.valueOf(kilometers));
    }

    public String getTableData() {
        return tableData;
    }

    public void setTableData(String tableData) {
        this.tableData = tableData;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getPartsCost() {
        return partsCost;
    }

    public void setPartsCost(String partsCost) {
        this.partsCost = partsCost;
    }

    public String getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(String laborCost) {
        this.laborCost = laborCost;
    }

    public String getOverallCost() {
        return overallCost;
    }

    public void setOverallCost(String overallCostTf) {
        this.overallCost = overallCostTf;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}

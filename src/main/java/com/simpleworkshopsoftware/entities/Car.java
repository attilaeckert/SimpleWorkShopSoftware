package com.simpleworkshopsoftware.entities;

import com.simpleworkshopsoftware.base.IdHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents a car entity that holds detailed information about a car.
 * It provides a static factory method to create a default car object,
 * in cases, like a new customer doesn't have a car yet.
 * Implements {@link IdHolder} to uniquely identify a car by its ID.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class Car implements IdHolder<Integer> {

    private Integer id;
    private String brand;
    private String model;
    private String vinNumber;
    private String engine;
    private String year;
    private String fuelType;
    private String licensePlateNum;
    private LocalDate roadCertificate;
    private int kilometers;
    private Integer customerId;
    private List<ServicePeriod> servicePeriods;

    public static Car getDefault() {
        return new Car(null, "", "", "",
                "", "", "", "",
                LocalDate.now(), 0, null, new ArrayList<>());
    }

    public Car(Integer id, String brand, String model, String vinNumber, String engine,
               String year, String fuelType, String licensePlateNum, LocalDate roadCertificate,
               int kilometers, Integer customerId, List<ServicePeriod> servicePeriods) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.vinNumber = vinNumber;
        this.engine = engine;
        this.year = year;
        this.fuelType = fuelType;
        this.licensePlateNum = licensePlateNum;
        this.roadCertificate = roadCertificate;
        this.kilometers = kilometers;
        this.customerId = customerId;
        this.servicePeriods = servicePeriods;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    public LocalDate getRoadCertificate() {
        return roadCertificate;
    }

    public void setRoadCertificate(LocalDate roadCertificate) {
        this.roadCertificate = roadCertificate;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(String kilometers) {
        this.kilometers = Integer.parseInt(kilometers);
    }

    public List<ServicePeriod> getServicePeriods() {
        return servicePeriods;
    }

    public void setServicePeriods(List<ServicePeriod> servicePeriods) {
        this.servicePeriods = servicePeriods;
    }

    @Override
    public String toString() {
        return  year + "  " + brand + " " + model + "  " + licensePlateNum;
    }
}

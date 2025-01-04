package com.simpleworkshopsoftware.entities;

import java.time.LocalDate;
/**
 * Represents a service period belongs to a car.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class ServicePeriod {
    private int carId;
    private String serviceType;
    private int kilometers;
    private int months;
    private int lastServiceKilometers;
    private LocalDate lastServiceDate;

    public ServicePeriod(Integer carId, String serviceType, int kilometers, int months, int lastServiceKilometers, LocalDate lastServiceDate) {
        this.carId = carId;
        this.serviceType = serviceType;
        this.kilometers = kilometers;
        this.months = months;
        this.lastServiceKilometers = lastServiceKilometers;
        this.lastServiceDate = lastServiceDate;
    }

    public static ServicePeriod createWhenServiceDone(int carId, String type, int lastKm, LocalDate lastDate) {
        return new ServicePeriod(carId, type, 0, 0, lastKm, lastDate);
    }

    public static ServicePeriod createWhenServiceNotDone(int carId, String type, int km, int months,  int lastKm) {
        LocalDate defaultDate = LocalDate.now();
        return new ServicePeriod(carId, type, km, months, lastKm, defaultDate);
    }
    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getLastServiceKilometers() {
        return lastServiceKilometers;
    }

    public void setLastServiceKilometers(int lastServiceKilometers) {
        this.lastServiceKilometers = lastServiceKilometers;
    }

    public LocalDate getLastServiceDate() {
        return lastServiceDate;
    }

    public void setLastServiceDate(LocalDate lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }

    @Override
    public String toString() {
        return this.serviceType + " " + this.kilometers + " " + this.months;
    }
}

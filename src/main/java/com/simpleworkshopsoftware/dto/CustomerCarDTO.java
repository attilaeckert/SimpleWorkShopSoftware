package com.simpleworkshopsoftware.dto;

import com.simpleworkshopsoftware.base.IdHolder;
import com.simpleworkshopsoftware.entities.Car;
import com.simpleworkshopsoftware.entities.Customer;
import javafx.beans.property.SimpleStringProperty;
/**
 * This class represents the data in the default view's first TableView.
 * One customer with one car.
 * It implements the IdHolder interface, although it doesn't have an ID.
 * IdHolder works as a wrapper here, to simplify repository creation.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CustomerCarDTO implements IdHolder<Integer> {

    private Customer cust;
    private Car car;
    private SimpleStringProperty lastName;
    private SimpleStringProperty firstName;
    private SimpleStringProperty brand;
    private SimpleStringProperty licensePlate;

    public CustomerCarDTO() {}

    public CustomerCarDTO(Customer cust, Car car) {
        this.cust = cust;
        this.car = car;
        this.lastName = new SimpleStringProperty(this.cust.getLastName());
        this.firstName = new SimpleStringProperty(this.cust.getFirstName());
        this.brand = new SimpleStringProperty(this.car.getBrand());
        this.licensePlate = new SimpleStringProperty(this.car.getLicensePlateNum());
    }


    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getBrand() {
        return brand.get();
    }

    public SimpleStringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public String getLicensePlate() {
        return licensePlate.get();
    }

    public SimpleStringProperty licensePlateProperty() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate.set(licensePlate);
    }

    public Customer getCust() {
        return cust;
    }

    public void setCust(Customer cust) {
        this.cust = cust;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public Integer getId() {
        return 0;
    }
}

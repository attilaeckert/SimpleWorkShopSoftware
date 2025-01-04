package com.simpleworkshopsoftware.entities;

import com.simpleworkshopsoftware.base.IdHolder;
/**
 * Represents a customer entity that holds detailed information about a customer.
 * It can be either an individual or a company, the differences are
 * the presence of a tax number, and the absence of the first name if it's a company.
 * Implements {@link IdHolder} to uniquely identify a car by its ID.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class Customer implements IdHolder<Integer> {

    private Integer id;
    private String lastName;
    private String firstName;
    private String address;
    private String phonenumber;
    private String email;
    private String taxNumber;

    public Customer() {
    }

    public Customer(Integer id, String lastName, String firstName, String address, String phonenumber, String email, String taxNumber) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.phonenumber = phonenumber;
        this.email = email;
        this.taxNumber = taxNumber;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    @Override
    public String toString() {
        return this.lastName + " " + this.firstName;
    }
}

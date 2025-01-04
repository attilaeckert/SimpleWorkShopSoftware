package com.simpleworkshopsoftware.factories;

import com.simpleworkshopsoftware.entities.Customer;
import com.simpleworkshopsoftware.validators.CustomerInputValidator;
import javafx.scene.control.TextField;
/**
 * CustomerFactory class is responsible for creating Customer objects based on user input.
 * It utilizes CustomerInputValidator to validate the input fields and handles
 * potential errors during the creation.
 * If all validations pass, it creates a Customer object with
 * the provided details and returns it.
 * If it fails, it returns null.
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CustomerFactory {

    private final CustomerInputValidator inputValidator = new CustomerInputValidator();

    public Customer makeCustomer(boolean isCompany, TextField[] fields) {
        Customer cust = null;
        if (inputValidator.validateCustomerFields(isCompany, fields)) {
            cust = new Customer();
            if (isCompany) {
                cust.setLastName(fields[0].getText());  // compNameTf
                cust.setFirstName("");                   // No first name for company
                cust.setAddress(fields[1].getText());    // compAddressTf
                cust.setEmail(fields[2].getText().toLowerCase());      // compEmailTf
                cust.setPhonenumber(fields[3].getText()); // compPhoneTf
                cust.setTaxNumber(fields[4].getText());  // compTaxNumTf
            } else {
                cust.setLastName(fields[0].getText());   // lastNameTf
                cust.setFirstName(fields[1].getText());  // firstNameTf
                cust.setAddress(fields[2].getText());    // addressTf
                cust.setEmail(fields[3].getText().toLowerCase());      // emailTf
                cust.setPhonenumber(fields[4].getText()); // phoneTf
                cust.setTaxNumber("");                   // No tax number for person
            }
        }
        return cust;
    }
}

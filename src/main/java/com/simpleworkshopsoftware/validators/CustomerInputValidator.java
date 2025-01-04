package com.simpleworkshopsoftware.validators;

import javafx.scene.control.TextField;
/**
 * The CustomerInputValidator class extends the InputValidator class and provides
 * validations specifically for customer-related input fields. It checks
 * the validity of various fields based on whether the customer is a company or an individual.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CustomerInputValidator extends InputValidator {
    /**
     * Validates the customer input fields based on whether the customer is a company
     * or an individual. Each field is checked against specific patterns to ensure valid input.
     *
     * @param isCompany a boolean indicating if the customer is a company (true) or an individual (false)
     * @param fields an array of TextField objects representing customer input fields
     *               (company name, address, email, phone number, tax number for companies;
     *                last name, first name, address, email, phone number for individuals)
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateCustomerFields(boolean isCompany, TextField[] fields) {
        clear();
        if (isCompany) {
            addValidation(fields[0], val -> val.matches(".*")); // compNameTf
            addValidation(fields[1], val -> val.matches("^\\d{4}.+$")); // compAddressTf
            addValidation(fields[2], val -> val.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")); // compEmailTf
            addValidation(fields[3], val -> val.matches("^(\\+|0)\\d*")); // compPhoneTf
            addValidation(fields[4], val -> val.matches("^\\d{8}-\\d-\\d{2}$")); // compTaxNumTf
        } else {
            // Separate validations for each personal field
            addValidation(fields[0], val -> val.matches("[A-ZÁÉÍÓÖŐÚÜŰ][A-ZÁÉÍÓÖŐÚÜŰa-záéíóöőúüű\\s-]+")); // lastNameTf
            addValidation(fields[1], val -> val.matches("[A-ZÁÉÍÓÖŐÚÜŰ][a-záéíóöőúüű\\s-]+")); // firstNameTf
            addValidation(fields[2], val -> val.matches("^\\d{4}.+$")); // addressTf
            addValidation(fields[3], val -> val.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")); // emailTf
            addValidation(fields[4], val -> val.matches("^(\\+|0)\\d*")); // phoneTf
        }
        return validate();
    }
}
package com.simpleworkshopsoftware.validators;

import javafx.scene.control.TextField;
/**
 * The SettingsInputValidator class extends the InputValidator class and provides
 * validation methods specifically for settings-related input fields. It checks
 * the validity of various fields such as company name, address, phone number,
 * email, tax number, bank account number, and other settings.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class SettingsInputValidator extends InputValidator {

    /**
     * Validates the settings input fields. Each field is checked against not too strict
     * patterns to ensure valid input for the app user company settings.
     *
     * @param fields an array of TextField objects representing settings input fields
     *               (company name, address, phone number, email, tax number, bank account number, etc.)
     * @return true if all fields are valid, false otherwise
     */
    public boolean validateSettingsFields(TextField[] fields) {
        clear();
        addValidation(fields[0], val -> val.matches(".*")); // setCompanyNameTf
        addValidation(fields[1], val -> val.matches("^\\d{4}.+$")); // setCompanyAddressTf
        addValidation(fields[2], val -> val.matches("^(\\+|0)\\d*")); // setCompanyPhoneTf
        addValidation(fields[3], val -> val.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")); // setCompanyEmailTf
        addValidation(fields[4], val -> val.matches("^\\d{8}-\\d-\\d{2}$")); // setCompanyTaxNumTf
        addValidation(fields[5], val -> val.matches("^\\d{8}-\\d{8}-\\d{8}$")); // setCompanyBankAccountTf
        addValidation(fields[6], val -> val.matches("\\d*")); // Additional field validation
        addValidation(fields[7], val -> val.matches("\\d{2}")); // Additional field validation
        return validate();
    }
}
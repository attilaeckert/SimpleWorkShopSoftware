package com.simpleworkshopsoftware.validators;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
/**
 * The CarInputValidator class extends the InputValidator class and provides
 * validations specifically for car-related input fields. It checks
 * the validity of various fields such as brand, model, VIN, engine, year,
 * license plate, license validity date, and kilometers.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CarInputValidator extends InputValidator {

    /**
     * Validates the car input fields and the selected fuel type from the
     * provided ChoiceBox. Each field is checked against not too strict patterns
     * to ensure valid input.
     *
     * @param fields an array of TextField objects representing car input fields
     *               (brand, model, VIN, engine, year, license plate, license validity date, kilometers)
     * @param fuelTypeChoice the ChoiceBox representing the fuel type selection
     * @return true if all fields are valid and a fuel type is selected, false otherwise
     */
    public boolean validateCarFields(TextField[] fields, ChoiceBox<String> fuelTypeChoice) {
        clear();
        addValidation(fields[0], val -> val.matches("[A-Za-z0-9\\s-]+"));    // brandTf
        addValidation(fields[1], val -> val.matches("[A-Za-z0-9\\s-]+"));    // modelTf
        addValidation(fields[2], val -> val.matches("[A-Za-z0-9]{17}")); // vinTf
        addValidation(fields[3], val -> val.matches("[A-Za-z0-9]+"));    // engineTf
        addValidation(fields[4], val -> val.matches("\\d{4}"));           // yearTf
        addValidation(fields[5], val -> val.matches("^[A-Za-z0-9\\-]+")); // licenseTf
        addValidation(fields[6], isValidDate("yyyy-MM-dd"));              // licenseValTf
        addValidation(fields[7], val -> val.matches("\\d+"));             // kmTf
        return validate() && choiceValidator(fuelTypeChoice);
    }
}
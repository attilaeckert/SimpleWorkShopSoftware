package com.simpleworkshopsoftware.validators;

import javafx.scene.control.TextField;
/**
 * The WorkOrderInputValidator class extends the InputValidator class and provides
 * validations such as kilometer, and date inputs.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrderInputValidator extends InputValidator {

    public boolean validateKilometers(TextField km) {
        clear();
        addValidation(km, val -> val.matches("\\d+"));
        return validate();
    }

    public boolean validateDate(TextField expectedDate) {
        clear();
        addValidation(expectedDate, isValidDate("yyyy-MM-dd"));
        return validate();
    }
}

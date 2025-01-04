package com.simpleworkshopsoftware.validators;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
/**
 * The InputValidator class provides different validations.
 * It allows for the addition of validation rules totext fields
 * and provides methods to validate those fields, check for
 * non-empty input, and validate date formats.
 * It can also validate the selection of items in a ChoiceBox.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class InputValidator {
    private final Map<TextField, Predicate<String>> validations = new HashMap<>();
    private static final String ERROR_STYLE = "-fx-border-color: red;";
    private static final String DEFAULT_STYLE = null;
    /**
     * Adds a validation rule for a specific TextField. The rule consists of
     * a non-empty check and a custom predicate for additional validation.
     *
     * @param textField the TextField to add validation for
     * @param predicate the custom validation rule as a Predicate
     */
    public void addValidation(TextField textField, Predicate<String> predicate) {
        validations.put(textField, input -> isNotEmpty().test(input) && predicate.test(input));
    }
    /**
     * Validates all registered TextFields. If a field fails validation,
     * it is highlighted with an error style. Returns true if all fields
     * are valid, false otherwise.
     *
     * @return true if all fields are valid, false if any field is invalid
     */
    public boolean validate() {
        boolean allValid = true;
        for (Map.Entry<TextField, Predicate<String>> entry : validations.entrySet()) {
            TextField tf = entry.getKey();
            Predicate<String> predicate = entry.getValue();
            if (!predicate.test(tf.getText())) {
                tf.setStyle(ERROR_STYLE);
                allValid = false;
            } else {
                tf.setStyle(DEFAULT_STYLE);
            }
        }
        return allValid;
    }
    /**
     * @return a Predicate that checks for non-empty input
     */
    public Predicate<String> isNotEmpty() {
        return input -> !input.trim().isEmpty();
    }
    /**
     * Returns a predicate that checks if the input string is a valid date
     * according to the specified format. Returns false if the input is
     * empty or if the date cannot be parsed.
     *
     * @param format the date format to validate against
     * @return a Predicate that checks for valid date input
     */
    public Predicate<String> isValidDate(String format) {
        return input -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
            if (input.isEmpty()) {
                return false;
            }
            try {
                LocalDate.parse(input, dtf);
                return true;
            } catch (DateTimeException e) {
                return false;
            }
        };
    }

    public void clear() {
        validations.clear();
    }
    /**
     * Validates the selection in a ChoiceBox. If no item is selected,
     * the ChoiceBox is highlighted with an error style. Returns true if
     * an item is selected, false otherwise.
     *
     * @param cb the ChoiceBox to validate
     * @return true if an item is selected, false otherwise
     */
    public boolean choiceValidator(ChoiceBox<String> cb) {
        boolean isSelected = cb.getSelectionModel().getSelectedItem() != null;
        cb.setStyle(isSelected ? DEFAULT_STYLE : ERROR_STYLE);
        return isSelected;
    }
}
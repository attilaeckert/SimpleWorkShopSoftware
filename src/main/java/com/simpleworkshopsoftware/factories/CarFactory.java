package com.simpleworkshopsoftware.factories;

import com.simpleworkshopsoftware.controller.PopupWindowsController;
import com.simpleworkshopsoftware.entities.Car;
import com.simpleworkshopsoftware.validators.CarInputValidator;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.time.LocalDate;
/**
 * CarFactory class is responsible for creating Car objects based on user input.
 * It utilizes CarInputValidator to validate the input fields and handles
 * potential errors during the creation.
 * If all validations pass, it creates a Car object with
 * the provided details and returns it.
 * If it fails, it returns null.
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CarFactory {

    private final CarInputValidator inputValidator = new CarInputValidator();

    public Car makeCar(TextField[] fields, ChoiceBox<String> fuelTypeChoice) {
        Car car = null;
        if (inputValidator.validateCarFields(fields, fuelTypeChoice)) {
            car = Car.getDefault();
            car.setBrand(fields[0].getText());    // brandTf
            car.setModel(fields[1].getText());    // modelTf
            car.setVinNumber(fields[2].getText().toUpperCase());    // vinTf
            car.setEngine(fields[3].getText().toUpperCase());   // engineTf
            car.setYear(fields[4].getText());     // yearTf//
            car.setLicensePlateNum(fields[5].getText().toUpperCase());      //licenseTf
            try {
                car.setRoadCertificate(LocalDate.parse(fields[6].getText()));    //licenseValTf
            } catch (RuntimeException e){
                PopupWindowsController.showDialog("Hibás dátum", PopupWindowsController.DialogType.WARNING, e);
                return null;
            }
            car.setKilometers(fields[7].getText());     // kmTf
            car.setFuelType(fuelTypeChoice.getValue());
        }
        return car;
    }
}

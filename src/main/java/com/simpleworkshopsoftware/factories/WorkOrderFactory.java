package com.simpleworkshopsoftware.factories;

import com.simpleworkshopsoftware.controller.PopupWindowsController;
import com.simpleworkshopsoftware.entities.WorkOrder;
import com.simpleworkshopsoftware.validators.WorkOrderInputValidator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
/**
 * WorkOrderFactory class is responsible for creating WorkOrder objects based on user input.
 * It utilizes a WorkOrderInputValidator to validate the input fields and handles
 * potential errors during the creation.
 * It includes methods to set work order dates, and generate a formatted unique title.
 * If all validations pass, it creates a Work order object with the provided details and returns it.
 * If it fails, it returns null.
 *
 * Author: Attila Eckert
 * Date: 12/27/2024
 * Version: 1.0
 */
public class WorkOrderFactory {

    private final WorkOrderInputValidator inputValidator = new WorkOrderInputValidator();

    public WorkOrder makeWorkOrder(WorkOrder.WorkOrderType type, int workOrderId, String requestTitle,
                                   int carId, String data, TextField[] fields,
                                   TextArea firstTA, TextArea secondTA) {
        boolean isWorkOrder = (type.equals(WorkOrder.WorkOrderType.ML));
        if (!isValidInput(fields, isWorkOrder)) {
            return null;
        }
        WorkOrder wo = WorkOrder.getEmptyWorkOrder(isWorkOrder);    // if it's not a work order, closed is false by default
        wo.setId(isWorkOrder
                ? workOrderId
                : workOrderId + 1);      // id increment needed here for the title generation process, if it's not a work order.
        if (!setWorkOrderDate(fields, wo, isWorkOrder)) {
            return null;
        }
        wo.setYear(wo.getDate().getYear());
        wo.setType(type);
        wo.setIssueOrRepair(firstTA.getText());
        wo.setExpectedOrNextRepairs(secondTA.getText());
        wo.setKilometers(fields[1].getText());      // Kilometers TextField
        wo.setNote(fields[2].getText());            // Note TextField
        wo.setDiscountPercent(fields[3].getText()); // Discount Percent TextField
        wo.setPartsCost(fields[4].getText());       // Parts Cost TextField
        wo.setLaborCost(fields[5].getText());       // Labor Cost TextField
        wo.setOverallCost(fields[6].getText());     // Overall Cost TextField
        if (isWorkOrder) {
            wo.setTechnician(fields[7].getText());  // Technician TextField
        }
        wo.setTableData(data);
        wo.setCarId(carId);
        wo.setTitle(generateTitle(wo, requestTitle));
        return wo;
    }

    private boolean setWorkOrderDate(TextField[] fields, WorkOrder wo, boolean isWorkOrder) {
        LocalDate now = LocalDate.now();
        try {
            wo.setDate(now);
            wo.setExpectedDate(isWorkOrder
                    ? now
                    : LocalDate.parse(fields[0].getText()));   // expectedDateTf
        } catch (DateTimeParseException e) {
            PopupWindowsController.showDialog("Nem létező dátum!", PopupWindowsController.DialogType.WARNING);
            fields[0].setStyle("-fx-border-color: red;");
            return false;
        }
        return true;
    }

    private boolean isValidInput(TextField[] fields, boolean isWorkOrder) {
        if (isWorkOrder) {
            return inputValidator.validateKilometers(fields[1]);
        } else {
            return inputValidator.validateDate(fields[0]) && inputValidator.validateKilometers(fields[1]);
        }
    }

public String generateTitle(WorkOrder wo, String requestTitle) {
    if (!requestTitle.isEmpty()) {
        return requestTitle.replace("MF", "ML");
    }
    String year = String.valueOf(wo.getYear());
    String number = String.format("%05d", wo.getId());
    return wo.getType().name() + "-" + year + "-" + number;
}
}

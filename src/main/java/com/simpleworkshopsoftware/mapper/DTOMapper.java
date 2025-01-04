package com.simpleworkshopsoftware.mapper;

import com.simpleworkshopsoftware.base.Database;
import com.simpleworkshopsoftware.entities.Car;
import com.simpleworkshopsoftware.entities.Customer;
import com.simpleworkshopsoftware.entities.WorkOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.simpleworkshopsoftware.base.DBConst.*;
/**
 * DTOMapper class that provides methods to map database result sets
 * to various entity objects such as Customer, Car, and WorkOrder. It includes methods
 * to convert single result sets into individual entities and to convert multiple
 * result sets into lists of entities. Additionally, it offers a static method to convert
 * lists into JavaFX ObservableLists for use in Table components.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class DTOMapper {

    public static <T> ObservableList<T> toObservableList(List<T> data) {
        return FXCollections.observableArrayList(data);
    }

    public Customer mapToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt(CUSTOMER_ID),
                rs.getString(LAST_NAME),
                rs.getString(FIRST_NAME),
                rs.getString(ADDRESS),
                rs.getString(PHONE),
                rs.getString(EMAIL),
                rs.getString(TAXNUM)
        );
    }

    public List<Customer> mapToCustomerList(ResultSet rs) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        while (rs.next()) {
            customers.add(mapToCustomer(rs));
        }
        return customers;
    }

    public Car mapToCar(ResultSet rs) throws SQLException {
        int id = rs.getInt(CAR_ID);
        return new Car(
                rs.getInt(CAR_ID),
                rs.getString(BRAND),
                rs.getString(MODEL),
                rs.getString(VIN),
                rs.getString(ENGINE),
                rs.getString(CAR_YEAR),
                rs.getString(FUEL),
                rs.getString(LICPLATENUM),
                rs.getDate(ROADCERTIFICATE).toLocalDate(),
                Integer.parseInt(rs.getString(KM)),
                rs.getInt(CUSTOMER_ID),
                Database.getCarServiceRepository().findAll(id));
    }

    public List<Car> mapToCarList(ResultSet rs) throws SQLException {
        List<Car> cars = new ArrayList<>();
        while (rs.next()) {
            cars.add(mapToCar(rs));
        }
        return cars;
    }

    public WorkOrder mapToWorkOrder(ResultSet rs) throws SQLException {
        return new WorkOrder(
                rs.getInt(WORK_ORDER_ID),
                WorkOrder.WorkOrderType.valueOf(rs.getString(WORK_ORDER_TYPE)),
                rs.getString(TITLE),
                rs.getDate(DATE).toLocalDate(),
                rs.getDate(EXPECTED_DATE).toLocalDate(),
                rs.getInt(WORK_ORDER_YEAR),
                rs.getString(REPAIR),
                rs.getString(NEXT_REPAIRS),
                Integer.parseInt(rs.getString(WORK_ORDER_KM)),
                rs.getString(TABLE_DATA),
                rs.getString(NOTE),
                rs.getString(DISCOUNTPERCENT),
                rs.getString(PARTSPRICESUM),
                rs.getString(LABORCOST),
                rs.getString(OVERALLCOST),
                rs.getString(TECHNICICAN),
                rs.getBoolean(CLOSED),
                rs.getInt(CAR_ID));
    }

    public List<WorkOrder> mapToWorkOrderList(ResultSet rs) throws SQLException {
        List<WorkOrder> workOrders = new ArrayList<>();
        while (rs.next()) {
            workOrders.add(mapToWorkOrder(rs));
        }
        return workOrders;
    }
}

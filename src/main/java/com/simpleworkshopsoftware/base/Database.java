package com.simpleworkshopsoftware.base;

import com.simpleworkshopsoftware.controller.PopupWindowsController;
import com.simpleworkshopsoftware.repo.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.simpleworkshopsoftware.base.DBConst.*;
/**
 * Database class that implements the Singleton design pattern to manage database connections and repositories.
 * It initializes various repository instances for managing customers, cars, work orders, and services.
 * The class provides methods to set the database connection, retrieve repository instances, and handle table creation.
 * Error handling is performed through a popup window controller for user notifications.
 *
 *  @author Attila Eckert
 *  @date 12/27/2024
 *  @version 1.0
 */
public class Database {

    private static Database _instance;
    private static Connection connection;
    private static CustomerRepository customerRepository;
    private static CarRepository carRepository;
    private static WorkOrderRepository workOrderRepository;
    private static CustomerCarDTORepository customerCarDTORepository;
    private static WorkOrderDTORepository workOrderDTORepository;
    private static CarServiceRepository carServiceRepository;

    public static void setConnection(Connection conn) throws SQLException {
        connection = conn;
        List<String> tableQueries = List.of(CREATE_TABLE_CUSTOMERS, CREATE_TABLE_CARS, CREATE_TABLE_WORK_ORDERS, CREATE_TABLE_SERVICE_PERIODS);
        try {
            // creates the database tables
            TablesConst.createTables(tableQueries, conn);
        } catch (SQLException e) {
            PopupWindowsController.showDialog("Nem sikerült létrehoznom az adatbázis táblákat",
                    PopupWindowsController.DialogType.ALERT, e);
        }
    }

    private Database() throws SQLException {
        customerRepository = new CustomerRepository(connection);
        carRepository = new CarRepository(connection);
        workOrderRepository = new WorkOrderRepository(connection);
        customerCarDTORepository = new CustomerCarDTORepository(connection);
        workOrderDTORepository = new WorkOrderDTORepository(connection);
        carServiceRepository = new CarServiceRepository(connection);
    }


    public static Database getInstance() throws SQLException {
        if (_instance == null) {
            if (connection == null) {
                PopupWindowsController.showDialog(
                        "Nem sikerült kapcsolódni az adatbázishoz", PopupWindowsController.DialogType.WARNING);
            } else {
                _instance = new Database();
            }
        }
        return _instance;
    }

    public static CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public static  CarRepository getCarRepository() {
        return carRepository;
    }

    public static WorkOrderRepository getWorkOrderRepository() {return workOrderRepository;}

    public static CustomerCarDTORepository getCustomerCarDTORepository() {
        return customerCarDTORepository;
    }

    public static WorkOrderDTORepository getWorkOrderDTORepository() { return workOrderDTORepository; }

    public static CarServiceRepository getCarServiceRepository() {return carServiceRepository; }
}

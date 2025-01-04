package com.simpleworkshopsoftware.base;
/**
 * This class defines constants for database table names and their respective column names
 * used in the application. It also contains SQL statements for creating tables related to
 * customers, cars, work orders, and service periods with appropriate data types and
 * foreign key relationships.
 *
 *  @author Attila Eckert
 *  @date 12/27/2024
 *  @version 1.0
 */
public class DBConst {

    public static final String TABLE_CUSTOMERS = "customer";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone_number";
    public static final String EMAIL = "email";
    public static final String TAXNUM = "taxnumber";

    public static final String TABLE_CARS = "car";
    public static final String CAR_ID = "car_id";
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final String VIN = "vin_number";
    public static final String ENGINE = "engine";
    public static final String CAR_YEAR =  "car_year";
    public static final String FUEL = "fuel_type";
    public static final String LICPLATENUM = "license_plate_num";
    public static final String ROADCERTIFICATE = "road_certificate";
    public static final String KM = "kilometers";

    public static final String TABLE_WORK_ORDERS = "work_order";
    public static final String WORK_ORDER_ID = "work_order_id";
    public static final String WORK_ORDER_TYPE = "work_order_type";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String EXPECTED_DATE = "expected_date";
    public static final String WORK_ORDER_YEAR = "work_order_year";
    public static final String REPAIR = "repair";
    public static final String NEXT_REPAIRS = "next_repairs";
    public static final String WORK_ORDER_KM = "work_order_km";
    public static final String TABLE_DATA = "table_data";
    public static final String NOTE = "note";
    public static final String DISCOUNTPERCENT = "discount_percent";
    public static final String PARTSPRICESUM = "parts_price_sum";
    public static final String LABORCOST = "labor_cost";
    public static final String OVERALLCOST = "overall_cost";
    public static final String TECHNICICAN = "technician";
    public static final String CLOSED = "closed";

    public static final String TABLE_SERVICE_PERIODS = "service_periods";
    public static final String SERVICE_PERIODS_ID = "service_periods_id";
    public static final String SERVICE_TYPE = "service_type";
    public static final String SERVICE_KM = "service_kilometers";
    public static final String SERVICE_MONTHS = "service_months";
    public static final String LAST_SERVICE_KM = "last_service_kilometers";
    public static final String LAST_SERVICE_DATE = "last_service_date";

    public static final String CREATE_TABLE_CUSTOMERS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMERS + " (" +
                    CUSTOMER_ID + " INT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    FIRST_NAME + " VARCHAR(50), " +
                    LAST_NAME + " VARCHAR(50), " +
                    ADDRESS + " VARCHAR(100), " +
                    PHONE + " VARCHAR(15), " +
                    EMAIL + " VARCHAR(50), " +
                    TAXNUM + " VARCHAR(20) " +
                    ")";

    public static final String CREATE_TABLE_CARS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CARS + " (" +
                    CAR_ID + " INT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    BRAND + " VARCHAR(25), " +
                    MODEL + " VARCHAR(25), " +
                    VIN + " CHAR(17), " +
                    ENGINE + " VARCHAR(25), " +
                    CAR_YEAR + " INT, " +
                    FUEL + " CHAR(10), " +
                    LICPLATENUM + " VARCHAR(10), " +
                    ROADCERTIFICATE + " DATE, " +
                    KM + " INT, " +
                    CUSTOMER_ID + " INT , " +
                    "FOREIGN KEY(" + CUSTOMER_ID + ") REFERENCES " + TABLE_CUSTOMERS + "(" + CUSTOMER_ID + ")" +
                    "ON DELETE CASCADE" +
                    ")";

    public static final String CREATE_TABLE_WORK_ORDERS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_WORK_ORDERS + " (" +
                    WORK_ORDER_ID + " INT NOT NULL, " +
                    WORK_ORDER_TYPE + " CHAR(2) NOT NULL, " +
                    TITLE + " VARCHAR(50), " +
                    DATE + " DATE, " +
                    EXPECTED_DATE + " DATE, " +
                    WORK_ORDER_YEAR + " INT NOT NULL, " +
                    REPAIR + " VARCHAR(255), " +
                    NEXT_REPAIRS + " VARCHAR(255), " +
                    WORK_ORDER_KM + " INT, " +
                    TABLE_DATA + " VARCHAR(255), " +
                    NOTE + " VARCHAR(255), " +
                    DISCOUNTPERCENT + " VARCHAR(5), " +
                    PARTSPRICESUM + " VARCHAR(50), " +
                    LABORCOST + " VARCHAR(50), " +
                    OVERALLCOST + " VARCHAR(50), " +
                    TECHNICICAN + " VARCHAR(50), " +
                    CLOSED + " BOOLEAN, " +
                    CAR_ID + " INT, " +
                    "PRIMARY KEY (" + WORK_ORDER_ID + ", " + WORK_ORDER_TYPE + ", " + WORK_ORDER_YEAR + "), " +
                    "FOREIGN KEY(" + CAR_ID + ") REFERENCES " + TABLE_CARS + "(" + CAR_ID + ") " +
                    "ON DELETE CASCADE" +
                    ")";

    public static final String CREATE_TABLE_SERVICE_PERIODS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SERVICE_PERIODS + " (" +
                    SERVICE_PERIODS_ID + " INT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    SERVICE_TYPE + " VARCHAR (25), " +
                    SERVICE_KM + " INT, " +
                    SERVICE_MONTHS + " INT, " +
                    LAST_SERVICE_KM + " INT, " +
                    LAST_SERVICE_DATE + " DATE, " +
                    CAR_ID + " INT , " +
                    "FOREIGN KEY(" + CAR_ID + ") REFERENCES " + TABLE_CARS + "(" + CAR_ID + ")" +
                    "ON DELETE CASCADE" +
                    ")";
}

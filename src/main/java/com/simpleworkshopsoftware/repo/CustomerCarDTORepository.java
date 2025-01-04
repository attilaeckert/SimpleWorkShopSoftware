package com.simpleworkshopsoftware.repo;

import com.simpleworkshopsoftware.dto.CustomerCarDTO;
import com.simpleworkshopsoftware.entities.Car;
import com.simpleworkshopsoftware.entities.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.simpleworkshopsoftware.base.DBConst.*;
/**
 * CustomerCarDTORepository class provides methods to interact with the database for DTO entities.
 * It extends AbstractRepository and utilizes a DTO mapper to convert ResultSet data into Objects.
 * It provides a findAll method to retrieve customer and car data from a database using a SQL LEFT JOIN query.
 * Other CRUD methods are defined but not needed yet.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CustomerCarDTORepository extends AbstractRepository<CustomerCarDTO, Integer> {

    public CustomerCarDTORepository(Connection conn) throws SQLException {
            this.findAll = conn.prepareStatement(
                        "SELECT " + TABLE_CUSTOMERS + ".*, " + TABLE_CARS + ".*" +
                            " FROM " + TABLE_CUSTOMERS +
                            " LEFT JOIN " + TABLE_CARS + " ON " + TABLE_CUSTOMERS + "." + CUSTOMER_ID + " = " + TABLE_CARS + "." + CUSTOMER_ID );
    }

    public List<CustomerCarDTO> findAll() throws SQLException {
        List<CustomerCarDTO> data = new ArrayList<>();
        try (ResultSet rs = this.findAll.executeQuery()) {
            while (rs.next()) {
                Customer cust = dtoMapper.mapToCustomer(rs);
                Car car = Car.getDefault();
                if (rs.getObject(CAR_ID) != null) {
                    car = dtoMapper.mapToCar(rs);
                }
                CustomerCarDTO dto = new CustomerCarDTO(cust, car);
                data.add(dto);
            }
        }
        return data;
    }
    // not used
    public CustomerCarDTO findById(Integer integer) {return null;}
    // not used
    public void insert(CustomerCarDTO m) throws SQLException {}
    // not used
    public void update(CustomerCarDTO m) throws SQLException {}
    // not used
    public void delete(Integer id) throws SQLException {}
}

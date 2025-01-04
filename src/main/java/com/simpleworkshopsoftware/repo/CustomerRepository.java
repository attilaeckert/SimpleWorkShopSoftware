package com.simpleworkshopsoftware.repo;

import com.simpleworkshopsoftware.entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.simpleworkshopsoftware.base.DBConst.*;
/**
 * CustomerRepository class provides methods to interact with the database for Customer entities.
 * It extends AbstractRepository and utilizes a DTO mapper to convert ResultSet data into Customer objects.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CustomerRepository extends AbstractRepository<Customer, Integer> {


        public CustomerRepository(Connection conn) throws SQLException {
            this.conn = conn;
            this.findAll = conn.prepareStatement("SELECT * FROM " + TABLE_CUSTOMERS);
            this.findById = conn.prepareStatement("SELECT * FROM " + TABLE_CUSTOMERS + " WHERE " + CUSTOMER_ID + " = ?");
            this.insert = conn.prepareStatement(
                    "INSERT INTO " + TABLE_CUSTOMERS + " (" + FIRST_NAME +
                            ", " + LAST_NAME + ", " +  ADDRESS + ", " + PHONE +
                            ", " + EMAIL + ", " + TAXNUM +
                            ") VALUES(?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            this.update = conn.prepareStatement(
                    "UPDATE " + TABLE_CUSTOMERS +
                            " SET " + FIRST_NAME + " = ?, " + LAST_NAME +
                            " = ?, " + ADDRESS + " = ?, " + PHONE + " = ?, " + EMAIL +
                            " = ?, " + TAXNUM + " = ?" +
                            " WHERE " + CUSTOMER_ID + " = ? ");
            this.delete = conn.prepareStatement("DELETE FROM " + TABLE_CUSTOMERS + " WHERE " + CUSTOMER_ID + " = ?");
        }

        @Override
        public List<Customer> findAll() throws SQLException {
            try (ResultSet rs = this.findAll.executeQuery()) {
                return dtoMapper.mapToCustomerList(rs);
            }
        }

        @Override
        public Customer findById(Integer id) throws SQLException {
            this.findById.setInt(1, id);
            try (ResultSet rs = this.findById.executeQuery()) {
                return rs.next() ? dtoMapper.mapToCustomer(rs) : null;
            }
        }

    public int findGeneratedId() throws SQLException {
        int insertedId = 0;
        try (ResultSet generatedKeys = this.insert.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                insertedId = generatedKeys.getInt(1);
            }
        }
        return insertedId;
    }

        @Override
        public void insert(Customer cust) throws SQLException {
            setParameters(this.insert, cust);
            this.insert.executeUpdate();
        }

        @Override
        public void update(Customer cust) throws SQLException {
            setParameters(this.update, cust);
            this.update.executeUpdate();
        }

        // not used
        @Override
        public void delete(Integer id) throws SQLException {
            this.delete.setInt(1,id);
            this.delete.executeUpdate();
        }

    private void setParameters(PreparedStatement stmt, Customer cust) throws SQLException {
        stmt.setString(1, cust.getFirstName());
        stmt.setString(2, cust.getLastName());
        stmt.setString(3, cust.getAddress());
        stmt.setString(4, cust.getPhonenumber());
        stmt.setString(5, cust.getEmail());
        stmt.setString(6, cust.getTaxNumber());
        if (cust.getId() != null) {
            stmt.setInt(7, cust.getId());
        }
    }

    }

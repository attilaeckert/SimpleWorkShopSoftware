package com.simpleworkshopsoftware.repo;

import com.simpleworkshopsoftware.entities.Car;

import java.sql.*;
import java.util.List;

import static com.simpleworkshopsoftware.base.DBConst.*;
/**
 * CarRepository class provides methods to interact with the database for Car entities.
 * It extends AbstractRepository and utilizes a DTO mapper to convert ResultSet data into Car objects.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class CarRepository extends AbstractRepository<Car, Integer> {

    private final PreparedStatement findByCustomerId;
    private final PreparedStatement updateKm;

    public CarRepository(Connection conn) throws SQLException {
        this.conn = conn;
        this.findAll = conn.prepareStatement("SELECT * FROM " + TABLE_CARS);
        this.findById = conn.prepareStatement("SELECT * FROM " + TABLE_CARS + " WHERE " + CAR_ID + " = ?");
        this.findByCustomerId = conn.prepareStatement("SELECT * FROM " + TABLE_CARS + " WHERE " + CUSTOMER_ID + " = ?");
        this.updateKm = conn.prepareStatement("UPDATE " + TABLE_CARS + " SET " + KM + " = ? WHERE " + CAR_ID + " = ?");
        this.insert = conn.prepareStatement(
                "INSERT INTO " + TABLE_CARS + " (" +
                        BRAND + ", " + MODEL + ", " + VIN + ", " + ENGINE + ", " +
                        CAR_YEAR + ", " + FUEL + ", " + LICPLATENUM + ", " +
                        ROADCERTIFICATE + ", " + KM + ", " + CUSTOMER_ID +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        this.update = conn.prepareStatement(
                "UPDATE " + TABLE_CARS +
                " SET " + BRAND + " = ? , " + MODEL + " = ? , " + VIN + " = ? , " +
                ENGINE + " = ? , " + CAR_YEAR + " = ? , " + FUEL + " = ? ," +
                " " + LICPLATENUM + " = ? , " + ROADCERTIFICATE + " = ? , " + KM + " = ? " +
                " WHERE " + CAR_ID + " = ?");
        this.delete = conn.prepareStatement("DELETE FROM " + TABLE_CARS + " WHERE " + CAR_ID + " = ?");
    }

    @Override
    public List<Car> findAll() throws SQLException {
        try (ResultSet rs = this.findAll.executeQuery()) {
            return dtoMapper.mapToCarList(rs);
        }
    }

    @Override
    public Car findById(Integer id) throws SQLException {
        this.findById.setInt(1, id);
        try (ResultSet rs = this.findById.executeQuery()) {
            return rs.next() ? dtoMapper.mapToCar(rs) : null;
        }
    }

    public List<Car> findByCustomerId(Integer id) throws SQLException {
        this.findByCustomerId.setInt(1, id);
        try (ResultSet rs = this.findByCustomerId.executeQuery()) {
            return dtoMapper.mapToCarList(rs);
        }
    }

    @Override
    public void insert(Car car) throws SQLException {
        setParameters(this.insert, car);
        this.insert.executeUpdate();
    }

    @Override
    public void update(Car car) throws SQLException {
        setParameters(this.update, car);
        this.update.executeUpdate();
    }


    @Override
    public void delete(Integer id) throws SQLException {
        this.delete.setInt(1, id);
        this.delete.executeUpdate();
    }

    public void updateKm(int id, String km) throws SQLException {
        this.updateKm.setString(1, km);
        this.updateKm.setInt(2, id);
        this.updateKm.executeUpdate();
    }

    private void setParameters(PreparedStatement stmt, Car car) throws SQLException {
        stmt.setString(1, car.getBrand());
        stmt.setString(2, car.getModel());
        stmt.setString(3, car.getVinNumber());
        stmt.setString(4, car.getEngine());
        stmt.setString(5, car.getYear());
        stmt.setString(6, car.getFuelType());
        stmt.setString(7, car.getLicensePlateNum());
        stmt.setDate(8, Date.valueOf(car.getRoadCertificate()));
        stmt.setInt(9, car.getKilometers());
        stmt.setInt(10, car.getId() == null ? car.getCustomerId() : car.getId());
    }
}

package com.simpleworkshopsoftware.repo;

import com.simpleworkshopsoftware.entities.ServicePeriod;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.simpleworkshopsoftware.base.DBConst.*;
public class CarServiceRepository {

    PreparedStatement findAll;
    PreparedStatement insert;
    PreparedStatement updateServicePeriods;
    PreparedStatement updateLastService;
    PreparedStatement checkIfIdExists;

    public CarServiceRepository(Connection conn) throws SQLException {
        this.findAll = conn.prepareStatement("SELECT * FROM " + TABLE_SERVICE_PERIODS + " WHERE " + CAR_ID + " = ?");
        this.insert = conn.prepareStatement("INSERT INTO " + TABLE_SERVICE_PERIODS + " (" + SERVICE_TYPE + ", " +
                SERVICE_KM + ", " + SERVICE_MONTHS + ", " + LAST_SERVICE_KM + ", " + LAST_SERVICE_DATE +
                ", " + CAR_ID + ") " + "VALUES (?, ?, ?, ?, ?, ?)");
        this.updateServicePeriods = conn.prepareStatement("UPDATE " + TABLE_SERVICE_PERIODS + " SET " +
                SERVICE_KM + " = ?, " + SERVICE_MONTHS + " =? WHERE " + CAR_ID + " = ? AND " + SERVICE_TYPE + " = ?");
        this.updateLastService = conn.prepareStatement("UPDATE " + TABLE_SERVICE_PERIODS + " SET " +
                LAST_SERVICE_KM + " = ?, " + LAST_SERVICE_DATE + " =? WHERE " + CAR_ID + " = ? AND " + SERVICE_TYPE + " = ?");
        this.checkIfIdExists = conn.prepareStatement("SELECT COUNT(*) FROM " + TABLE_SERVICE_PERIODS + " WHERE " + CAR_ID + " = ?");
    }

    public List<ServicePeriod> findAll(Integer id) throws SQLException {
        List<ServicePeriod> services = new ArrayList<>();
        this.findAll.setInt(1,id);
        try (ResultSet rs = this.findAll.executeQuery()) {
            while (rs.next()) {
                ServicePeriod sp = new ServicePeriod(
                        rs.getInt(CAR_ID),
                        rs.getString(SERVICE_TYPE),
                        rs.getInt(SERVICE_KM),
                        rs.getInt(SERVICE_MONTHS),
                        rs.getInt(LAST_SERVICE_KM),
                        rs.getDate(LAST_SERVICE_DATE).toLocalDate());
                services.add(sp);
            }
        }
        return services;
    }

    public boolean check(int id) throws SQLException {
        this.checkIfIdExists.setInt(1, id);
        try (ResultSet rs = checkIfIdExists.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    return false;
    }


    public void insert(List<ServicePeriod> periods, Integer id) throws SQLException {
        if (check(id)) {
            throw new SQLException();
        }
        for (ServicePeriod sp : periods) {
            this.insert.setString(1, sp.getServiceType());
            this.insert.setInt(2, sp.getKilometers());
            this.insert.setInt(3, sp.getMonths());
            this.insert.setInt(4, sp.getLastServiceKilometers());
            this.insert.setDate(5, Date.valueOf(sp.getLastServiceDate()));
            this.insert.setInt(6, sp.getCarId());
            this.insert.addBatch();
        }
        this.insert.executeBatch();
    }

    public void updateServicePeriods(List<ServicePeriod> periods, Integer id) throws SQLException {
        for (ServicePeriod sp : periods) {
            this.updateServicePeriods.setInt(1, sp.getKilometers());
            this.updateServicePeriods.setInt(2, sp.getMonths());
            this.updateServicePeriods.setInt(3, sp.getCarId());
            this.updateServicePeriods.setString(4, sp.getServiceType());
            this.updateServicePeriods.addBatch();
        }
        this.updateServicePeriods.executeBatch();
    }

    public void updateLastService(List<ServicePeriod> periods) throws SQLException {
        for (ServicePeriod sp : periods) {
            this.updateLastService.setInt(1, sp.getLastServiceKilometers());
            this.updateLastService.setDate(2, Date.valueOf(sp.getLastServiceDate()));
            this.updateLastService.setInt(3, sp.getCarId());
            this.updateLastService.setString(4, sp.getServiceType());
            this.updateLastService.addBatch();
        }
        this.updateLastService.executeBatch();
    }

    public void delete(ServicePeriod sp) {
    }
}

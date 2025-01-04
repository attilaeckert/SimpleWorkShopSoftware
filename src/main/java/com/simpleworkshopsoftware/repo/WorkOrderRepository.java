package com.simpleworkshopsoftware.repo;

import com.simpleworkshopsoftware.entities.WorkOrder;

import java.sql.*;
import java.util.List;

import static com.simpleworkshopsoftware.base.DBConst.*;
/**
 * WorkOrderRepository class provides methods to interact with the database for Work Order entities.
 * It extends AbstractRepository and utilizes a DTO mapper to convert ResultSet data into Work Order objects.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrderRepository extends AbstractRepository<WorkOrder, Integer> {

    PreparedStatement findByStatus;
    PreparedStatement updateTitle;
    PreparedStatement updateClosed;

    public WorkOrderRepository(Connection conn) throws SQLException {
        this.conn = conn;
        this.findAll = conn.prepareStatement("SELECT * FROM " + TABLE_WORK_ORDERS);
        this.findById = conn.prepareStatement(
                "SELECT MAX(" + WORK_ORDER_ID + ") " +
                        "FROM " + TABLE_WORK_ORDERS +
                        " WHERE " + WORK_ORDER_TYPE + " = ?" +
                        " AND " + WORK_ORDER_YEAR + " = ?");
        this.findByStatus = conn.prepareStatement("SELECT COUNT (*) FROM " + TABLE_WORK_ORDERS + "  WHERE " + CLOSED + " = ?");
        this.insert = conn.prepareStatement(
                "INSERT INTO " + TABLE_WORK_ORDERS +
                        " (" + WORK_ORDER_ID + ", " + WORK_ORDER_TYPE + ", " +  TITLE + ", " +
                        DATE + ", " + EXPECTED_DATE + ", " + WORK_ORDER_YEAR + ", " +
                        REPAIR + ", " + NEXT_REPAIRS + ", " + WORK_ORDER_KM + ", " +
                        TABLE_DATA + ", " + NOTE + ", " + DISCOUNTPERCENT + ", " +
                        PARTSPRICESUM + ", " + LABORCOST + ", " + OVERALLCOST + ", " +
                        TECHNICICAN + ", " + CLOSED + ", " + CAR_ID + ")" +
                        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        this.updateTitle = conn.prepareStatement("UPDATE " + TABLE_WORK_ORDERS + " SET " + TITLE + " = ? WHERE " + WORK_ORDER_ID + " = ?");
        this.updateClosed = conn.prepareStatement("UPDATE " + TABLE_WORK_ORDERS + " SET " + CLOSED + " = ? WHERE " + WORK_ORDER_ID +
                " = ? AND " + WORK_ORDER_TYPE + " = ?");
        this.delete = conn.prepareStatement("DELETE FROM " + TABLE_WORK_ORDERS + " WHERE " + WORK_ORDER_ID + " = ?");
    }

    @Override
    public List<WorkOrder> findAll() throws SQLException {
        try (ResultSet rs = this.findAll.executeQuery()) {
            return dtoMapper.mapToWorkOrderList(rs);
        }
    }

    @Override
    public WorkOrder findById(Integer id) throws SQLException {
        this.findById.setInt(1, id);
        try (ResultSet rs = this.findById.executeQuery()) {
            return rs.next() ? dtoMapper.mapToWorkOrder(rs) : null;
        }
    }

    @Override
    public void insert(WorkOrder wo) throws SQLException {
        this.insert.setInt(1, wo.getId());
        this.insert.setString(2, wo.getType().name());
        this.insert.setString(3, wo.getTitle());
        this.insert.setDate(4, Date.valueOf(wo.getDate()));
        this.insert.setDate(5, Date.valueOf(wo.getExpectedDate()));
        this.insert.setInt(6,wo.getYear());
        this.insert.setString(7, wo.getIssueOrRepair());
        this.insert.setString(8, wo.getExpectedOrNextRepairs());
        this.insert.setInt(9, wo.getKilometers());
        this.insert.setString(10, wo.getTableData());
        this.insert.setString(11, wo.getNote());
        this.insert.setString(12, wo.getDiscountPercent());
        this.insert.setString(13, wo.getPartsCost());
        this.insert.setString(14, wo.getLaborCost());
        this.insert.setString(15, wo.getOverallCost());
        this.insert.setString(16, wo.getTechnician());
        this.insert.setBoolean(17, wo.isClosed());
        this.insert.setInt(18, wo.getCarId());
        this.insert.executeUpdate();
    }
    // not used
    @Override
    public void update(WorkOrder wo) throws SQLException {
    }
    // not used
    @Override
    public void delete(Integer id) throws SQLException {
        this.delete.setInt(1, id);
        this.delete.executeUpdate();
    }

    public int findLastIdInCurrentYear(WorkOrder.WorkOrderType type, int year) throws SQLException {
        int lastId = 1;
        this.findById.setString(1, type.name());
        this.findById.setInt(2, year);
        try (ResultSet rs = this.findById.executeQuery()) {
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
        }
        return lastId;
    }

    public int findByStatus(Boolean status) throws SQLException {
        int result = 0;
        this.findByStatus.setBoolean(1, status);
        try (ResultSet rs = this.findByStatus.executeQuery()) {
            if (rs.next()) {
                result = rs.getInt(1);
            }
        }
        return result;
    }

    public void updateClosed(int id, boolean isClosed, WorkOrder.WorkOrderType type) throws SQLException {
        this.updateClosed.setBoolean(1, isClosed);
        this.updateClosed.setInt(2, id);
        this.updateClosed.setString(3, type.name());
        this.updateClosed.executeUpdate();
    }
}

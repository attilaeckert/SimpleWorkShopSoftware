package com.simpleworkshopsoftware.repo;

import com.simpleworkshopsoftware.dto.WorkOrderDTO;
import com.simpleworkshopsoftware.entities.WorkOrder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.simpleworkshopsoftware.base.DBConst.*;
/**
 * WorkOrderDTORepository class provides methods to interact with the database for DTO entities.
 * It extends AbstractRepository and utilizes a DTO mapper to convert ResultSet data into Objects.
 * It provides a findAll method to retrieve customer and car data from a database using a SQL LEFT JOIN query.
 * Other CRUD methods are defined but not needed yet.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrderDTORepository extends AbstractRepository<WorkOrderDTO, Integer> {

    public WorkOrderDTORepository(Connection conn) throws SQLException {
        this.findAll = conn.prepareStatement(
                "SELECT " + TABLE_WORK_ORDERS + "." + "*" +
                " FROM " + TABLE_CARS +
                " LEFT JOIN " + TABLE_WORK_ORDERS + " ON " + TABLE_CARS + "." + CAR_ID + " = " + TABLE_WORK_ORDERS + "." + CAR_ID +
                " WHERE " + TABLE_CARS + "." + CAR_ID + " = ?");
    }

    public List<WorkOrderDTO> findAll(Integer id) throws SQLException {
        List<WorkOrderDTO> data = new ArrayList<>();
        this.findAll.setInt(1, id);
        try (ResultSet rs = this.findAll.executeQuery()) {
            while (rs.next()) {
                if (rs.getObject(WORK_ORDER_ID) != null) {
                    WorkOrderDTO dto = new WorkOrderDTO(rs.getString(WORK_ORDER_KM), rs.getString(DATE),
                            rs.getString(TITLE), dtoMapper.mapToWorkOrder(rs));
                    data.add(dto);
                }
            }
        }
        return data;
    }
    // not used
    public List<WorkOrderDTO> findAll() throws SQLException {
        return null;
    }
    // not used
    public WorkOrderDTO findById(Integer id) throws SQLException {
        WorkOrderDTO dto = null;
        WorkOrder wo;
        this.findById.setInt(1, id);
        try (ResultSet rs = this.findById.executeQuery()) {
            while (rs.next()) {
                if (rs.getObject(WORK_ORDER_ID) != null) {
                    String date = (rs.getDate(DATE) != null) ? rs.getDate(DATE).toString() : "";
                    wo = WorkOrder.getEmptyWorkOrder(false);
                    wo.setId(rs.getInt(WORK_ORDER_ID));
                    wo.setTitle(rs.getString(TITLE));
                    wo.setDate(LocalDate.parse(date));
                    wo.setIssueOrRepair(rs.getString(REPAIR));
                    wo.setExpectedOrNextRepairs(rs.getString(NEXT_REPAIRS));
                    wo.setKilometers(rs.getString(WORK_ORDER_KM));
                    wo.setTableData(rs.getString(TABLE_DATA));
                    wo.setNote(rs.getString(NOTE));
                    wo.setDiscountPercent(rs.getString(DISCOUNTPERCENT));
                    wo.setPartsCost(rs.getString(PARTSPRICESUM));
                    wo.setLaborCost(rs.getString(LABORCOST));
                    wo.setOverallCost(rs.getString(OVERALLCOST));
                    wo.setTechnician(rs.getString(TECHNICICAN));
                    wo.setClosed(rs.getBoolean(CLOSED));
                    dto = new WorkOrderDTO(rs.getString(WORK_ORDER_KM), date,
                            rs.getString(TITLE), wo);
                }
            }
        }
        return dto;
    }
    // not used
    public void insert(WorkOrderDTO m) throws SQLException {}
    // not used
    public void update(WorkOrderDTO m) throws SQLException {}
    // not used
    public void delete(Integer id) throws SQLException {}
}

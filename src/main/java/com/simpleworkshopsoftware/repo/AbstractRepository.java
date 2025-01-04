package com.simpleworkshopsoftware.repo;

import com.simpleworkshopsoftware.base.IdHolder;
import com.simpleworkshopsoftware.mapper.DTOMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * AbstractRepository is an abstract class that serves as a base for repository implementations.
 * The class is generic, allowing it to work with any type that extends IdHolder.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public abstract class AbstractRepository<T extends IdHolder<ID>, ID> implements Repository<T,ID> {

    protected Connection conn;
    protected PreparedStatement findAll;
    protected PreparedStatement findById;
    protected PreparedStatement insert;
    protected PreparedStatement delete;
    protected PreparedStatement update;
    protected final DTOMapper dtoMapper = new DTOMapper();

    @Override
    public void close() throws SQLException {
        findAll.close();
        findById.close();
        insert.close();
        delete.close();
        update.close();
    }
}

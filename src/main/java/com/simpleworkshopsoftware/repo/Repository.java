package com.simpleworkshopsoftware.repo;

import java.sql.SQLException;
import java.util.List;
/**
 * This interface defines a generic Repository for CRUD operations on entities of type T with an identifier of type ID.
 *
 * @param <T> the type of the entity
 * @param <ID> the type of the entity's identifier
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public interface Repository<T,ID> {

    List<T> findAll() throws SQLException;

    T findById(ID id) throws SQLException;

    void insert(T m) throws SQLException;

    void update(T m) throws SQLException;

    void delete(ID id) throws SQLException;

    void close() throws SQLException;
}

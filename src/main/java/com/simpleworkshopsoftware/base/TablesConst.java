package com.simpleworkshopsoftware.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
/**
 * This class provides a method to create database tables using SQL statements.
 * It takes a list of SQL table creation queries and a database connection,
 * executing each query to create the corresponding tables in the database.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class TablesConst {

    public static void createTables(List<String> tableQueries, Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            for (String s : tableQueries) {
                st.execute(s);
            }
        }
    }
}

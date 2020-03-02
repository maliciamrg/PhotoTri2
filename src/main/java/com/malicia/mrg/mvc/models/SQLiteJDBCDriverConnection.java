package com.malicia.mrg.mvc.models;

import java.sql.*;
import java.util.logging.Logger;

/**
 * The type Sq lite jdbc driver connection.
 *
 * @author sqlitetutorial.net
 */
public class SQLiteJDBCDriverConnection {
    private static final Logger LOGGER;
    /**
     * The constant conn.
     */
    public static Connection conn;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    /**
     * Connect to a sample database
     *
     * @param sqlliteDatabase the sqllite database
     */
    public static void connect(String sqlliteDatabase) {
        LOGGER.info("connect to database : " + sqlliteDatabase);
        conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + sqlliteDatabase;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            LOGGER.finer("Connection to SQLite has been established.");

        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    /**
     * Disconnect.
     */
    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    /**
     * select all rows in the warehouses table
     *
     * @param sql the sql
     * @return the boolean
     */
    public static boolean execute(String sql) {


        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            LOGGER.finest(sql);

            return stmt.execute(sql);

        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return false;
    }

    /**
     * select all rows in the warehouses table
     *
     * @param sql the sql
     * @return the result set
     * @throws SQLException the sql exception
     */
    public static ResultSet select(String sql) throws SQLException {


        Statement stmt = null;
//        try {
        stmt = conn.createStatement();
        LOGGER.finest(sql);

        // forcage display du resultset
        ResultSet resultSet = stmt.executeQuery(sql);
//            display_resultset(resultSet);

        return stmt.executeQuery(sql);


//        } catch (SQLException e) {
//            LOGGER.severe(e.getMessage());
//        }
//        return null;
    }

    private static void display_resultset(ResultSet resultSet) throws SQLException {
        ResultSet resultSetAff = resultSet;

        ResultSetMetaData rsmd = resultSetAff.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        String colname = " + ";
        for (int i = 1; i <= columnsNumber; i++) {
            colname += String.format("%-20s", rsmd.getColumnName(i)) + " + ";
        }
        LOGGER.info(colname);


        while (resultSetAff.next()) {
            String columnValue = " + ";
            for (int i = 1; i <= columnsNumber; i++) {
                columnValue += String.format("%-20s", resultSetAff.getString(i)) + " + ";
            }
            LOGGER.info(columnValue);
        }

    }

    /**
     * select all rows in the warehouses table
     *
     * @param sql the sql
     * @return the int
     */
    public int executeUpdate(String sql) {


        Statement stmt = null;
        try {
            stmt = conn.createStatement();

            return stmt.executeUpdate(sql);

        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return 0;
    }
}
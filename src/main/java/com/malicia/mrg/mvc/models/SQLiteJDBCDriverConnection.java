package com.malicia.mrg.mvc.models;

import java.sql.*;
import java.util.logging.Logger;

/**
 * The type Sq lite jdbc driver connection.
 *
 * @author sqlitetutorial.net
 */
public class SQLiteJDBCDriverConnection {
    private final Logger LOGGER;
    /**
     * The constant conn.
     */
    private Connection conn;

    /**
     * Instantiates a new Sq lite jdbc driver connection.
     *
     * @param catalogLrcat the catalog lrcat
     */
    public SQLiteJDBCDriverConnection(String catalogLrcat) {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        connect(catalogLrcat);
    }

    /**
     * Connect to a sample database
     *
     * @param sqlliteDatabase the sqllite database
     */
    public void connect(String sqlliteDatabase) {
        LOGGER.info(() -> "connect to database : " + sqlliteDatabase);
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
    public void disconnect() {
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
     * @throws SQLException the sql exception
     */
    public boolean execute(String sql) throws SQLException {

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            LOGGER.fine(sql);
            return stmt.execute(sql);
        } finally {
            conn.close();
        }

    }

    /**
     * select all rows in the warehouses table
     *
     * @param sql the sql
     * @return the result set
     * @throws SQLException the sql exception
     */
    public ResultSet select(String sql) throws SQLException {


        Statement stmt = null;

        stmt = conn.createStatement();
        LOGGER.fine(sql);

        // forcage display du resultset
        ResultSet resultSet = stmt.executeQuery(sql);
        displayResultset(resultSet);

        return stmt.executeQuery(sql);


    }

    /**
     * select all rows in the warehouses table
     *
     * @param sql the sql
     * @return the int
     * @throws SQLException the sql exception
     */
    public int executeUpdate(String sql) throws SQLException {


        Statement stmt = null;

        stmt = conn.createStatement();
        LOGGER.fine(sql);
        return stmt.executeUpdate(sql);


    }


    private void displayResultset(ResultSet resultSet) throws SQLException {
        ResultSet resultSetAff = resultSet;

        ResultSetMetaData rsmd = resultSetAff.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        StringBuilder colname = new StringBuilder();
        colname.append(" + ");
        for (int i = 1; i <= columnsNumber; i++) {
            colname.append(String.format("%-20s", rsmd.getColumnName(i)) + " + ");
        }
        LOGGER.fine(() -> "" + colname.toString());


        while (resultSetAff.next()) {
            StringBuilder columnValue = new StringBuilder();
            columnValue.append(" + ");
            for (int i = 1; i <= columnsNumber; i++) {
                columnValue.append(String.format("%-20s", resultSetAff.getString(i)) + " + ");
            }
            LOGGER.fine(() -> "" + columnValue.toString());
        }

    }

}
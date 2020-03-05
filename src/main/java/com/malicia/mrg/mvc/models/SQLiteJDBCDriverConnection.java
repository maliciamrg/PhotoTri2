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
    private static Connection conn;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    /**
     * Instantiates a new Sq lite jdbc driver connection.
     */
    private SQLiteJDBCDriverConnection() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Connect to a sample database
     *
     * @param sqlliteDatabase the sqllite database
     */
    public static void connect(String sqlliteDatabase) {
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
     * @throws SQLException the sql exception
     */
    public static boolean execute(String sql) throws SQLException {

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
    public static ResultSet select(String sql) throws SQLException {


        Statement stmt = null;

        stmt = conn.createStatement();
        LOGGER.fine(sql);

        // forcage display du resultset
        ResultSet resultSet = stmt.executeQuery(sql);
        displayResultset(resultSet);

        return stmt.executeQuery(sql);


    }

    private static void displayResultset(ResultSet resultSet) throws SQLException {
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

    /**
     * select all rows in the warehouses table
     *
     * @param sql the sql
     * @return the int
     * @throws SQLException the sql exception
     */
    public static int executeUpdate(String sql) throws SQLException {


        Statement stmt = null;

        stmt = conn.createStatement();
        LOGGER.fine(sql);
        return stmt.executeUpdate(sql);


    }
}
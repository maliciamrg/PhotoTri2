package com.malicia.mrg.mvc.models;

import java.sql.*;
import java.util.logging.Logger;

/**
 *
 * @author sqlitetutorial.net
 */
public class SQLiteJDBCDriverConnection {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public static Connection conn;

    /**
     * Connect to a sample database
     */
    public static void connect(String sqlliteDatabase) {
        LOGGER.info("connect to database : "+sqlliteDatabase);
        conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:"+sqlliteDatabase;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            LOGGER.finer("Connection to SQLite has been established.");

        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
    }

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
     */
    public int executeUpdate(String sql ){


        Statement stmt  = null;
        try {
            stmt = conn.createStatement();

            return stmt.executeUpdate(sql);

        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return 0;
    }

    /**
     * select all rows in the warehouses table
     */
    public static boolean execute(String sql){


        Statement stmt  = null;
        try {
            stmt = conn.createStatement();

            LOGGER.finest(sql);

            return stmt.execute(sql);

        } catch (SQLException e) {
            LOGGER.severe( e.getMessage());
        }
        return false;
    }


    /**
     * select all rows in the warehouses table
     */
    public static ResultSet select(String sql){


        Statement stmt  = null;
        try {
            stmt = conn.createStatement();
            LOGGER.finest(sql);
            return stmt.executeQuery(sql);


        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return null;
    }
}
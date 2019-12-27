package com.malicia.mrg.model.sqlite;

import java.sql.*;
/**
 *
 * @author sqlitetutorial.net
 */
public class SQLiteJDBCDriverConnection {
    public static Connection conn;

    /**
     * Connect to a sample database
     */
    public static void connect(String sqlliteDatabase) {
        conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:"+sqlliteDatabase;
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            logger.log("Connection to SQLite has been established.");

        } catch (SQLException e) {
            logger.log(e.getMessage());
        }
    }

    public static void disconnect() {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.log(ex.getMessage());
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
            logger.log("context" , e);
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

            logger.log(sql);

            return stmt.execute(sql);

        } catch (SQLException e) {
            logger.log("context" , e);
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
            logger.log(sql);
            return stmt.executeQuery(sql);


        } catch (SQLException e) {
            logger.log("context" , e);
        }
        return null;
    }
}
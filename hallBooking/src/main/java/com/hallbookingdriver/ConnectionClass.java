/*
* The ConnectionClass class implements an application that 
* simply have the connection of database  
* @author Suvetha Jayagopi(Expleo)
* @since 18 FEB 2024
*
* 
*/
package com.hallbookingdriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USERNAME = "SYSTEM";
    private static final String PASSWORD = "Suvetha";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}


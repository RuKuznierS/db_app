/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db_app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author COGETI
 */
public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    
    public DatabaseConnection(String server, String user, String password){
        if (server.equals("mysql"))
            URL = "jdbc:mysql://localhost:3306/";
        if (server.equals("postgres"))
            URL = "jdbc:postgresql://localhost:5432/";
        USER = user;
        PASSWORD = password;
    }
    public static Connection getConnection(String dbName) throws SQLException {
        return DriverManager.getConnection(URL + dbName, USER, PASSWORD);
    }
}

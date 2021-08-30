package edu.conn;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServlet;

import java.sql.Date;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Connect extends HttpServlet {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private static PreparedStatement stmt = null;

    private static Connection conn = null;
    private static final String URL = "jdbc:mysql://localhost:3306/Muebleria";
    private static final String USER = "root";
    private static final String PASS = "N47ur41B1u35#";

    public static boolean closeSession() {
        boolean success = false;
        
        if (stmt != null) {
            try {
                stmt.close();
                stmt = null;
                success = true;
            } catch (SQLException e) {
                success = false;
            }
        } if (conn != null) {
            try {
                conn.close();
                conn = null;
                success = true;
            } catch (SQLException ex) {
                success = false;
            }
        }
        return success;
    }

    public static boolean createSession() {
        boolean success = true;
        
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException | SQLException e) {
                success = false;
            }
        }
        
        return success;
    }

    public static PreparedStatement getPrepareStatement(String query) throws SQLException {
        return Connect.conn.prepareStatement(query);
    }

    public static String getDate(Date fecha) {
        LocalDate date = fecha.toLocalDate();
        return date.format(formatter);
    }
}
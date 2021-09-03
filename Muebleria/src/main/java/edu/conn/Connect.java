package edu.conn;

import javax.servlet.http.HttpServlet;

import edu.general.TagFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Connect extends HttpServlet {

    private static Connection conn = null;
    private static final String URL = "jdbc:mysql://localhost:3306/Muebleria";
    private static final String USER = "root";
    private static final String PASS = "N47ur41B1u35#";

    public static boolean closeSession() {
        boolean success = false;
        
        if (conn != null) {
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

    protected static String getOpciones(String query) {
        try {
            StringBuilder sb = new StringBuilder();
            PreparedStatement ps = getPrepareStatement(query);
            
            ResultSet res = ps.executeQuery();

            while (res.next())
                sb.append(TagFactory.getOptions(res.getString(1)));

            return sb.toString();
        } catch (SQLException e) {
            return "Hubo un error con las opciones, intentelo mas tarde.";
        }
    }
}
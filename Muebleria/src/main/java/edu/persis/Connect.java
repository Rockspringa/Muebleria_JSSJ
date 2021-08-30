package edu.persis;

import edu.general.TagFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Connect {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private static final String select = "SELECT * FROM ? WHERE ? ? ?";
    private static PreparedStatement stmt = null;

    private static Connection conn = null;
    private static final String URL = "jdbc:mysql://localhost:3306/Muebleria";
    private static final String USER = "root";
    private static final String PASS = "N47ur41B1u35#";

    public static String selectEstudiante(String campo, String operador, String dato) {
        StringBuilder sb = new StringBuilder();
        String query = getSelectQuery("estudiante", campo, operador);
        ResultSet res = getResultSet(query, dato);

        try {
            while (res != null && res.next()) {
                sb.append(TagFactory.getRow(String.valueOf(res.getInt(1)),
                        res.getString(2), res.getString(3),
                        getDate(res.getDate(4))));
            }
        } catch (SQLException e) {
            sb.append("Ocurrio un error al llamar los datos.");
        }

        return sb.toString();
    }

    public static String selectAsignacion(String campo, String operador, String dato) {
        StringBuilder sb = new StringBuilder();
        String query = getSelectQuery("asignacion", campo, operador);
        ResultSet res = getResultSet(query, dato);

        try {
            while (res != null && res.next()) {
                sb.append(TagFactory.getRow(getDate(res.getDate(1)),
                        String.valueOf(res.getInt(2)),
                        String.valueOf(res.getInt(3)),
                        String.valueOf(res.getBoolean(4))));
            }
        } catch (SQLException e) {
            sb.append("Ocurrio un error al llamar los datos.");
        }

        return sb.toString();
    }

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

    @Deprecated
    private static ResultSet getResultSet(String query, String ...dato) {
        ResultSet res = null;
        
        try {
            stmt = conn.prepareStatement(query);

            for (int i = 0; i < dato.length; i++)
                stmt.setString(i + 1, dato[i]);

            res = stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Ocurrio un error al crear el query." + query + dato[0]);
        }

        return res;
    }

    private static String getSelectQuery(String tabla, String campo, String operador) {
        String query = select.replaceFirst("\\?", tabla);
        query = query.replaceFirst("\\?", campo);
        query = query.replaceFirst("\\?", operador);

        return query;
    }
}
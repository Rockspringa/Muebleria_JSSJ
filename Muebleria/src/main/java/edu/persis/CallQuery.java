package edu.persis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CallQuery {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private static final String select = "SELECT * FROM ? WHERE ? ? ?";
    private static PreparedStatement stmt = null;

    private static Connection conn = null;
    private static final String url = "jdbc:mysql://localhost:3306/Muebleria";
    private static final String user = "root";
    private static final String pass = "N47ur41B1u35#";

    public static String isUsuario(String usuario, String contraseña) {
        String query = "CALL IS_USUARIO (?, ?)";
        ResultSet res = getResultSet(query, usuario, contraseña);
        String response = "";

        try {
            while (res != null && res.next()) {
                response += res.getString(1);
            }
        } catch (SQLException e) {
            response = "Hubo un error al validar el usuario";
        }

        return response;
    }

    public static String selectEstudiante(String campo, String operador, String dato) {
        StringBuilder sb = new StringBuilder();
        String query = getSelectQuery("estudiante", campo, operador);
        ResultSet res = getResultSet(query, dato);

        try {
            while (res != null && res.next()) {
                sb.append(getRow(String.valueOf(res.getInt(1)),
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
                sb.append(getRow(getDate(res.getDate(1)),
                        String.valueOf(res.getInt(2)),
                        String.valueOf(res.getInt(3)),
                        String.valueOf(res.getBoolean(4))));
            }
        } catch (SQLException e) {
            sb.append("Ocurrio un error al llamar los datos.");
        }

        return sb.toString();
    }

    public static String closeSession() {
        String res = "";
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                res += "Hubo un error al cerrar el statement.\n";
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                res += "Hubo un error al cerrar la conexion.\n";
            }
        }
        return res;
    }

    public static String createSession() {
        if (conn != null) return "Conexion ya existente";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            return "ready";
        } catch (Exception e) {
            return "Hubo un error al conectar" + e.getMessage();
        }
    }

    private static String getDate(Date fecha) {
        LocalDate date = fecha.toLocalDate();
        return date.format(formatter);
    }

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

    private static String getRow(String ...args) {
        StringBuilder sb = new StringBuilder("<tr>");
        for (int i = 0; i < args.length; i++) {
            sb.append("<td>");
            sb.append(args[i]);
            sb.append("</td>");
        }
        sb.append("</tr>");

        return sb.toString();
    }
}
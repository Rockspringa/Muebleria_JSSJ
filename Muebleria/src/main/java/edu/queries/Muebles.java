package edu.queries;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.conn.Connect;
import edu.general.TagFactory;

@WebServlet(name = "Muebles", urlPatterns = {"/fabrica/muebles"})
public class Muebles extends Connect {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();

        createSession();

        String outString = switch (request.getParameter("datos")) {
            case "muebles" -> getMuebles();
            case "piezas"  -> getPiezas(request.getParameter("mueble"));
            default -> "";
        };

        closeSession();

        out.println(outString);
    }

    private static String getMuebles() {
        String query = "CALL GET_MUEBLES ()";
        try {
            ResultSet res = getPrepareStatement(query).executeQuery();
            StringBuilder sb = new StringBuilder();

            while (res.next())
                sb.append(TagFactory.getSquaredP(res.getString(1), res.getString(2)));

            return sb.toString();
        } catch (SQLException e) {
            return "No se pudieron recuperar los muebles";
        }
    }

    private static String getPiezas(String mueble) {
        String query = "CALL GET_PIEZAS_IND (?)";
        try {
            StringBuilder sb = new StringBuilder();
            PreparedStatement ps = getPrepareStatement(query);
            ps.setString(1, mueble);
            
            ResultSet res = ps.executeQuery();

            while (res.next())
                sb.append(TagFactory.getListNum(res.getString(1), res.getString(2), res.getBoolean(3)));

            return sb.toString();
        } catch (SQLException e) {
            return "No se pudieron recuperar las piezas";
        }
    }
}

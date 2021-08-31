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

import edu.obj.Usuario;
import edu.conn.Connect;
import edu.general.TagFactory;

@WebServlet(name = "Muebles", urlPatterns = {"/fabrica/muebles"})
public class Muebles extends Connect {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();

        createSession();

        String outString = switch (request.getParameter("opcion")) {
            case "muebles"   -> getMuebles();
            case "piezas"    -> getPiezas(request.getParameter("mueble"));
            case "ensamblar" -> ensamblarMueble(request.getParameter("mueble"), request);
            case "mueblesLi" -> getListaMuebles();
            case "indicacio" -> getIndicaciones(request.getParameter("mueble"));
            case "precios"   -> getPrecios(request.getParameter("mueble"));
            default -> "No se ejecuto ninguna opcion.";
        };

        closeSession();

        out.println(outString);
    }

    private static String ensamblarMueble(String mueble, HttpServletRequest request) {
        String query = "CALL ENSAMBLAR_NOW (?, ?)";

        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, mueble);
            ps.setString(2, ((Usuario) request.getSession().getAttribute("user")).getUsername());
            
            ResultSet res = ps.executeQuery();

            res.next();
            String salida = res.getString(1);
            if (salida.equals("falta el mueble")) return "El mueble no existe";
            else if (salida.equals("falta el usuario")) return "No esta logeado";
            else if (salida.equals("faltan piezas")) return "No hay piezas suficientes";
            else return salida;
        } catch (SQLException e) {
            return "No se pudo ensamblar el mueble";
        } catch (NullPointerException e) {
            return "Se obtuvo que no hay un usuario logeado";
        }
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

    private static String getListaMuebles() {
        String query = "CALL GET_MUEBLES ()";
        try {
            ResultSet res = getPrepareStatement(query).executeQuery();
            StringBuilder sb = new StringBuilder();

            while (res.next())
                sb.append(TagFactory.getListMuebles(res.getString(1)));

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
                sb.append(TagFactory.getListNum(res.getString(1), res.getString(2), res.getInt(3)));

            return sb.toString();
        } catch (SQLException e) {
            return "No se pudieron recuperar las piezas";
        }
    }

    private static String getIndicaciones(String mueble) {
        String query = "CALL GET_PIEZAS_IND_TABLA (?)";
        
        try {
            StringBuilder sb = new StringBuilder();
            PreparedStatement ps = getPrepareStatement(query);
            String[] titles = {"Tipo de Pieza", "Costo de la Pieza", "Cantidad necesaria"};

            ps.setString(1, mueble);
            
            ResultSet res = ps.executeQuery();

            while (res.next())
                sb.append(TagFactory.getRow(res.getInt(1), res.getString(2),
                            res.getString(3), res.getString(4)));

            return TagFactory.getTable(titles, sb.toString());
        } catch (SQLException e) {
            return "No se pudieron recuperar las indicaciones";
        }
    }

    private static String getPrecios(String mueble) {
        String query = "CALL GET_PRECIOS_MUEBLE (?)";

        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, mueble);
            
            ResultSet res = ps.executeQuery();

            res.next();
            return res.getString(1) + "_" + res.getString(2);

        } catch (SQLException e) {
            return "No se pudieron recuperar el precio y coste del mueble";
        }
    }
}

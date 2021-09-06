package edu.queries;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.conn.Connect;
import edu.errores.NegativeNumberException;
import edu.errores.NonAlfanumericStringException;
import edu.general.Corroborador;
import edu.general.TagFactory;

@WebServlet(name = "venta", urlPatterns = { "/ventas/venta" })
public class Ventas extends Connect {

    private ResultSet res = null;
    protected static ArrayList<Integer> list = new ArrayList<>();
    protected static ArrayList<String> tipos = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String salida = "";

        createSession();

        try {
            switch (Corroborador.parseAlfanumericString(req.getParameter("opcion"))) {
                case "agregar":
                    list.add(Integer.valueOf(req.getParameter("id")));
                    tipos.add(req.getParameter("tipo"));
                    break;

                case "tipos":
                    salida = getTipos();
                    break;

                case "datos":
                    salida = getDatos(Corroborador.parseAlfanumericString(req.getParameter("tipo")));
                    break;

                case "reset":
                    list.clear();;
                    break;

                default:
                    salida = "No se pudo recuperar correctamente la opcion.";
            }
        } catch (NonAlfanumericStringException e) {
            salida = "Solo se aceptan entradas alfanumericas.";
        } catch (NegativeNumberException | NumberFormatException e) {
            salida = "No se recuro el numero de forma correcta.";
        } catch (SQLException e) {
            salida = "No se pudo completar la operacion.";
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        closeSession();
        resp.getWriter().print(salida);
    }

    private String getTipos() throws SQLException {
        StringBuilder sb = new StringBuilder();
        String query = "CALL GET_TIPOS_MUEBLE ()";
        res = getPrepareStatement(query).executeQuery();

        while (res.next())
            sb.append(TagFactory.getOptions(res.getString(1)));

        return sb.toString();
    }

    private String getDatos(String mueble) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String query = "SELECT precio FROM MUEBLE WHERE nombre = ?";
        PreparedStatement ps = getPrepareStatement(query);
        ps.setString(1, mueble);

        res = ps.executeQuery();
        res.next();
        sb.append(res.getDouble(1) + "_");

        query = "SELECT mueble_id FROM ENSAMBLADO WHERE tipo = ?";
        ps = getPrepareStatement(query);
        ps.setString(1, mueble);

        res = ps.executeQuery();

        while (res.next()) {
            int value = res.getInt(1);
            if (!list.contains(value))
                sb.append(TagFactory.getOptions(String.valueOf(value)));
        }

        return sb.toString();
    }

}

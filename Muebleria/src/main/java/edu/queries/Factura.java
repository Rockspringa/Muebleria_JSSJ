package edu.queries;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.errores.NonAlfanumericStringException;
import edu.general.Corroborador;

@WebServlet(name = "factura", urlPatterns = { "/ventas/factura" })
public class Factura extends Ventas {

    ResultSet res = null;
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String salida = "";

        createSession();

        try {
            switch (Corroborador.parseAlfanumericString(req.getParameter("opcion"))) {
                case "muebles":
                    salida = getMuebles();
                    break;

                case "cliente":
                    salida = existsCliente(Corroborador.parseAlfanumericString(req.getParameter("nit")));
                    break;

                case "agregar":
                    salida = addCliente(req);
                    break;

                default:
                    salida = "No se pudo recuperar correctamente la opcion.";
            }
        } catch (NonAlfanumericStringException e) {
            salida = "Solo se aceptan entradas alfanumericas.";
        } catch (SQLException e) {
            salida = "Hubo un error con la consulta, vuelva a intentarlo.";
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
        if (!salida.equals("")) resp.getWriter().print(salida);
    }

    private String getMuebles() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            sb.append("<div class=\"item flex\"><div>Identificador del mueble: </div><div id=\"id-mueble\">");
            sb.append(String.valueOf(list.get(i)));
            sb.append("</div><div>Tipo de mueble: </div><div id=\"tipo\">");
            sb.append(tipos.get(i));
            sb.append("</div></div>");
        }

        if (sb.toString().equals("")) sb.append("null");
        return sb.toString();
    }

    private String existsCliente(String nit) throws SQLException {
        String query = "SELECT nombre, direccion, municipio, departamento FROM CLIENTE WHERE nit = ";
        PreparedStatement ps = getPrepareStatement(query);

        ps.setString(1, nit);

        res = ps.executeQuery();

        if (res.next()) {
            return res.getString(1) + "_" + res.getString(2) + "_" + res.getString(3) + "_" + res.getString(4);
        } else {
            return "No existe el nit.";
        }
    }

    private String addCliente(HttpServletRequest req) throws SQLException {
        String nit = Corroborador.parseAlfanumericString(req.getParameter("nit"));
        String nombre = Corroborador.parseAlfanumericString(req.getParameter("nombre"));
        String direc = Corroborador.parseAlfanumericString(req.getParameter("direc"));
        String muni = Corroborador.parseAlfanumericString(req.getParameter("muni"));
        String depar = Corroborador.parseAlfanumericString(req.getParameter("depar"));

        if (muni == null || muni.equals("") || depar == null || depar.equals("")) {
            String query = "INSERT INTO CLIENTE (nit, nombre, direccion) VALUES (?, ?, ?)";
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, nit);
            ps.setString(2, nombre);
            ps.setString(3, direc);

            ps.executeUpdate();
            return "success";
        } else {
            String query = "INSERT INTO CLIENTE VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, nit);
            ps.setString(2, nombre);
            ps.setString(3, direc);
            ps.setString(4, muni);
            ps.setString(5, depar);

            ps.executeUpdate();
            return "success";
        }
    }
}

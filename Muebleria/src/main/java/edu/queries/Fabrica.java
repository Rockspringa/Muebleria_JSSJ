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
import edu.errores.NegativeNumberException;
import edu.errores.NonAlfanumericStringException;
import edu.general.Corroborador;
import edu.general.TagFactory;

@WebServlet(name = "fabrica", urlPatterns = { "/fabrica/queryDB" })
public class Fabrica extends Connect {

    private ResultSet res = null;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        createSession();

        String outString;
        try {
            switch (Corroborador.parseAlfanumericString(request.getParameter("opcion"))) {
                case "muebles":
                    outString = getMuebles();
                    break;

                case "piezas":
                    outString = getPiezas(Corroborador.parseAlfanumericString(request.getParameter("mueble")));
                    break;

                case "ensamblar":
                    outString = ensamblarMueble(Corroborador.parseAlfanumericString(request.getParameter("mueble")),
                            request);
                    break;

                case "mueblesLi":
                    outString = getListaMuebles();
                    break;

                case "indicacio":
                    outString = getIndicaciones(Corroborador.parseAlfanumericString(request.getParameter("mueble")));
                    break;

                case "precios":
                    outString = getPrecios(Corroborador.parseAlfanumericString(request.getParameter("mueble")));
                    break;

                case "piezasTab":
                    outString = getTablaPiezas();
                    break;

                case "creadoTab":
                    outString = getCreados();
                    break;

                case "piezasOpt":
                    outString = getOpciones("SELECT tipo FROM MATERIA_PRIMA");
                    break;

                case "costosOpt":
                    outString = getCostosOpciones(request.getParameter("pieza"));
                    break;

                case "cantidOpt":
                    outString = getCantidad(request);
                    break;

                case "addPiezas":
                    outString = addPiezas(request);
                    break;

                case "crePiezas":
                    outString = crearPieza(request);
                    break;

                case "updPiezas":
                    outString = actualizarPieza(request);
                    break;

                case "delPiezas":
                    outString = eliminarPieza(request);
                    break;

                default:
                    outString = "No se ejecuto ninguna opcion.";
            }

        } catch (NonAlfanumericStringException | NegativeNumberException e) {
            outString = e.getMessage();
        } catch (NumberFormatException e) {
            outString = "Ingrese solamente numeros, intentelo nuevamente.";
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                res = null;
            }
        }

        closeSession();

        out.print(outString);
    }

    private String ensamblarMueble(String mueble, HttpServletRequest request) {
        String query = "CALL ENSAMBLAR_NOW (?, ?)";

        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, mueble);
            ps.setString(2, ((Usuario) request.getSession().getAttribute("user")).getUsername());

            res = ps.executeQuery();

            res.next();
            String salida = res.getString(1);

            if (salida.equals("No existe el usuario"))
                return "No esta logeado";
            else
                return salida;
        } catch (SQLException e) {
            return "No se pudo ensamblar el mueble";
        } catch (NullPointerException e) {
            return "Se obtuvo que no hay un usuario logeado";
        }
    }

    private String getMuebles() {
        String query = "CALL GET_MUEBLES ()";
        try {
            res = getPrepareStatement(query).executeQuery();
            StringBuilder sb = new StringBuilder();

            while (res.next())
                sb.append(TagFactory.getSquaredP(res.getString(1), res.getString(2)));

            return sb.toString();
        } catch (SQLException e) {
            return "No se pudieron recuperar los muebles";
        }
    }

    private String getListaMuebles() {
        String query = "CALL GET_MUEBLES ()";
        try {
            res = getPrepareStatement(query).executeQuery();
            StringBuilder sb = new StringBuilder();

            while (res.next())
                sb.append(TagFactory.getListMuebles(res.getString(1)));

            return sb.toString();
        } catch (SQLException e) {
            return "No se pudieron recuperar los muebles";
        }
    }

    private String getPiezas(String mueble) {
        String query = "CALL GET_PIEZAS_IND (?)";
        try {
            StringBuilder sb = new StringBuilder();
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, mueble);

            res = ps.executeQuery();

            while (res.next())
                sb.append(TagFactory.getListNum(res.getString(1), res.getString(2), res.getInt(3)));

            return sb.toString();
        } catch (SQLException e) {
            return "No se pudieron recuperar las piezas";
        }
    }

    private String getIndicaciones(String mueble) {
        String query = "CALL GET_PIEZAS_IND_TABLA (?)";

        try {
            StringBuilder sb = new StringBuilder();
            PreparedStatement ps = getPrepareStatement(query);
            String[] titles = { "Tipo de Pieza", "Costo de la Pieza", "Cantidad necesaria" };

            ps.setString(1, mueble);

            res = ps.executeQuery();

            while (res.next())
                sb.append(TagFactory.getRow(res.getInt(1), res.getString(2), res.getString(3), res.getString(4)));

            return TagFactory.getTable(titles, sb.toString());
        } catch (SQLException e) {
            return "No se pudieron recuperar las indicaciones";
        }
    }

    private String getPrecios(String mueble) {
        String query = "CALL GET_PRECIOS_MUEBLE (?)";

        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, mueble);

            res = ps.executeQuery();

            res.next();
            return res.getString(1) + "_" + res.getString(2);

        } catch (SQLException e) {
            return "No se pudieron recuperar el precio y coste del mueble";
        }
    }

    private String getTablaPiezas() {
        String query = "CALL GET_PIEZAS ()";
        try {
            StringBuilder sb = new StringBuilder();
            res = getPrepareStatement(query).executeQuery();

            String[] titles = { "  ", "Tipo de la Pieza", "Costo de la Pieza", "Cantidad en Existencias", "Estado" };

            while (res.next())
                sb.append(TagFactory.getRow(res.getInt(1), String.valueOf(res.getInt(2)), res.getString(3),
                        String.valueOf(res.getDouble(4)), res.getString(5), res.getString(6)));

            return TagFactory.getTable(titles, sb.toString());
        } catch (SQLException e) {
            return "No se pudieron recuperar la tabla de piezas";
        }
    }

    private String getCreados() {
        String query = "CALL GET_CREADOS ()";
        try {
            StringBuilder sb = new StringBuilder();
            res = getPrepareStatement(query).executeQuery();

            String[] titles = { "  ", "Tipo de Mueble", "Ensamblador", "Fecha de Ensamble", "Costo Total", "Precio" };

            while (res.next())
                sb.append(TagFactory.getRow(2, res.getString(1), res.getString(2), res.getString(3),
                        Corroborador.getDate(res.getDate(4)), String.valueOf(res.getDouble(5)),
                        String.valueOf(res.getString(6))));

            return TagFactory.getTable(titles, sb.toString());
        } catch (SQLException e) {
            return "No se pudieron recuperar la tabla de muebles";
        }
    }

    private String addPiezas(HttpServletRequest request) {
        String query = "CALL ADD_PIEZAS (?, ?, ?)";
        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, Corroborador.parseAlfanumericString(request.getParameter("pieza")));
            ps.setDouble(2, Corroborador.parseUnsignedDouble(request.getParameter("costo")));
            ps.setInt(3, Corroborador.parseUnsignedInt(request.getParameter("cantidad")));

            res = ps.executeQuery();

            res.next();
            return res.getString(1);
        } catch (SQLException e) {
            return "Hay problemas en la bodega, no se pueden añadir piezas en este momento";
        }
    }

    private String crearPieza(HttpServletRequest request) {
        String query = "CALL CREAR_PIEZA (?, ?, ?)";
        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, Corroborador.parseAlfanumericString(request.getParameter("pieza")));
            ps.setDouble(2, Corroborador.parseUnsignedDouble(request.getParameter("costo")));
            ps.setInt(3, Corroborador.parseUnsignedInt(request.getParameter("cantidad")));

            res = ps.executeQuery();

            res.next();
            return res.getString(1);
        } catch (SQLException e) {
            return "Hay problemas en la bodega, no se pueden añadir piezas en este momento";
        }
    }

    private String actualizarPieza(HttpServletRequest request) {
        String query = "CALL UPDATE_PIEZA (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, Corroborador.parseAlfanumericString(request.getParameter("pieza")));
            ps.setDouble(2, Corroborador.parseUnsignedDouble(request.getParameter("costo")));
            ps.setString(3, Corroborador.parseAlfanumericString(request.getParameter("pieza_n")));
            ps.setDouble(4, Corroborador.parseUnsignedDouble(request.getParameter("costo_n")));
            ps.setInt(5, Corroborador.parseUnsignedInt(request.getParameter("cantidad_n")));

            res = ps.executeQuery();

            res.next();
            return res.getString(1);
        } catch (SQLException e) {
            return "Hay problemas en la bodega, no se pueden modificar piezas en este momento";
        }
    }

    private String eliminarPieza(HttpServletRequest request) {
        String query = "CALL DEL_PIEZA (?, ?)";
        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, Corroborador.parseAlfanumericString(request.getParameter("pieza")));
            ps.setDouble(2, Corroborador.parseUnsignedDouble(request.getParameter("costo")));

            res = ps.executeQuery();

            res.next();
            return res.getString(1);
        } catch (SQLException e) {
            return "Hay problemas en la bodega, no se pueden eliminar piezas en este momento";
        }
    }

    private String getCostosOpciones(String pieza) {
        String query = "SELECT costo FROM MATERIA_PRIMA WHERE tipo = '" + Corroborador.parseAlfanumericString(pieza)
                + "'";
        return getOpciones(query);
    }

    private String getCantidad(HttpServletRequest request) {
        String query = "SELECT cantidad FROM MATERIA_PRIMA WHERE tipo = ? AND costo = ?";
        try {
            PreparedStatement ps = getPrepareStatement(query);

            ps.setString(1, Corroborador.parseAlfanumericString(request.getParameter("pieza")));
            ps.setDouble(2, Corroborador.parseUnsignedDouble(request.getParameter("costo")));

            res = ps.executeQuery();

            res.next();
            return res.getString(1);
        } catch (SQLException e) {
            return "Hay problemas en la bodega, no se puedo recuperar la cantidad acutal.";
        }
    }
}

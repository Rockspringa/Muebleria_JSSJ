package edu.queries;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.conn.Connect;
import edu.general.Corroborador;
import edu.general.TagFactory;

@WebServlet(name = "reportes", urlPatterns = { "/administracion/reportes" })
public class Reportes extends Connect {

    private ResultSet res = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String salida = "";

        createSession();

        try {
            switch (Corroborador.parseAlfanumericString(req.getParameter("opcion"))) {
                case "Reporte de ventas":
                    salida = getTableVentas(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte de devoluciones":
                    salida = getTableDevoluciones(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte de ganancias":
                    salida = getTableGanancias(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte del vendedor con mas ventas":
                    salida = getTableUsuarioVentas(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte del vendedor con mas ganancias":
                    salida = getTableUsuarioGanancias(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte del mueble mas vendido":
                    salida = getTableMuebleMas(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte del mueble menos vendido":
                    salida = getTableMuebleMenos(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                default:
                    salida = "No se pudo recuperar correctamente la opcion.";
            }
        } catch (SQLException e) {
            salida = "No se pudo recuperar el reporte.";
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

    private String getTableVentas(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Factura", "Cliente", "Tipo de mueble", "Precio de venta", "Fecha de la venta" };

        res = getVentas(inicio, final_);

        while (res.next())
            sb.append(TagFactory.getRow(String.valueOf(res.getInt(1)), res.getString(2), res.getString(3),
                    String.valueOf(res.getDouble(4)), Corroborador.getDate(res.getDate(5))));

        return TagFactory.getTable(titles, sb.toString());
    }

    protected ResultSet getVentas(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_VENTAS (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    private String getTableDevoluciones(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Factura", "Cliente", "Tipo de mueble", "Precio de venta", "Fecha de la venta",
                "Fecha de la devolucion", "Perdida para la empresa" };

        res = getDevoluciones(inicio, final_);

        while (res.next())
            sb.append(TagFactory.getRow(String.valueOf(res.getInt(1)), res.getString(2), res.getString(3),
                    String.valueOf(res.getDouble(4)), Corroborador.getDate(res.getDate(5)),
                    Corroborador.getDate(res.getDate(6)), String.valueOf(res.getDouble(7))));

        return TagFactory.getTable(titles, sb.toString());
    }

    protected ResultSet getDevoluciones(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_DEVOLUCIONES (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    private String getTableGanancias(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Tipo de mueble", "Coste total", "Precio de venta", "Ganancia" };

        res = getGanancias(inicio, final_);

        while (res.next())
            sb.append(TagFactory.getRow(res.getString(1), String.valueOf(res.getDouble(2)),
                    String.valueOf(res.getDouble(3)), String.valueOf(res.getDouble(4))));

        res = getGananciasTotales(inicio, final_);

        while (res.next())
            sb.append(TagFactory.getRow("", "", "Total", String.valueOf(res.getDouble(1))));

        return TagFactory.getTable(titles, sb.toString());
    }

    protected ResultSet getGanancias(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_GANANCIAS (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    protected ResultSet getGananciasTotales(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_GANANCIAS_TOTALES (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    private String getTableUsuarioVentas(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Tipo de mueble", "Precio de venta" };

        res = getUsuarioVentas(inicio, final_);

        res.next();
        String caption = "Usuario: " + res.getString(1);

        res = getUsuarioVentasDatos(inicio, final_);

        while (res.next())
            sb.append(TagFactory.getRow(res.getString(1), String.valueOf(res.getDouble(2))));

        return TagFactory.getTableWithCaption(caption, titles, sb.toString());
    }

    protected ResultSet getUsuarioVentas(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_USUARIO_VENTAS (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    protected ResultSet getUsuarioVentasDatos(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_USUARIO_VENTAS_TABLA (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    private String getTableUsuarioGanancias(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Tipo de mueble", "Precio de venta", "Coste total", "Ganancia" };

        res = getUsuarioGanancias(inicio, final_);

        res.next();
        String caption = "Usuario: " + res.getString(1) + "        Ganancias: " + res.getString(2);

        res = getUsuarioGananciasDatos(inicio, final_);

        while (res.next())
            sb.append(TagFactory.getRow(res.getString(1), String.valueOf(res.getDouble(2)),
                    String.valueOf(res.getDouble(3)), String.valueOf(res.getDouble(4))));

        return TagFactory.getTableWithCaption(caption, titles, sb.toString());
    }

    protected ResultSet getUsuarioGanancias(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_USUARIO_GANANCIAS (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    protected ResultSet getUsuarioGananciasDatos(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_USUARIO_GANANCIAS_TABLA (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }
    
    private String getTableMuebleMas(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Precio de venta", "Coste total", "Fecha de la venta" };

        res = getMuebleMas(inicio, final_);

        res.next();
        String caption = "Mueble: " + res.getString(1);

        res = getMuebleMasDatos(inicio, final_);

        while (res.next())
            sb.append(TagFactory.getRow(res.getString(1), String.valueOf(res.getDouble(2)),
                    Corroborador.getDate(res.getDate(3))));

        return TagFactory.getTableWithCaption(caption, titles, sb.toString());
    }

    protected ResultSet getMuebleMas(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_MUEBLE_MAS (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    protected ResultSet getMuebleMasDatos(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_MUEBLE_MAS_TABLA (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }
    
    private String getTableMuebleMenos(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Precio de venta", "Coste total", "Fecha de la venta" };

        res = getMuebleMenos(inicio, final_);

        res.next();
        String caption = "Mueble: " + res.getString(1);

        res = getMuebleMenosDatos(inicio, final_);

        while (res.next())
            sb.append(TagFactory.getRow(res.getString(1), String.valueOf(res.getDouble(2)),
                    Corroborador.getDate(res.getDate(3))));

        return TagFactory.getTableWithCaption(caption, titles, sb.toString());
    }

    protected ResultSet getMuebleMenos(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_MUEBLE_MENOS (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }

    protected ResultSet getMuebleMenosDatos(Date inicio, Date final_) throws SQLException {
        String query = "CALL REPORTE_MUEBLE_MENOS_TABLA (?, ?)";

        PreparedStatement ps = getPrepareStatement(query);
        ps.setDate(1, inicio);
        ps.setDate(2, final_);

        return ps.executeQuery();
    }
}

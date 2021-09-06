package edu.queries;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.general.Corroborador;

@WebServlet(name = "download", urlPatterns = { "/administracion/download" })
public class DownloadReportes extends Reportes {

    private ResultSet res = null;
    private HttpServletResponse response = null;
    private static File fileToDownload = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createSession();

        try {
            switch (Corroborador.parseAlfanumericString(req.getParameter("opcion"))) {
                case "Reporte de ventas":
                    getCsvVentas(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte de devoluciones":
                    getCsvDevoluciones(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte de ganancias":
                    getCsvGanancias(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte del vendedor con mas ventas":
                    getCsvUsuarioVentas(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte del vendedor con mas ganancias":
                    getCsvUsuarioGanancias(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte del mueble mas vendido":
                    getCsvMuebleMas(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                case "Reporte del mueble menos vendido":
                    getCsvMuebleMenos(Corroborador.getDate(req.getParameter("inicio")),
                            Corroborador.getDate(req.getParameter("final")));
                    break;

                default:
                    ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (fileToDownload != null) {
            response = resp;
            outputFile(fileToDownload);
        }
    }

    private File createFile(String nombre, String contenido) {
        File file = new File(nombre + ".csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file));) {
            bw.write(contenido);
            bw.flush();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void outputFile(File file) {
        try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file))) {

            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + file.getName());
            int data = fileStream.read();

            while (data > -1) {
                response.getOutputStream().write(data);
                data = fileStream.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getCsvVentas(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Factura", "Cliente", "Tipo de mueble", "Precio de venta", "Fecha de la venta" };

        res = getVentas(inicio, final_);

        while (res.next())
            sb.append(getRow(String.valueOf(res.getInt(1)), res.getString(2), res.getString(3),
                    String.valueOf(res.getDouble(4)), Corroborador.getDate(res.getDate(5))));

        exportTable("Reporte de Ventas", titles, sb.toString());
    }

    private void getCsvDevoluciones(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Factura", "Cliente", "Tipo de mueble", "Precio de venta", "Fecha de la venta",
                "Fecha de la devolucion", "Perdida para la empresa" };

        res = getDevoluciones(inicio, final_);

        while (res.next())
            sb.append(getRow(String.valueOf(res.getInt(1)), res.getString(2), res.getString(3),
                    String.valueOf(res.getDouble(4)), Corroborador.getDate(res.getDate(5)),
                    Corroborador.getDate(res.getDate(6)), String.valueOf(res.getDouble(7))));

        exportTable("Reporte de Devoluciones", titles, sb.toString());
    }

    private void getCsvGanancias(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Tipo de mueble", "Coste total", "Precio de venta", "Ganancia" };

        res = getGanancias(inicio, final_);

        while (res.next())
            sb.append(getRow(res.getString(1), String.valueOf(res.getDouble(2)), String.valueOf(res.getDouble(3)),
                    String.valueOf(res.getDouble(4))));

        res = getGananciasTotales(inicio, final_);

        while (res.next())
            sb.append(getRow("", "", "Total", String.valueOf(res.getDouble(1))));

        exportTable("Reporte de Ganancias", titles, sb.toString());
    }

    private void getCsvUsuarioVentas(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Tipo de mueble", "Precio de venta" };

        res = getUsuarioVentas(inicio, final_);

        res.next();
        String caption = "Usuario con mas ventas - " + res.getString(1) + " desde " + Corroborador.getDate(inicio)
                + " hasta " + Corroborador.getDate(final_);

        res = getUsuarioVentasDatos(inicio, final_);

        while (res.next())
            sb.append(getRow(res.getString(1), String.valueOf(res.getDouble(2))));

        exportTable(caption, titles, sb.toString());
    }

    private void getCsvUsuarioGanancias(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Tipo de mueble", "Precio de venta", "Coste total", "Ganancia" };

        res = getUsuarioGanancias(inicio, final_);

        res.next();
        String caption = "Usuario genero mas ganancias - " + res.getString(1) + " - Ganancias de " + res.getDouble(2)
                + " desde " + Corroborador.getDate(inicio) + " hasta " + Corroborador.getDate(final_);

        res = getUsuarioGananciasDatos(inicio, final_);

        while (res.next())
            sb.append(getRow(res.getString(1), String.valueOf(res.getDouble(2)), String.valueOf(res.getDouble(3)),
                    String.valueOf(res.getDouble(4))));

        exportTable(caption, titles, sb.toString());
    }

    private void getCsvMuebleMas(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Precio de venta", "Coste total", "Fecha de la venta" };

        res = getMuebleMas(inicio, final_);

        res.next();
        String caption = "Mueble mas vendido - " + res.getString(1) + " desde " + Corroborador.getDate(inicio)
                + " hasta " + Corroborador.getDate(final_);

        res = getMuebleMasDatos(inicio, final_);

        while (res.next())
            sb.append(getRow(res.getString(1), String.valueOf(res.getDouble(2)), Corroborador.getDate(res.getDate(3))));

        exportTable(caption, titles, sb.toString());
    }

    private void getCsvMuebleMenos(Date inicio, Date final_) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String[] titles = { "Precio de venta", "Coste total", "Fecha de la venta" };

        res = getMuebleMenos(inicio, final_);

        res.next();
        String caption = "Mueble menos vendido - " + res.getString(1) + " desde " + Corroborador.getDate(inicio)
                + " hasta " + Corroborador.getDate(final_);

        res = getMuebleMenosDatos(inicio, final_);

        while (res.next())
            sb.append(getRow(res.getString(1), String.valueOf(res.getDouble(2)), Corroborador.getDate(res.getDate(3))));

        exportTable(caption, titles, sb.toString());
    }

    private String getRow(String... args) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(",");
            } else {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private void exportTable(String nombre, String[] titles, String rows) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < titles.length; i++) {
            sb.append(titles[i]);
            if (i < titles.length - 1) {
                sb.append(",");
            } else {
                sb.append("\n");
            }
        }

        sb.append(rows);

        fileToDownload = createFile(nombre, sb.toString());
    }
}

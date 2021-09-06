package edu.queries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import edu.conn.Connect;
import edu.enums.Esquema;
import edu.general.Corroborador;
import edu.general.TagFactory;
import javax.servlet.annotation.MultipartConfig;

@MultipartConfig
@WebServlet(name = "upload", urlPatterns = { "/administracion/upload" })
public class UploadData extends Connect {

    private ResultSet res = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createSession();

        int i = 1;
        Part part = req.getPart("file");
        InputStream stream = part.getInputStream();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
            String line = in.readLine();

            while (line != null) {
                Esquema sch = Esquema.getEsquema(line);
                if ((sch == Esquema.CLIENTE)
                        ? (sch.matches(line) || Esquema.CLIENTE2.matches(line))
                        : sch.matches(line)) {
                            
                    String result = "";
                    try {
                        switch (sch) {
                            case USUARIO:
                                result = addUsuario(line);
                                break;

                            case PIEZA:
                                result = addPieza(line);
                                break;

                            case MUEBLE:
                                result = addMueble(line);
                                break;

                            case INDICACIONES:
                                result = addEnsamblePiezas(line);
                                break;

                            case ENSAMBLADO:
                                result = addEnsamblado(line);
                                break;

                            case CLIENTE:
                                result = addCliente(line);
                                break;

                            default:
                                result = "No se encontro la accion solicitada.";
                        }

                        sb.append(TagFactory.getLine("Linea " + (i++) + ": " + result));
                    } catch (SQLException e) {
                        sb.append(TagFactory.getErrorLine("Linea " + (i++) + ": " + e.getMessage()));
                    }
                } else {
                    sb.append(TagFactory.getErrorLine("Linea " + (i++) + ": La linea no tiene el patron adecuado."));
                }
                line = in.readLine();
            }
        } catch (IOException e) {
            resp.getWriter().print("Ocurrio un error al leer o cerrar el archivo, intentelo nuevamente.");
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeSession();
        }

        resp.getOutputStream().print(sb.toString());
    }

    private String addUsuario(String line) throws SQLException {
        String query = "INSERT INTO USUARIO VALUES (?, ?, ?, 1)";
        PreparedStatement ps = getPrepareStatement(query);
        String[] params = Esquema.USUARIO.getParams(line);

        ps.setString(1, params[0]);
        ps.setString(2, params[1]);
        ps.setInt(3, Integer.parseInt(params[2]));
        ps.executeUpdate();

        return "Usuario agregado con exito.";
    }

    private String addPieza(String line) throws SQLException {
        String query = "CALL ADD_PIEZA (?, ?)";
        PreparedStatement ps = getPrepareStatement(query);
        String[] params = Esquema.PIEZA.getParams(line);

        ps.setString(1, params[0]);
        ps.setDouble(2, Double.parseDouble(params[1]));
        res = ps.executeQuery();

        res.next();
        return res.getString(1);

    }

    private String addMueble(String line) throws SQLException {
        String query = "INSERT INTO MUEBLE VALUES (?, ?)";
        PreparedStatement ps = getPrepareStatement(query);
        String[] params = Esquema.MUEBLE.getParams(line);

        ps.setString(1, params[0]);
        ps.setDouble(2, Double.parseDouble(params[1]));
        ps.executeUpdate();

        return "Mueble agregado con exito.";
    }

    private String addEnsamblePiezas(String line) throws SQLException {
        String query = "CALL ADD_INDICACION (?, ?, ?)";
        PreparedStatement ps = getPrepareStatement(query);
        String[] params = Esquema.INDICACIONES.getParams(line);

        ps.setString(1, params[0]);
        ps.setString(2, params[1]);
        ps.setInt(3, Integer.parseInt(params[2]));
        res = ps.executeQuery();

        res.next();
        String respuesta = res.getString(1);
        if (respuesta.contains("No existe")) throw new SQLException(respuesta);
        return respuesta;
    }

    private String addEnsamblado(String line) throws SQLException {
        String query = "CALL ENSAMBLAR (?, ?, ?)";
        PreparedStatement ps = getPrepareStatement(query);
        String[] params = Esquema.ENSAMBLADO.getParams(line);

        ps.setString(1, params[0]);
        ps.setString(2, params[1]);
        ps.setDate(3, Corroborador.getDate(params[2]));
        res = ps.executeQuery();

        res.next();
        String respuesta = res.getString(1);
        if (respuesta.contains("No") || respuesta.contains("no"))
            throw new SQLException(respuesta);
        return respuesta;
    }

    private String addCliente(String line) throws SQLException {
        String[] params = Esquema.CLIENTE.getParams(line);
        params = (params == null) ? Esquema.CLIENTE2.getParams(line) : params;
        String query = (params.length == 3)
                ? "INSERT INTO CLIENTE (nombre, nit, direccion) VALUES (?, ?, ?)"
                : "INSERT INTO CLIENTE (nombre, nit, direccion, municipio, departamento) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = getPrepareStatement(query);

        for (int i = 0; i < params.length; i++)
            ps.setString(i + 1, params[0]);

        ps.executeUpdate();

        return "Cliente agregado con exito.";
    }
}

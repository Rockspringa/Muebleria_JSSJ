package edu.queries;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.conn.Connect;
import edu.errores.NegativeNumberException;
import edu.errores.NonAlfanumericStringException;
import edu.general.Corroborador;
import edu.general.TagFactory;

@WebServlet(name = "usuarios", urlPatterns = { "/administracion/usuarios" })
public class Usuarios extends Connect {

    private ResultSet res = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String salida = "";

        createSession();

        try {
            switch (Corroborador.parseAlfanumericString(req.getParameter("option"))) {
                case "crear":
                    salida = crear(
                            Corroborador.parseAlfanumericString(req.getParameter("user")),
                            Corroborador.parseAlfanumericString(req.getParameter("pass")),
                            Corroborador.parseUnsignedInt(req.getParameter("tipo"))
                        );
                    break;

                case "cancelar":
                    salida = cancelar(Corroborador.parseAlfanumericString(req.getParameter("user")));
                    break;

                case "verificar":
                    salida = verificar(Corroborador.parseAlfanumericString(req.getParameter("user")));
                    break;

                case "usuarios":
                    salida = getUsuarios();
                    break;

                case "activos":
                    salida = getUsuariosActivos();
                    break;

                case "actualizar":
                    salida = update(
                            Corroborador.parseAlfanumericString(req.getParameter("user")),
                            req.getParameter("pass"),
                            req.getParameter("tipo")
                        );
                    break;

                case "datos":
                    salida = getDatos(Corroborador.parseAlfanumericString(req.getParameter("user")));
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

    private String crear(String user, String pass, int tipo) throws SQLException {
        String query = "INSERT INTO USUARIO VALUES (?, ?, ?, 1)";
        PreparedStatement ps = getPrepareStatement(query);

        ps.setString(1, user);
        ps.setString(2, pass);
        ps.setInt(3, tipo);
        ps.executeUpdate();

        return "Usuario agregado con exito.";
    }

    private String update(String user, String pass, String tipo) throws SQLException {
        PreparedStatement ps = null;
        String query = "";
        if (pass == "") {
            if (tipo == null) return "No realizo ningun cambio";
            else {
                query = "UPDATE USUARIO SET tipo = ? WHERE nombre_usuario = ?";
                ps = getPrepareStatement(query);
                ps.setInt(1, Corroborador.parseUnsignedInt(tipo));
                ps.setString(2, user);
            }
        } else {
            if (tipo == "") {
                query = "UPDATE USUARIO SET contraseña = ? WHERE nombre_usuario = ?";
                ps = getPrepareStatement(query);
                ps.setString(1, Corroborador.parseAlfanumericString(pass));
                ps.setString(2, user);
            } else {
                query = "UPDATE USUARIO SET contraseña = ?, tipo = ? WHERE nombre_usuario = ?";
                ps = getPrepareStatement(query);
                ps.setString(1, Corroborador.parseAlfanumericString(pass));
                ps.setInt(2, Corroborador.parseUnsignedInt(tipo));
                ps.setString(3, user);
            }
        }
        
        ps.executeUpdate();

        return "Usuario modificado con exito.";
    }

    private String getUsuarios() throws SQLException {
        StringBuilder sb = new StringBuilder();
        String query = "SELECT nombre_usuario FROM USUARIO";
        res = getPrepareStatement(query).executeQuery();

        while (res.next())
            sb.append(TagFactory.getOptions(res.getString(1)));

        return sb.toString();
    }

    private String cancelar(String user) throws SQLException {
        String query = "UPDATE USUARIO SET activo = 0 WHERE nombre_usuario = ?";
        PreparedStatement ps = getPrepareStatement(query);
        ps.setString(1, user);

        ps.executeUpdate();

        return "El usuario se cancelo exitosamente.";
    }

    private String getUsuariosActivos() throws SQLException {
        StringBuilder sb = new StringBuilder();
        String query = "SELECT nombre_usuario FROM USUARIO WHERE activo = 1";
        res = getPrepareStatement(query).executeQuery();

        while (res.next())
            sb.append(TagFactory.getOptions(res.getString(1)));

        return sb.toString();
    }

    private String getDatos(String user) throws SQLException {
        String query = "SELECT tipo, activo FROM USUARIO WHERE nombre_usuario LIKE ?";
        PreparedStatement ps = getPrepareStatement(query);
        ps.setString(1, user);

        res = ps.executeQuery();

        res.next();

        return res.getInt(1) + "_" + res.getBoolean(2);
    }

    private String verificar(String user) {
        String query = "SELECT nombre_usuario FROM USUARIO WHERE nombre_usuario = ?";
        try {
            PreparedStatement ps = getPrepareStatement(query);
            ps.setString(1, user);

            res = ps.executeQuery();

            return (res.next()) ? "exists" : "open";
        } catch (SQLException e) {
            return "exists";
        }
    }
}

package edu.general;

import edu.conn.Connect;
import edu.enums.Index;
import edu.obj.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GetUserArea", urlPatterns = { "/user_area" })
public class UserArea extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        String toReturn = "";

        if (Connect.createSession()) {
            try {
                toReturn = getUserVerification(user, pass);
                if (toReturn.length() > 1) {
                    Usuario usuario = Usuario.construct(user, pass, Integer.parseInt(toReturn.substring(2)));
                    HttpSession session = request.getSession();

                    session.setAttribute("user", usuario);
                    
                    toReturn = switch (usuario.getNivel()) {
                        case 1 -> Index.FABRICA.getUrl();
                        case 2 -> Index.VENTAS.getUrl();
                        case 3 -> Index.ADMIN.getUrl();
                        default -> Index.LOGIN.getUrl();
                    };
                } else {
                    toReturn = "El nombre de usuario o la contraseña es incorrecto";
                }
            } catch (SQLException e) {
                toReturn = "Hubo un error al verificar su usuario";
            }

            Connect.closeSession();
        } else {
            toReturn = "Hubo un error intente mas tarde.";
        }

        if (!toReturn.equals("")) out.println((toReturn));
    }

    private static String getUserVerification(String user, String pass) throws SQLException {
        String query = "CALL IS_USUARIO (?, ?)";
        PreparedStatement ps = Connect.getPrepareStatement(query);

        ps.setString(1, user);
        ps.setString(2, pass);

        ResultSet res = ps.executeQuery();

        res.next();
        return res.getString(1);
    }

    public static String redirectToArea(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, String areaRequesting) throws IOException {

        session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/");
            return "No se ha logeado";
        } else {
            Usuario usuario = (Usuario) session.getAttribute("user");

            if (usuario.isCancelado()) {
                response.sendRedirect("/");
                return "El usuario ha sido cancelado";
            } else {
                switch (usuario.getNivel()) {
                    case 1:
                        if (!areaRequesting.contains("Fabrica"))
                            response.sendRedirect(Index.FABRICA.getUrl());
                        break;

                    case 2:
                        if (!areaRequesting.contains("Ventas"))
                            response.sendRedirect(Index.VENTAS.getUrl());
                        break;

                    case 3:
                        if (!areaRequesting.contains("Admin"))
                            response.sendRedirect(Index.ADMIN.getUrl());
                        break;

                    default:
                        response.sendRedirect(Index.LOGIN.getUrl());
                };
            }
        }
        return "";
    }
}

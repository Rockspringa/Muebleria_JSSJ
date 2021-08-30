package edu.obj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Usuario {
    private final String username;
    private final String pass;
    private final int nivel;
    private boolean cancelado = false;

    private Usuario(String user, String pass, int nivel) {
        this.username = user;
        this.pass = pass;
        this.nivel = nivel;
    }

    public static Usuario construct(String user, String pass, int nivel) throws PatternSyntaxException {
        Usuario out = null;
        Pattern pat = Pattern.compile("^\\w+$");
        Matcher matchUser = pat.matcher(user);
        Matcher matchPass = pat.matcher(pass);

        if (matchUser.matches() && matchPass.matches() && nivel < 4 && nivel > 0) {
            out = new Usuario(user, pass, nivel);
        } else {
            throw new PatternSyntaxException("Existen simbolos en las cadenas", "", -1);
        } return out;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

    public int getNivel() {
        return nivel;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void cancelar() {
        if (!this.cancelado) this.cancelado = !this.cancelado;
    }
}
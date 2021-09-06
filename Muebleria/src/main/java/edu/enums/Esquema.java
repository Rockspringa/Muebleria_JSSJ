package edu.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Esquema {
    USUARIO("USUARIO", "\\(\\\"([\\w ]{1,40})\\\",[ ]?+\\\"([\\w ]{1,40})\\\",[ ]?([1-3])\\)", 3),
    PIEZA("PIEZA", "\\(\\\"([\\w ]{1,40})\\\",[ ]?([\\d]+([.][\\d]+)?)\\)", 2),
    MUEBLE("MUEBLE", "\\(\\\"([\\w ]{1,40})\\\",[ ]?([\\d]+([.][\\d]+)?)\\)", 2),
    INDICACIONES("ENSAMBLE_PIEZAS", "\\(\\\"([\\w ]{1,40})\\\",[ ]?\\\"([\\w ]{1,40})\\\",[ ]?([\\d])\\)", 3),
    ENSAMBLADO("ENSAMBLAR_MUEBLE", "\\(\\\"([\\w ]{1,40})\\\",[ ]?\\\"?([\\w ]{1,40})\\\"?,[ ]?\\\"?([\\d]{1,2}/[\\d]{1,2}/[\\d]{4})\\\"?\\)", 3),
    CLIENTE("CLIENTE", "\\(\\\"([\\w ]{1,40})\\\",[ ]?\\\"([\\w]{6,7})\\\",[ ]?\\\"([\\w -]{1,40})\\\",[ ]?\\\"([\\w ]{1,25})\\\",[ ]?\\\"([\\w ]{1,25})\\\"\\)", 5),
    CLIENTE2("CLIENTE", "\\(\\\"([\\w ]{1,40})\\\",[ ]?\\\"([\\w]{6,7})\\\",[ ]?\\\"([\\w -]{1,40})\\\"\\)", 3);

    private final String name;
    private final Pattern namePat;
    private final Pattern regex;
    private final int groups;

    private Esquema(String name, String regex, int groups) {
        this.name = name;
        this.namePat = Pattern.compile("^[ ]*" + name + "\\(");
        this.regex = Pattern.compile(regex);
        this.groups = groups;
    }

    public String getName() {
        return this.name;
    }

    public boolean matches(String line) {
        Matcher match = regex.matcher(line);
        return match.find();
    }

    public String[] getParams(String line) {
        Matcher match = regex.matcher(line);
        String[] list = new String[groups];
        if (match.find()) {
            for (int i = 1; i <= groups; i++) {
                list[i - 1] = (match.group(i));
            }
            return list;
        } return null;
    }

    public static Esquema getEsquema(String line) {
        for (Esquema esq : Esquema.values()) {
            if (esq.namePat.matcher(line).find()) return esq;
        } return null;
    }
}

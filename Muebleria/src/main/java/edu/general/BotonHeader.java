package edu.general;

public class BotonHeader {
    private final String boton;
    private final String link;
    private String classes;
    
    public BotonHeader(String boton, String link, String classes) {
        this.boton = boton;
        this.link = link;
        this.classes = classes;
    }

    public String getNombre() {
        return boton;
    }

    public String getLink() {
        return link;
    }

    public String getClasses() {
        return classes;
    }

    public void addClass(String classes) {
        this.classes = this.classes + " " + classes;
    }
}

package edu.enums;

public enum Index {
    LOGIN("/"),
    FABRICA("/fabrica/construir.jsp"),
    VENTAS("/ventas/escoger.jsp"),
    ADMIN("/administracion/import.jsp");
    
    private final String url;
    
    private Index(String url) {
        this.url = url;
    }
    
    public String getUrl() { return this.url; }
}

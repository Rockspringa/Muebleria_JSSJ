package edu.enums;

public enum Index {
    LOGIN("/"),
    FABRICA("/fabrica/construir.jsp"),
    VENTAS("/ventas/..."),
    ADMIN("/administracion/...");
    
    private final String url;
    
    private Index(String url) {
        this.url = url;
    }
    
    public String getUrl() { return this.url; }
}

package edu.general;

public class TagFactory {
    
    public static String getRow(String ...data) {
        return getRow(false, data);
    }
    
    public static String getRow(boolean warning, String ...data) {
        StringBuilder sb = new StringBuilder(
                "<tr" + (warning ? " class=\"table-warning" : "") + "\">"
        );
        
        for (String data1 : data) {
            sb.append("<td>");
            sb.append(data1);
            sb.append("</td>");
        }
        sb.append("</tr>");

        return sb.toString();
    }
    
    public static String getTable(String[] titles, String ...rows) {
        StringBuilder sb = new StringBuilder("<table><thead><tr>");

        for (String row : rows) {
            sb.append("<th>");
            sb.append(row);
            sb.append("</th>");
        }

        sb.append("</tr></thead>");
        sb.append("<tbody>");

        for (String row : rows) {
            sb.append(row);
        }

        sb.append("</tbody>");
        sb.append("</table>");

        return sb.toString();
    }

    public static String getSquaredP(String title, String color) {
        StringBuilder sb = new StringBuilder("<button type=\"button\" class=\"btn " + color + "\">");
        String imageSrc = (color.contains("danger"))
                ? "/resources/images/mueble-agotado.png"
                : "/resources/images/mueble.png";
        
        sb.append("<img src=\"");
        sb.append(imageSrc);
        sb.append("\"><span>");
        sb.append(title);
        sb.append("</span></button>");

        return sb.toString();
    }

    public static String getListNum(String item, String badge, boolean notDanger) {
        StringBuilder sb = new StringBuilder("<li class=\"list-group-item d-flex "
                + "justify-content-between align-items-center "
                + (notDanger ? "list-group-item-success" : "list-group-item-danger") + "\">");
        
        sb.append(item);
        sb.append("<span class=\"badge badge-" + (notDanger ? "success" : "danger") + " badge-pill\">");
        sb.append(badge);
        sb.append("</span></li>");

        return sb.toString();
    }
}

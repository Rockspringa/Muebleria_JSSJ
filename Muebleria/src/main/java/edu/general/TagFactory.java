package edu.general;

public class TagFactory {
    
    public static String getRow(String ...data) {
        return getRow(2, data);
    }
    
    public static String getRow(int flag, String ...data) {
        StringBuilder sb = new StringBuilder("<tr class=\"table-"
                + (switch (flag) { case 0  -> "danger"; case 1  -> "warning"; default -> "success"; })
                + "\">"
        );
        
        for (String data1 : data) {
            sb.append("<td>");
            sb.append(data1);
            sb.append("</td>");
        }
        sb.append("</tr>");

        return sb.toString();
    }
    
    public static String getTable(String[] titles, String rows) {
        StringBuilder sb = new StringBuilder("<table class=\"table\"><thead class=\"thead-light\"><tr>");

        for (String th : titles) {
            sb.append("<th>");
            sb.append(th);
            sb.append("</th>");
        }

        sb.append("</tr></thead>");
        sb.append("<tbody>");

        sb.append(rows);

        sb.append("</tbody>");
        sb.append("</table>");

        return sb.toString();
    }

    public static String getTableWithCaption(String caption, String[] titles, String rows) {
        StringBuilder sb = new StringBuilder("<table class=\"table\">");
        sb.append("<caption class=\"bg-light text-dark\">" + caption + "</caption>");
        sb.append("<thead class=\"thead-light\"><tr>");

        for (String th : titles) {
            sb.append("<th>");
            sb.append(th);
            sb.append("</th>");
        }

        sb.append("</tr></thead>");
        sb.append("<tbody>");

        sb.append(rows);

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

    public static String getListNum(String item, String badge, int cant) {
        String color = switch (cant) { case 0  -> "danger"; case 1  -> "warning"; default -> "success"; };
        StringBuilder sb = new StringBuilder("<li class=\"list-group-item d-flex justify-content"
                + "-between align-items-center list-group-item-" + color + "\">");
        
        sb.append(item);
        sb.append("<span class=\"badge badge-" + color + " badge-pill\">");
        sb.append(badge);
        sb.append("</span></li>");

        return sb.toString();
    }

    public static String getListMuebles(String mueble) {
        StringBuilder sb = new StringBuilder("<li class=\"list-group-item d-flex justify-content"
                + "-between align-items-center list-group-item-primary\">");
        
        sb.append(mueble);
        sb.append("</li>");

        return sb.toString();
    }

    public static String getOptions(String opt) {
        return "<option class=\"del\">" + opt + "</option>";
    }

    public static String getLine(String line) {
        return "<li class=\"list-group-item list-group-item-success\">" + line + "</li>";
    }

    public static String getErrorLine(String line) {
        return "<li class=\"list-group-item list-group-item-danger\">" + line + "</li>";
    }
}

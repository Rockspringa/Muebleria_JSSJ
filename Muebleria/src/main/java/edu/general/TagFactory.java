package edu.general;

public class TagFactory {
    
    public static String getRow(String ...data) {
        return getRow(false, data);
    }
    
    public static String getRow(boolean warning, String ...data) {
        StringBuilder sb = new StringBuilder(
                "<tr" + (warning ? " class=\"table-warning" : "") + "\">"
        );
        
        for (int i = 0; i < data.length; i++) {
            sb.append("<td>");
            sb.append(data[i]);
            sb.append("</td>");
        }
        sb.append("</tr>");

        return sb.toString();
    }
    
    public static String getTable(String[] titles, String ...rows) {
        StringBuilder sb = new StringBuilder("<table><thead><tr>");

        for (int i = 0; i < rows.length; i++) {
            sb.append("<th>");
            sb.append(rows[i]);
            sb.append("</th>");
        }

        sb.append("</tr></thead>");
        sb.append("<tbody>");

        for (int i = 0; i < rows.length; i++) {
            sb.append(rows[i]);
        }

        sb.append("</tbody>");
        sb.append("</table>");

        return sb.toString();
    }
}

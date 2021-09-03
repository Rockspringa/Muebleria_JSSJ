package edu.general;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import edu.errores.NegativeNumberException;
import edu.errores.NonAlfanumericStringException;

public class Corroborador {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static double parseUnsignedDouble(String s) {
        double d = Double.parseDouble(s);

        if (d < 0)
            throw new NegativeNumberException();

        return d;
    }

    public static int parseUnsignedInt(String s) {
        int i = Integer.parseInt(s);

        if (i < 0)
            throw new NegativeNumberException();

        return i;
    }

    public static String parseAlfanumericString(String s) {
        Pattern r = Pattern.compile("^[\\w ]+$");
        Matcher m = r.matcher(s);

        if (!m.matches())
            throw new NonAlfanumericStringException();

        return s;
    }

    public static String getDate(Date fecha) {
        LocalDate date = fecha.toLocalDate();
        return date.format(formatter);
    }
}

package io.github.cakilgan.clogger.format;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CLDate {
    public static final String FORMAT_STR = "yyyy$MM/dd/hh/mm;::;hh.mm.ss.SSSXXX";
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy$MM/dd/hh/mm;::;hh.mm.ss.SSSXXX");
    public static String getDate(){
        return format.format(new Date());
    }
    public static String getDate(String format){
        return new SimpleDateFormat(format).format(new Date());
    }
}

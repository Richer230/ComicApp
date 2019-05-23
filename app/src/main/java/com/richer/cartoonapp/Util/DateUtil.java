package com.richer.cartoonapp.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public int getYear(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return Integer.parseInt(format.format(new java.util.Date(time)));
    }
    public int getMonth(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return Integer.parseInt(format.format(new java.util.Date(time)));
    }
    public int getDay(long time){
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return Integer.parseInt(format.format(new java.util.Date(time)));
    }

}

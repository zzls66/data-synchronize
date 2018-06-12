package com.shbaoyuantech.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String accessYYYYMMDDataStr(String origin){
        SimpleDateFormat date2Str = new SimpleDateFormat("yyyyMMdd");
        Date date = transStr2Date(origin);
        return date2Str.format(date);
    }

    public static Date transStr2Date(String origin){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(origin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

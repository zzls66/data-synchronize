package com.shbaoyuantech.commons;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat dateFullFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String accessYYYYMMDDataStr(String origin){
        if (StringUtils.isEmpty(origin)) {
            return null;
        }
        return origin.substring(0, 10).replace("-", "");
    }

    public static Date transStr2Date(String origin) {
        if (StringUtils.isEmpty(origin)) {
            return null;
        }
        try {
            return dateFullFormat.parse(origin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

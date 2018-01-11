package com.example.hunter.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    static private DateUtils mInstance;

    static public DateUtils getInstance() {
        if (mInstance == null) {
            mInstance = new DateUtils();
        }
        return mInstance;
    }

    public String getNowDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    public String getNowDateFormat(String format) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }
}

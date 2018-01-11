package com.example.hunter.utils;

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

    public String getNowDateFormat() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}

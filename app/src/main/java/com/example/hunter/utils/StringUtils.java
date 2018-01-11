package com.example.hunter.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    static private StringUtils mInstance;

    static public StringUtils getInstance() {
        if (mInstance == null) {
            mInstance = new StringUtils();
        }
        return mInstance;
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}

package com.ex.unamic.pec.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Unamic on 9/17/2016.
 */
public class Utils {
    public static Date convertStringToFullDate(String date)
    {
        //String dateString = "03/26/2012 11:49:00 AM";
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        try {
            Date convertedDate = dateFormat.parse(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date convertStringToDate(String date)
    {
        //String dateString = "03/26/2012 11:49:00 AM";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date convertedDate = dateFormat.parse(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

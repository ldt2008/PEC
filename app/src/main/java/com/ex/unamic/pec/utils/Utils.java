package com.ex.unamic.pec.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Unamic on 9/17/2016.
 */
public class Utils {
    public static String convertDateToFullDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());
        String convertedDate = dateFormat.format(date);
        return convertedDate;
    }

    public static Date convertStringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date convertedDate = dateFormat.parse(date);
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateFormatMonthYear(String date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
            try {
                Date convertedDate = dateFormat.parse(date);
                if (convertedDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(convertedDate);

                    SimpleDateFormat formatDate = new SimpleDateFormat("MMMM, yyyy");
                    String month_name = formatDate.format(calendar.getTime());
                    return month_name;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }
}

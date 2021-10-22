package com.magma.lemonade.models;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateTime {

    private String tag = "DATETIME";

    public String getCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CEST"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public int getDateDifference(String start1, Locale locale){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date start = simpleDateFormat.parse(start1);
            Date end = simpleDateFormat.parse(getCurrentDate());

            Calendar cal = Calendar.getInstance(locale);

            cal.setTime(start);
            long startTime = cal.getTimeInMillis();

            cal.setTime(end);
            long endTime = cal.getTimeInMillis();

            TimeZone timezone = cal.getTimeZone();
            int offsetStart = timezone.getOffset(startTime);
            int offsetEnd = timezone.getOffset(endTime);
            int offset = offsetEnd - offsetStart;

            long difference = endTime - startTime + offset;

            int days = (int) TimeUnit.MILLISECONDS.toDays(difference);

            return days;
        } catch (ParseException e) {
            Log.d(tag, e.toString() + " error time");
            return 0;
        }
    }
}

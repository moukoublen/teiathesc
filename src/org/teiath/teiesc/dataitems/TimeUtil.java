package org.teiath.teiesc.dataitems;


import android.annotation.SuppressLint;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@SuppressLint("DefaultLocale")
public class TimeUtil
{
    public static long toUTCTime(String t)
    {
        Calendar loc = new GregorianCalendar();
        loc.setTimeInMillis(Time.valueOf(t).getTime());
        
        Calendar utc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(0);
        
        set(utc, loc, Calendar.HOUR_OF_DAY);
        set(utc, loc, Calendar.MINUTE);
        set(utc, loc, Calendar.SECOND);
        set(utc, loc, Calendar.MILLISECOND);

        return utc.getTimeInMillis();
    }


    public static String formatUTCTime(long UTCtime)
    {
        Calendar utc = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(UTCtime);
        
        return String.format("%02d:%02d", utc.get(Calendar.HOUR_OF_DAY), utc.get(Calendar.MINUTE));
    }
    
    private static void set(Calendar dest, Calendar from, int field)
    {
        dest.set(field, from.get(field));
    }
    

}

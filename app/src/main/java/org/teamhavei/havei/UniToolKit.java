/**
 * @author lbc
 * @last_modifier lbc
 */
package org.teamhavei.havei;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UniToolKit {

    public static final String TODO_NOTIFICATION_CHANNEL_ID = "HaveI_Notification_todo";
    public static final String HABIT_NOTIFICATION_CHANNEL_ID = "HaveI_Notification_habit";



    public static final String EVENT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String EVENT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String EVENT_TIME_FORMAT = "HH:mm";

    public static String eventDatetimeFormatter(Date dateTime){
        SimpleDateFormat datetimeSDF = new SimpleDateFormat(EVENT_DATETIME_FORMAT);
        return datetimeSDF.format(dateTime);
    }

    public static String eventTimeFormatter(Date time){
        SimpleDateFormat timeSDF = new SimpleDateFormat(EVENT_TIME_FORMAT);
        return timeSDF.format(time);
    }

    public static String eventDateFormatter(Date date){
        SimpleDateFormat dateSDF = new SimpleDateFormat(EVENT_DATE_FORMAT);
        return dateSDF.format(date);
    }

    public static Date eventDatetimeParser(String sDateTime){
        SimpleDateFormat datetimeSDF = new SimpleDateFormat(EVENT_DATETIME_FORMAT);
        Date datetime = null;
        try{
            datetime = datetimeSDF.parse(sDateTime);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return datetime;
    }

    public static Date eventDateParser(String sDate){
        SimpleDateFormat dateSDF = new SimpleDateFormat(EVENT_DATE_FORMAT);
        Date date = null;
        try {
            date = dateSDF.parse(sDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public static Date eventTimeParser(String sTime){
        SimpleDateFormat timeSDF = new SimpleDateFormat(EVENT_TIME_FORMAT);
        Date time = null;
        try{
            time = timeSDF.parse(sTime);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return time;
    }
}

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

    public static final String PREF_SETTINGS = "settings";
    public static final String PREF_SETTINGS_USER_NAME = "user_name";

    public static final int TAG_TYPE_EVENT = 0;
    public static final int TAG_TYPE_BOOKKEEP = 1;

    public static final String EVENT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String EVENT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String EVENT_TIME_FORMAT = "HH:mm";

    public static final SimpleDateFormat datetimeSDF = new SimpleDateFormat(EVENT_DATETIME_FORMAT);
    public static final SimpleDateFormat timeSDF = new SimpleDateFormat(EVENT_TIME_FORMAT);
    public static final SimpleDateFormat dateSDF = new SimpleDateFormat(EVENT_DATE_FORMAT);

    public static String eventDatetimeFormatter(Date dateTime) {
        return datetimeSDF.format(dateTime);
    }

    public static String eventTimeFormatter(Date time) {
        return timeSDF.format(time);
    }

    public static String eventDateFormatter(Date date) {
        return dateSDF.format(date);
    }

    public static Date eventDatetimeParser(String sDateTime) {
        Date datetime = null;
        try {
            datetime = datetimeSDF.parse(sDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datetime;
    }

    public static Date eventDateParser(String sDate) {
        Date date = null;
        try {
            date = dateSDF.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date eventTimeParser(String sTime) {
        SimpleDateFormat timeSDF = new SimpleDateFormat(EVENT_TIME_FORMAT);
        Date time = null;
        try {
            time = timeSDF.parse(sTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}

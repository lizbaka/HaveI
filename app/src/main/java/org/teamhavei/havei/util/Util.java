package org.teamhavei.havei.util;

import org.teamhavei.havei.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static final String BASIC_NOTIFICATION_CHANNEL_ID = "Basic_Notification_habit";
    public static final String TODO_NOTIFICATION_CHANNEL_ID = "HaveI_Notification_todo";
    public static final String HABIT_NOTIFICATION_CHANNEL_ID = "HaveI_Notification_habit";

    public static final String PREF_SETTINGS = "settings";
    public static final String PREF_SETTINGS_USER_NAME = "user_name";
    public static final String PREF_FIRST_RUN = "first_run";
    public static final String PREF_BUDGET = "budget";
    public static final String PREF_FIRST_RUN_DATE = "first_run_date";

    public static final int TAG_TYPE_EVENT = 0;
    public static final int TAG_TYPE_BOOKKEEP = 1;

    public static final int BOOKKEEP_TAG_EXPENDITURE = 0;
    public static final int BOOKKEEP_TAG_INCOME = 1;

    public static final int DEFAULT_ACCOUNT_ID = 0;

    public static final String EVENT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String EVENT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String EVENT_TIME_FORMAT = "HH:mm";
    public static final String EVENT_YEAR_MONTH_FORMAT = "yyyy-MM";

    public static final SimpleDateFormat datetimeSDF = new SimpleDateFormat(EVENT_DATETIME_FORMAT);
    public static final SimpleDateFormat timeSDF = new SimpleDateFormat(EVENT_TIME_FORMAT);
    public static final SimpleDateFormat dateSDF = new SimpleDateFormat(EVENT_DATE_FORMAT);
    public static final SimpleDateFormat yearMonthSDF = new SimpleDateFormat(EVENT_YEAR_MONTH_FORMAT);

    public static String eventDatetimeFormatter(Date dateTime) {
        return datetimeSDF.format(dateTime);
    }

    public static String eventTimeFormatter(Date time) {
        return timeSDF.format(time);
    }

    public static String eventDateFormatter(Date date) {
        return dateSDF.format(date);
    }

    public static String eventYearMonthFormatter(Date date) {
        return yearMonthSDF.format(date);
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
        Date time = null;
        try {
            time = timeSDF.parse(sTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static Date eventYearMonthParser(String sYearMonth) {
        Date yearMonth = null;
        try {
            yearMonth = yearMonthSDF.parse(sYearMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yearMonth;
    }

    public static Date unionDateTime(Date date, Date time) {
        String sDate = eventDateFormatter(date);
        String sTime = eventTimeFormatter(time);
        String sDateTime = String.format("%s %s", sDate, sTime);
        return eventDatetimeParser(sDateTime);
    }

    public static int getGreetingTimeId() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 5 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            return R.string.greeting_morning;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 12 && calendar.get(Calendar.HOUR_OF_DAY) < 14) {
            return R.string.greeting_noon;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 14 && calendar.get(Calendar.HOUR_OF_DAY) < 18) {
            return R.string.greeting_afternoon;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 18 && calendar.get(Calendar.HOUR_OF_DAY) < 22) {
            return R.string.greeting_evening;
        } else {
            return R.string.greeting_midnight;
        }
    }

    public static int getGreetingSecId() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 5 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            return R.string.greeting_morning_secondary;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 12 && calendar.get(Calendar.HOUR_OF_DAY) < 14) {
            return R.string.greeting_noon_secondary;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 14 && calendar.get(Calendar.HOUR_OF_DAY) < 18) {
            return R.string.greeting_afternoon_secondary;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 18 && calendar.get(Calendar.HOUR_OF_DAY) < 22) {
            return R.string.greeting_evening_secondary;
        } else {
            return R.string.greeting_midnight_secondary;
        }
    }

    public static int getGreetingIconId() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 5 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            return R.drawable.ic_planet;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 12 && calendar.get(Calendar.HOUR_OF_DAY) < 14) {
            return R.drawable.ic_hs_sun;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 14 && calendar.get(Calendar.HOUR_OF_DAY) < 18) {
            return R.drawable.ic_hs_paper_airplane;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 18 && calendar.get(Calendar.HOUR_OF_DAY) < 22) {
            return R.drawable.ic_cs_astronomy;
        } else {
            return R.drawable.ic_hs_moon1;
        }
    }

}

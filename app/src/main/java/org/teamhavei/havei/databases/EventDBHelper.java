package org.teamhavei.havei.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.teamhavei.havei.Event.EventTag;
import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.HabitExec;
import org.teamhavei.havei.Event.Todo;
import org.teamhavei.havei.UniToolKit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventDBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DEBUG";

    public static final int DB_VERSION = 3;
    public static final String DB_NAME = "Event.db";

    //========const column and table names:Begin========
    private static final String TABLE_HABIT = "Habit";
    private static final String HABIT_ID = "id";
    private static final String HABIT_NAME = "name";
    private static final String HABIT_TAG_ID = "tag_id";
    private static final String HABIT_REPEAT_UNIT = "repeat_unit";
    private static final String HABIT_REPEAT_TIMES = "repeat_times";
    private static final String HABIT_REMINDER_TIME = "reminder_time";

    private static final String TABLE_HABIT_EXECS = "Habit_execs";
    private static final String HABIT_EXECS_ID = "id";
    private static final String HABIT_EXECS_HABIT_ID = "habit_id";
    private static final String HABIT_EXECS_DATE = "date";

    private static final String TABLE_EVENT_TAGS = "EventTags";
    private static final String EVENT_TAGS_ID = "id";
    private static final String EVENT_TAGS_ICON_ID = "icon_id";
    private static final String EVENT_TAGS_NAME = "name";
    private static final String EVENT_TAGS_DELETE = "del";

    private static final String TABLE_TODO = "Todo";
    private static final String TODO_ID = "id";
    private static final String TODO_NAME = "name";
    private static final String TODO_TAG_ID = "tag_id";
    private static final String TODO_DATETIME = "datetime";
    private static final String TODO_REMINDER_DATETIME = "reminder_datetime";
    private static final String TODO_REMARK = "remark";
    private static final String TODO_DONE = "done";

    //========const column and table names:end========

    private static final String CREATE_HABIT =
            "create table " + TABLE_HABIT + "(" +
                    HABIT_ID + " integer primary key autoincrement," +//id
                    HABIT_NAME + " text," +//习惯名
                    HABIT_TAG_ID + " integer," +//标签id
                    HABIT_REPEAT_UNIT + " integer," +//目标计数周期 天为单位
                    HABIT_REPEAT_TIMES + " integer," +//目标计数次数
                    HABIT_REMINDER_TIME + " text)";//提醒时间 格式HH:mm

    private static final String CREATE_HABIT_EXECS =
            "create table " + TABLE_HABIT_EXECS + "(" +
                    HABIT_EXECS_ID + " integer primary key autoincrement," +//执行记录id
                    HABIT_EXECS_HABIT_ID + " integer," +//执行习惯id
                    HABIT_EXECS_DATE + " text)";//执行日期 格式 yyyy-MM-dd

    private static final String CREATE_EVENT_TAGS =
            "create table " + TABLE_EVENT_TAGS + "(" +
                    EVENT_TAGS_ID + " integer primary key autoincrement," +//标签id
                    EVENT_TAGS_ICON_ID + " integer," +//图标id
                    EVENT_TAGS_NAME + " text," +//标签名称
                    EVENT_TAGS_DELETE + " integer)";//标签是否被删除 0:未删除 1:已删除

    private static final String CREATE_TODO =
            "create table " + TABLE_TODO + "(" +
                    TODO_ID + " integer primary key autoincrement," +//待办id
                    TODO_NAME + " text," +//待办名称
                    TODO_TAG_ID + " integer," +//标签id
                    TODO_DATETIME + " text," +//预期日期时间 格式 yyyy-MM-dd HH:mm
                    TODO_REMINDER_DATETIME + " text," +//提醒日期时间 格式 yyyy-MM-dd HH:mm
                    TODO_REMARK + " text," +//备注
                    TODO_DONE + " integer)";//是否已完成 0:未完成 1:已完成

    private Context mContext;

    private SQLiteDatabase db = getReadableDatabase();

    public EventDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HABIT);
        db.execSQL(CREATE_HABIT_EXECS);
        db.execSQL(CREATE_EVENT_TAGS);
        db.execSQL(CREATE_TODO);
        initialize(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("drop table if exists Habit");
            db.execSQL("drop table if exists HabitExecs");
        }
        db.execSQL("drop table if exists " + TABLE_EVENT_TAGS);
        db.execSQL("drop table if exists " + TABLE_HABIT);
        db.execSQL("drop table if exists " + TABLE_HABIT_EXECS);
        db.execSQL("drop table if exists " + TABLE_TODO);
        onCreate(db);
    }


    //========Habit相关功能:Begin========
    private ContentValues habitToValues(Habit mHabit) {
        ContentValues values = new ContentValues();
        values.put(HABIT_NAME, mHabit.getName());
        values.put(HABIT_TAG_ID, mHabit.getTagId());
        values.put(HABIT_REPEAT_UNIT, mHabit.getRepeatUnit());
        values.put(HABIT_REPEAT_TIMES, mHabit.getRepeatTimes());
        if (mHabit.getReminderTime() != null) {
            values.put(HABIT_REMINDER_TIME, mHabit.getReminderTime());
        } else {
            values.putNull(HABIT_REMINDER_TIME);
        }
        return values;
    }

    private List<Habit> cursorToHabitList(Cursor cursor) {
        List<Habit> mHabitList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToHabit: Found more than one habit");
                }
                while (cursor.moveToNext()) {
                    Habit mHabit = new Habit();
                    mHabit.setId(cursor.getInt(cursor.getColumnIndex(HABIT_ID)));
                    mHabit.setName(cursor.getString(cursor.getColumnIndex(HABIT_NAME)));
                    mHabit.setTagId(cursor.getInt(cursor.getColumnIndex(HABIT_TAG_ID)));
                    mHabit.setRepeatUnit(cursor.getInt(cursor.getColumnIndex(HABIT_REPEAT_UNIT)));
                    mHabit.setRepeatTimes(cursor.getInt(cursor.getColumnIndex(HABIT_REPEAT_TIMES)));
                    mHabit.setReminderTime(cursor.getString(cursor.getColumnIndex(HABIT_REMINDER_TIME)));
                    mHabitList.add(mHabit);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "cursorToHabitList: No such habit");
        }
        return mHabitList;
    }

    public void insertHabit(Habit mHabit) {
        db.insert(TABLE_HABIT, null, habitToValues(mHabit));
    }

    public Habit findHabitById(int id) {
        Cursor cursor = db.query(TABLE_HABIT, null, HABIT_ID + "= ?", new String[]{Integer.toString(id)}, null, null, null);
        List<Habit> habitList = cursorToHabitList(cursor);
        Habit mHabit;
        if (habitList.isEmpty()) {
            mHabit = new Habit();
        } else {
            mHabit = habitList.get(0);
        }
        cursor.close();
        return mHabit;
    }

    public void updateHabit(Habit oldHabit, Habit newHabit) {
        db.update(TABLE_HABIT, habitToValues(newHabit), HABIT_ID + " = ?", new String[]{Integer.toString(oldHabit.getId())});
    }

    public void updateHabit(int habitId, Habit newHabit) {
        db.update(TABLE_HABIT, habitToValues(newHabit), HABIT_ID + " = ?", new String[]{Integer.toString(habitId)});
    }

    public void deleteHabit(Habit mHabit) {
        db.delete(TABLE_HABIT, HABIT_ID + " = ? ", new String[]{Integer.toString(mHabit.getId())});
        db.delete(TABLE_HABIT_EXECS, HABIT_EXECS_HABIT_ID + " = ?", new String[]{Integer.toString(mHabit.getId())});
    }

    public List<Habit> findHabitByReminderTime(Date time) {
        String sTime = UniToolKit.eventTimeFormatter(time);
        Cursor cursor = db.query(TABLE_HABIT, null, HABIT_REMINDER_TIME + " = ?", new String[]{sTime}, null, null, null);
        return cursorToHabitList(cursor);
    }

    public List<Habit> findAllHabit() {
        Cursor cursor = db.query(TABLE_HABIT, null, null, null, null, null, null);
        return cursorToHabitList(cursor);
    }

    public int getHabitRank(int habitId) {
        /* SELECT Habit.id, COUNT(Habit.id) FROM Habit JOIN Habit_execs ON Habit.id = Habit_execs.habit_id GROUP BY Habit.id ORDER BY COUNT(Habit.id) DESC*/
        final String QUERY_ORDER_BY_RANK =
                "SELECT " + TABLE_HABIT + "." + HABIT_ID + ", COUNT(" + TABLE_HABIT + "." + HABIT_ID + ") " +
                        "FROM " + TABLE_HABIT + " JOIN " + TABLE_HABIT_EXECS + " ON " + TABLE_HABIT + "." + HABIT_ID + " = " + TABLE_HABIT_EXECS + "." + HABIT_EXECS_HABIT_ID +
                        " GROUP BY " + TABLE_HABIT + "." + HABIT_ID +
                        " ORDER BY COUNT(" + TABLE_HABIT + "." + HABIT_ID + ") DESC";
        Log.d(TAG, "getHabitRank: " + QUERY_ORDER_BY_RANK);
        Cursor cursor = db.rawQuery(QUERY_ORDER_BY_RANK, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(HABIT_ID)) == habitId) {
                return cursor.getPosition() + 1;
            }
        }
        Log.d(TAG, "getHabitRank: no such habit with id of " + habitId);
        return 0;
    }

    public Boolean checkHabitFinishBetween(int habitID, Date startDate, Date endDate) {
        Habit habit = findHabitById(habitID);
        if (habit == null) {
            return false;
        }
        String sStartDate = UniToolKit.eventDateFormatter(startDate);
        String sEndDate = UniToolKit.eventDateFormatter(endDate);
        Cursor cursor = db.query(TABLE_HABIT_EXECS, null, HABIT_EXECS_DATE + " > ? AND " + HABIT_EXECS_DATE + " < ?", new String[]{sStartDate, sEndDate}, null, null, null);
        return cursor.getCount() >= habit.getRepeatTimes();
    }

    public List<Habit> findUnfinishedHabit(Calendar centerCalendar) {
        List<Habit> allHabit = findAllHabit();
        List<Habit> unfinishedHabit = new ArrayList<>();
        Calendar startCalendar;
        Calendar endCalendar;
        for (Habit mHabit : allHabit) {
            startCalendar = (Calendar) centerCalendar.clone();
            endCalendar = (Calendar) centerCalendar.clone();
            startCalendar.add(Calendar.DAY_OF_YEAR, -1 * (mHabit.getRepeatUnit() - 1));
            endCalendar.add(Calendar.DAY_OF_YEAR, mHabit.getRepeatUnit() - 1);
            if (!checkHabitFinishBetween(mHabit.getId(), startCalendar.getTime(), endCalendar.getTime())) {
                unfinishedHabit.add(mHabit);
            }
        }
        return unfinishedHabit;
    }
    //========Habit相关功能:end===============


    //========Habit_Exec相关功能:Begin========
    private ContentValues habitExecToValues(HabitExec mHabitExec) {
        ContentValues values = new ContentValues();
        values.put(HABIT_EXECS_HABIT_ID, mHabitExec.getHabitId());
        values.put(HABIT_EXECS_DATE, mHabitExec.getDate());
        return values;
    }

    private List<HabitExec> cursorToHabitExecList(Cursor cursor) {
        List<HabitExec> mHabitExecList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToHabitExecList: Found more than one habit execution record");
                }
                while (cursor.moveToNext()) {
                    HabitExec mHabitExec = new HabitExec();
                    mHabitExec.setId(cursor.getInt(cursor.getColumnIndex(HABIT_EXECS_ID)));
                    mHabitExec.setHabitId(cursor.getInt(cursor.getColumnIndex(HABIT_EXECS_HABIT_ID)));
                    mHabitExec.setDate(cursor.getString(cursor.getColumnIndex(HABIT_EXECS_DATE)));
                    mHabitExecList.add(mHabitExec);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "cursorToHabitExecList: No such HabitExec");
        }
        return mHabitExecList;
    }

    public void insertHabitExec(HabitExec mHabitExec) {
        db.insert(TABLE_HABIT_EXECS, null, habitExecToValues(mHabitExec));
    }

    public void deleteHabitExec(HabitExec mHabitExec) {
        db.delete(TABLE_HABIT_EXECS, HABIT_ID + " = ?", new String[]{Integer.toString(mHabitExec.getId())});
    }

    public boolean switchHabitExec(int habitId, String date) {
        if (isHabitDone(habitId, date)) {
            db.delete(TABLE_HABIT_EXECS, HABIT_EXECS_HABIT_ID + " = ?" + " AND " + HABIT_EXECS_DATE + " = ?", new String[]{Integer.toString(habitId), date});
            return false;
        } else {
            HabitExec habitExec = new HabitExec();
            habitExec.setDate(date);
            habitExec.setHabitId(habitId);
            insertHabitExec(habitExec);
            return true;
        }
    }

    public HabitExec findHabitExecById(int id) {
        Cursor cursor = db.query(TABLE_HABIT_EXECS, null, HABIT_EXECS_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        HabitExec mHabitExec = (HabitExec) cursorToHabitExecList(cursor).get(0);
        cursor.close();
        return mHabitExec;
    }

    /**
     * return a group of HabitExecs chronologically
     */
    public List<HabitExec> findHabitExecByHabitId(int habitId) {
        Cursor cursor = db.query(TABLE_HABIT_EXECS, null, HABIT_EXECS_HABIT_ID + " = ?", new String[]{Integer.toString(habitId)}, null, null, HABIT_EXECS_DATE);
        List<HabitExec> habitExecList = cursorToHabitExecList(cursor);
        cursor.close();
        return habitExecList;
    }

    public boolean isHabitDoneToday(int habitId) {
        Cursor cursor = db.query(TABLE_HABIT_EXECS, null, HABIT_EXECS_HABIT_ID + " = ?" + " AND " + HABIT_EXECS_DATE + " = ?", new String[]{Integer.toString(habitId), UniToolKit.eventDateFormatter(new Date())}, null, null, null);
        return cursor.getCount() > 0;
    }

    public boolean isHabitDone(int habitId, String date) {
        Cursor cursor = db.query(TABLE_HABIT_EXECS, null, HABIT_EXECS_HABIT_ID + " = ?" + " AND " + HABIT_EXECS_DATE + " = ?", new String[]{Integer.toString(habitId), date}, null, null, null);
        return cursor.getCount() > 0;
    }
    //========Habit_Exec相关功能:end=========


    //========Event_Tag相关功能:Begin========
    private ContentValues eventTagToValues(EventTag mEventTag) {
        ContentValues values = new ContentValues();
        values.put(EVENT_TAGS_NAME, mEventTag.getName());
        values.put(EVENT_TAGS_ICON_ID, mEventTag.getIconId());
        values.put(EVENT_TAGS_DELETE, mEventTag.isDel() ? 1 : 0);
        return values;
    }

    private List<EventTag> cursorToEventTagList(Cursor cursor) {
        List<EventTag> tagList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToEventTag: Found more than one event tag");
                }
                while (cursor.moveToNext()) {
                    EventTag mEventTag = new EventTag();
                    mEventTag.setId(cursor.getInt(cursor.getColumnIndex(EVENT_TAGS_ID)));
                    mEventTag.setName(cursor.getString(cursor.getColumnIndex(EVENT_TAGS_NAME)));
                    mEventTag.setIconId(cursor.getInt(cursor.getColumnIndex(EVENT_TAGS_ICON_ID)));
                    mEventTag.setDel(cursor.getInt(cursor.getColumnIndex(EVENT_TAGS_DELETE)) == 1);
                    tagList.add(mEventTag);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "cursorToEventTag: No such EventTag");
        }
        return tagList;
    }

    public void insertEventTag(EventTag mEventTag) {
        db.insert(TABLE_EVENT_TAGS, null, eventTagToValues(mEventTag));
    }

    public EventTag findEventTagById(int id) {
        Cursor cursor = db.query(TABLE_EVENT_TAGS, null, EVENT_TAGS_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        EventTag mEventTag = cursorToEventTagList(cursor).get(0);
        cursor.close();
        return mEventTag;
    }

    public void updateEventTag(EventTag oldEventTag, EventTag newEventTag) {
        db.update(TABLE_EVENT_TAGS, eventTagToValues(newEventTag), EVENT_TAGS_ID + " = ?", new String[]{Integer.toString(oldEventTag.getId())});
    }

    public void updateEventTag(int eventTagId, EventTag newEventTag) {
        db.update(TABLE_EVENT_TAGS, eventTagToValues(newEventTag), EVENT_TAGS_ID + " = ?", new String[]{Integer.toString(eventTagId)});
    }

    public void deleteEventTag(EventTag mEventTag) {
        EventTag delEventTag = mEventTag;
        delEventTag.setDel(true);
        updateEventTag(mEventTag, delEventTag);
    }

    public List<EventTag> findAllEventTag(Boolean excludeDeleted) {
        Cursor cursor;
        if (excludeDeleted) {
            cursor = db.query(TABLE_EVENT_TAGS, null, EVENT_TAGS_DELETE + " = ?", new String[]{"0"}, null, null, null);
        } else {
            cursor = db.query(TABLE_EVENT_TAGS, null, null, null, null, null, null);
        }
        List<EventTag> tagList = cursorToEventTagList(cursor);
        cursor.close();
        return tagList;
    }
    //========Event_Tag相关功能:end========


    //========Todo相关功能:Begin========
    private ContentValues todoToValues(Todo mTodo) {
        ContentValues values = new ContentValues();
        values.put(TODO_NAME, mTodo.getName());
        values.put(TODO_TAG_ID, mTodo.getTagId());
        values.put(TODO_DATETIME, mTodo.getDateTime());
        if (mTodo.getReminderDateTime() == null) {
            values.putNull(TODO_REMINDER_DATETIME);
        } else {
            values.put(TODO_REMINDER_DATETIME, mTodo.getReminderDateTime());
        }
        if (mTodo.getRemark() == null) {
            values.putNull(TODO_REMARK);
        } else {
            values.put(TODO_REMARK, mTodo.getRemark());
        }
        values.put(TODO_REMARK, mTodo.getRemark());
        values.put(TODO_DONE, mTodo.isDone() ? 1 : 0);
        return values;
    }

    private List<Todo> cursorToTodoList(Cursor cursor) {
        List<Todo> mTodoList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToTodoList: Found more than one todo");
                }
                cursor.moveToFirst();
                do {
                    Todo mTodo = new Todo();
                    mTodo.setId(cursor.getInt(cursor.getColumnIndex(TODO_ID)));
                    mTodo.setName(cursor.getString(cursor.getColumnIndex(TODO_NAME)));
                    mTodo.setTagId(cursor.getInt(cursor.getColumnIndex(TODO_TAG_ID)));
                    mTodo.setDateTime(cursor.getString(cursor.getColumnIndex(TODO_DATETIME)));
                    mTodo.setDone(cursor.getInt(cursor.getColumnIndex(TODO_DONE)) == 1);
                    mTodo.setRemark(cursor.getString(cursor.getColumnIndex(TODO_REMARK)));
                    mTodo.setReminderDateTime(cursor.getString(cursor.getColumnIndex(TODO_REMINDER_DATETIME)));
                    mTodoList.add(mTodo);
                } while (cursor.moveToNext());
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "cursorToTodoList: No such Todo");
        }
        return mTodoList;
    }

    public void insertTodo(Todo mTodo) {
        db.insert(TABLE_TODO, null, todoToValues(mTodo));
    }

    public Todo findTodoById(int id) {
        Cursor cursor = db.query(TABLE_TODO, null, TODO_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        Todo mTodo = (Todo) cursorToTodoList(cursor).get(0);
        cursor.close();
        return mTodo;
    }

    public void updateTodo(Todo oldTodo, Todo newTodo) {
        db.update(TABLE_TODO, todoToValues(newTodo), TODO_ID + " = ?", new String[]{Integer.toString(oldTodo.getId())});
    }

    public void updateTodo(int todoId, Todo newTodo) {
        db.update(TABLE_TODO, todoToValues(newTodo), TODO_ID + " = ?", new String[]{Integer.toString(todoId)});
    }


    public void deleteTodo(Todo mTodo) {
        db.delete(TABLE_TODO, TODO_ID + " = ?", new String[]{Integer.toString(mTodo.getId())});
    }

    public List<Todo> findTodoByDate(Date date) {
        String sDate = UniToolKit.eventDateFormatter(date);
        Cursor cursor = db.query(TABLE_TODO, null, TODO_DATETIME + " LIKE ?", new String[]{sDate + "%"}, null, null, null);
        return cursorToTodoList(cursor);
    }

    public List<Todo> findTodoByDatetime(Date datetime) {
        String sDatetime = UniToolKit.eventDatetimeFormatter(datetime);
        Cursor cursor = db.query(TABLE_TODO, null, TODO_DATETIME + " = ?", new String[]{sDatetime}, null, null, null);
        return cursorToTodoList(cursor);
    }

    public List<Todo> findTodoByReminderDatetime(Date datetime) {
        String sDatetime = UniToolKit.eventDatetimeFormatter(datetime);
        Cursor cursor = db.query(TABLE_TODO, null, TODO_REMINDER_DATETIME + " = ?", new String[]{sDatetime}, null, null, null);
        return cursorToTodoList(cursor);
    }

    public boolean switchTodoDone(int todoId){
        Todo newTodo = findTodoById(todoId);
        if(newTodo.isDone()){
            newTodo.setDone(false);
            updateTodo(todoId,newTodo);
            return false;
        }else{
            newTodo.setDone(true);
            updateTodo(todoId,newTodo);
            return true;
        }
    }

    //========Todo相关功能:end========

    /**
     * 向数据库预先输入数据
     */
    // TODO: 2021.08.18 整理完成所需类别后修改
    private void initialize(SQLiteDatabase db){
        EventTag tag = new EventTag();
        for(int i=1;i<=25;i++){
            tag.setIconId(i);
            tag.setName("tag"+i);
            db.insert(TABLE_EVENT_TAGS, null, eventTagToValues(tag));
        }
    }
}
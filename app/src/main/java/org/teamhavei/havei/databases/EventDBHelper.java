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

import java.util.ArrayList;
import java.util.List;

public class EventDBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DEBUG";

    public static final int DATABASE_VERSION = 2;
    public static final String DB_NAME = "Event.db";

    //========const column and table names:Begin========
    private static final String TABLE_HABIT = "Habit";
    private static final String HABIT_ID = "id";
    private static final String HABIT_NAME = "name";
    private static final String HABIT_TAG_ID = "tagid";
    private static final String HABIT_REPEAT_UNIT = "repeatunit";
    private static final String HABIT_REPEAT_TIMES = "repeattimes";
    private static final String HABIT_REMINDER_TIME = "remindertime";

    private static final String TABLE_HABIT_EXECS = "Habit_execs";
    private static final String HABIT_EXECS_ID = "id";
    private static final String HABIT_EXECS_HABIT_ID = "habitid";
    private static final String HABIT_EXECS_DATE = "date";

    private static final String TABLE_EVENT_TAGS = "EventTags";
    private static final String EVENT_TAGS_ID = "id";
    private static final String EVENT_TAGS_ICON_ID = "icon_id";
    private static final String EVENT_TAGS_NAME = "name";

    private static final String TABLE_TODO = "Todo";
    private static final String TODO_ID = "id";
    private static final String TODO_NAME = "name";
    private static final String TODO_TAG_ID = "tagid";
    private static final String TODO_DATETIME = "datetime";
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
                    HABIT_EXECS_DATE + " text)";//执行日期

    private static final String CREATE_EVENT_TAGS =
            "create table " + TABLE_EVENT_TAGS + "(" +
                    EVENT_TAGS_ID + " integer primary key autoincrement," +//标签id
                    EVENT_TAGS_ICON_ID + " integer," +//图标id
                    EVENT_TAGS_NAME + " text)";//标签名称

    private static final String CREATE_TODO =
            "create table " + TABLE_TODO + "(" +
                    TODO_ID + " integer primary key autoincrement," +//待办id
                    TODO_NAME + " text," +//待办名称
                    TODO_TAG_ID + " integer," +//标签id
                    TODO_DATETIME + " text," +//预期日期时间 格式 yyyy-MM-dd HH:mm
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("drop table if exists Habit");
            db.execSQL("drop table if exists HabitExecs");
        }
        onCreate(db);
    }


    //========Habit相关功能:Begin========
    private ContentValues habitToValues(Habit mHabit) {
        ContentValues values = new ContentValues();
        values.put(HABIT_NAME, mHabit.getName());
        values.put(HABIT_TAG_ID, mHabit.getTagId());
        values.put(HABIT_REPEAT_UNIT, mHabit.getRepeatUnit());
        values.put(HABIT_REPEAT_TIMES, mHabit.getRepeatTimes());
        values.put(HABIT_REMINDER_TIME, mHabit.getReminderTime());
        return values;
    }

    private List cursorToHabitList(Cursor cursor) {
        List<Habit> mHabitList = new ArrayList<>();
        try {
            if (cursor.getCount() > 1) {
                Log.d(TAG, "cursorToHabit: Found more than one habit");
            }
            cursor.moveToFirst();
            do {
                Habit mHabit = new Habit();
                mHabit.setId(cursor.getInt(cursor.getColumnIndex(HABIT_ID)));
                mHabit.setName(cursor.getString(cursor.getColumnIndex(HABIT_NAME)));
                mHabit.setTagId(cursor.getInt(cursor.getColumnIndex(HABIT_TAG_ID)));
                mHabit.setRepeatUnit(cursor.getInt(cursor.getColumnIndex(HABIT_REPEAT_UNIT)));
                mHabit.setRepeatTimes(cursor.getInt(cursor.getColumnIndex(HABIT_REPEAT_TIMES)));
                mHabit.setReminderTime(cursor.getString(cursor.getColumnIndex(HABIT_REMINDER_TIME)));
                mHabitList.add(mHabit);
            } while (cursor.moveToNext());
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e(TAG, "cursorToHabitList: No such habit", e);
        }
        return mHabitList;
    }

    public void insertHabit(Habit mHabit) {
        db.insert(TABLE_HABIT, null, habitToValues(mHabit));
    }

    public Habit findHabitById(int id) {
        Cursor cursor = db.query(TABLE_HABIT, null, HABIT_ID + "= ?", new String[]{Integer.toString(id)}, null, null, null);
        Habit mHabit = (Habit) cursorToHabitList(cursor).get(0);
        cursor.close();
        return mHabit;
    }

    public void updateHabit(Habit oldHabit, Habit newHabit) {
        db.update(TABLE_HABIT, habitToValues(newHabit), HABIT_ID + " = ?", new String[]{Integer.toString(oldHabit.getId())});
    }

    public void deleteHabit(Habit mHabit) {
        db.delete(TABLE_HABIT, HABIT_ID + " = ? ", new String[]{Integer.toString(mHabit.getId())});
        db.delete(TABLE_HABIT_EXECS, HABIT_EXECS_HABIT_ID + " = ?", new String[]{Integer.toString(mHabit.getId())});
    }

    public List findHabitByReminderTime(String time){
        Cursor cursor = db.query(TABLE_HABIT,null,HABIT_REMINDER_TIME + " = ?",new String[]{time},null,null,null);
        return cursorToHabitList(cursor);
    }
    //========Habit相关功能:end========


    //========Habit_Exec相关功能:Begin========
    private ContentValues habitExecToValues(HabitExec mHabitExec) {
        ContentValues values = new ContentValues();
        values.put(HABIT_EXECS_HABIT_ID, mHabitExec.getHabitId());
        values.put(HABIT_EXECS_DATE, mHabitExec.getDate());
        return values;
    }

    private List cursorToHabitExecList(Cursor cursor) {
        List<HabitExec> mHabitExecList = new ArrayList<>();
        try {
            if (cursor.getCount() > 1) {
                Log.d(TAG, "cursorToHabitExecList: Found more than one habit execution record");
            }
            cursor.moveToFirst();
            do {
                HabitExec mHabitExec = new HabitExec();
                mHabitExec.setId(cursor.getInt(cursor.getColumnIndex(HABIT_EXECS_ID)));
                mHabitExec.setHabitId(cursor.getInt(cursor.getColumnIndex(HABIT_EXECS_HABIT_ID)));
                mHabitExec.setDate(cursor.getString(cursor.getColumnIndex(HABIT_EXECS_DATE)));
                mHabitExecList.add(mHabitExec);
            } while (cursor.moveToNext());
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e(TAG, "cursorToHabitExecList: No such Habit execution record", e);
        }
        return mHabitExecList;
    }

    public void insertHabitExec(HabitExec mHabitExec) {
        db.insert(TABLE_HABIT_EXECS, null, habitExecToValues(mHabitExec));
    }

    public void deleteHabitExec(HabitExec mHabitExec) {
        db.delete(TABLE_HABIT_EXECS, HABIT_ID + " = ?", new String[]{Integer.toString(mHabitExec.getId())});
    }

    public HabitExec findHabitExecById(int id) {
        Cursor cursor = db.query(TABLE_HABIT_EXECS, null, HABIT_EXECS_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        HabitExec mHabitExec = (HabitExec) cursorToHabitExecList(cursor).get(0);
        cursor.close();
        return mHabitExec;
    }
    //========Habit_Exec相关功能:end========


    //========Event_Tag相关功能:Begin========
    private ContentValues eventTagToValues(EventTag mEventTag) {
        ContentValues values = new ContentValues();
        values.put(EVENT_TAGS_ID, mEventTag.getId());
        values.put(EVENT_TAGS_NAME, mEventTag.getName());
        values.put(EVENT_TAGS_ICON_ID, mEventTag.getIconId());
        return values;
    }

    private EventTag cursorToEventTag(Cursor cursor) {
        EventTag mEventTag = new EventTag();
        try {
            if (cursor.getCount() > 1) {
                Log.d(TAG, "cursorToEventTag: Found more than one event tag");
            }
            cursor.moveToFirst();
            mEventTag.setId(cursor.getInt(cursor.getColumnIndex(EVENT_TAGS_ID)));
            mEventTag.setName(cursor.getString(cursor.getColumnIndex(EVENT_TAGS_NAME)));
            mEventTag.setIconId(cursor.getInt(cursor.getColumnIndex(EVENT_TAGS_ICON_ID)));
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e(TAG, "cursorToEventTag: No such cursor", e);
        }
        return mEventTag;
    }

    public void insertEventTag(EventTag mEventTag) {
        db.insert(TABLE_EVENT_TAGS, null, eventTagToValues(mEventTag));
    }

    //找不到tag时返回默认tag
    public EventTag findEventTagById(int id) {
        Cursor cursor = db.query(TABLE_EVENT_TAGS, null, EVENT_TAGS_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        if (cursor.getCount() < 1) {
            db.query(TABLE_EVENT_TAGS, null, EVENT_TAGS_ID + " = ?", new String[]{"1"}, null, null, null);
            Log.d(TAG, "findEventTagById: No such event tag, return default tag");
        }
        EventTag mEventTag = cursorToEventTag(cursor);
        cursor.close();
        return mEventTag;
    }

    public void updateEventTag(EventTag oldEventTag, EventTag newEventTag) {
        if (oldEventTag.getId() == 1) {
            Log.d(TAG, "updateEventTag: Can not change default event tag");
            return;
        }
        db.update(TABLE_EVENT_TAGS, eventTagToValues(newEventTag), EVENT_TAGS_ID + " = ?", new String[]{Integer.toString(oldEventTag.getId())});
    }

    public void deleteEventTag(EventTag mEventTag) {
        if (mEventTag.getId() == 1) {
            Log.d(TAG, "deleteEventTag: Can not delete default event tag");
            return;
        }
        db.delete(TABLE_EVENT_TAGS, EVENT_TAGS_ID + " = ?", new String[]{Integer.toString(mEventTag.getIconId())});
    }
    //========Event_Tag相关功能:end========


    //========Todo相关功能:Begin========
    private ContentValues todoToValues(Todo mTodo) {
        ContentValues values = new ContentValues();
        values.put(TODO_ID, mTodo.getId());
        values.put(TODO_NAME, mTodo.getName());
        values.put(TODO_TAG_ID, mTodo.getTagId());
        values.put(TODO_DATETIME, mTodo.getDateTime());
        values.put(TODO_DONE, mTodo.isDone() ? 1 : 0);
        return values;
    }

    private List cursorToTodoList(Cursor cursor) {
        List<Todo> mTodoList = new ArrayList<>();
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
                mTodoList.add(mTodo);
            } while (cursor.moveToNext());
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e(TAG, "cursorToTodoList: No such todo", e);
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

    public void deleteTodo(Todo mTodo) {
        db.delete(TABLE_TODO, TODO_ID + " = ?", new String[]{Integer.toString(mTodo.getId())});
    }

    public List findTodoByDatetime(String datetime){
        Cursor cursor = db.query(TABLE_TODO,null,TODO_DATETIME + " = ?",new String[]{datetime},null,null,null);
        return cursorToTodoList(cursor);
    }

    //========Todo相关功能:end========
}
/*可能需要的数据表
* Habit(id integer, name text, tag text)
* HabitExecs(habit_id integer, date text)
* date的格式为yyyyMMdd
 */
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

import java.lang.reflect.Array;
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
                    HABIT_NAME + "text," +//习惯名
                    HABIT_TAG_ID + "integer," +//标签id
                    HABIT_REPEAT_UNIT + "integer," +//目标计数周期 天为单位
                    HABIT_REPEAT_TIMES + "integer," +//目标计数次数
                    HABIT_REMINDER_TIME + "text)";//提醒时间 格式HH:MM

    private static final String CREATE_HABIT_EXECS =
            "create table "+ TABLE_HABIT_EXECS + "(" +
                    HABIT_EXECS_ID + "integer primary key autoincrement," +//执行记录id
                    HABIT_EXECS_HABIT_ID + "integer," +//执行习惯id
                    HABIT_EXECS_DATE + "text)";//执行日期

    private static final String CREATE_EVENT_TAGS =
            "create table " + TABLE_EVENT_TAGS + "(" +
                    EVENT_TAGS_ID + "integer primary key autoincrement," +//标签id
                    EVENT_TAGS_ICON_ID + "integer," +//图标id
                    EVENT_TAGS_NAME + "name text)";//标签名称

    private static final String CREATE_TODO =
            "create table " + TABLE_TODO + "(" +
                    TODO_ID + "integer primary key autoincrement," +//待办id
                    TODO_NAME + "text," +//待办名称
                    TODO_TAG_ID + "integer," +//标签id
                    TODO_DATETIME + "text," +//预期日期时间 格式 YYYY-MM-DD HH:MM
                    TODO_DONE + "integer)";//是否已完成 0:未完成 1:已完成

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
        if(oldVersion == 1) {
            db.execSQL("drop table if exists Habit");
            db.execSQL("drop table if exists HabitExecs");
        }
        onCreate(db);
    }

    //========Habit相关功能:Begin========
    private ContentValues habitToValues(Habit mHabit){
        ContentValues values = new ContentValues();
        values.put(HABIT_NAME,mHabit.getName());
        values.put(HABIT_TAG_ID, mHabit.getTagId());
        values.put(HABIT_REPEAT_UNIT,mHabit.getRepeatUnit());
        values.put(HABIT_REPEAT_TIMES,mHabit.getRepeatTimes());
        values.put(HABIT_REMINDER_TIME,mHabit.getReminderTime());
        return values;
    }

    private List cursorToHabitList(Cursor cursor){
        List<Habit> mHabitList = new ArrayList<>();
        try{
            if(cursor.getCount()>1){
                Log.d(TAG, "cursorToHabit: Found more than one habit");
            }
            do {
                Habit mHabit = new Habit();
                mHabit.setId(cursor.getInt(cursor.getColumnIndex(HABIT_ID)));
                mHabit.setName(cursor.getString(cursor.getColumnIndex(HABIT_NAME)));
                mHabit.setTagId(cursor.getInt(cursor.getColumnIndex(HABIT_TAG_ID)));
                mHabit.setRepeatUnit(cursor.getInt(cursor.getColumnIndex(HABIT_REPEAT_UNIT)));
                mHabit.setRepeatTimes(cursor.getInt(cursor.getColumnIndex(HABIT_REPEAT_TIMES)));
                mHabit.setReminderTime(cursor.getString(cursor.getColumnIndex(HABIT_REMINDER_TIME)));
                mHabitList.add(mHabit);
            }while(cursor.moveToNext());
        }catch(CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            Log.e(TAG, "cursorToHabitList: No such habit", e);
        }
        return mHabitList;
    }

    public void insertHabit(Habit mHabit){
        db.insert(TABLE_HABIT,null,habitToValues(mHabit));
    }

    public Habit findHabitById(int id){
        Cursor cursor = db.query(TABLE_HABIT,null, HABIT_ID+"= ?",new String[]{Integer.toString(id)},null,null,null);
        Habit mHabit = (Habit)cursorToHabitList(cursor).get(0);
        cursor.close();
        return mHabit;
    }

    public void updateHabit(Habit oldHabit,Habit newHabit){
        db.update(TABLE_HABIT,habitToValues(newHabit),HABIT_ID + " = ?",new String[]{Integer.toString(oldHabit.getId())});
    }

    public void deleteHabit(Habit mHabit){
        db.delete(TABLE_HABIT,HABIT_ID + " = ? ",new String[]{Integer.toString(mHabit.getId())});
        db.delete(TABLE_HABIT_EXECS,HABIT_EXECS_HABIT_ID + " = ?",new String[]{Integer.toString(mHabit.getId())});
    }
    //========Habit相关功能:end========

    //========Habit_Exec相关功能:Begin========
    private ContentValues habitExecToValues(HabitExec mHabitExec){
        ContentValues values = new ContentValues();
        values.put(HABIT_EXECS_HABIT_ID,mHabitExec.getHabitId());
        values.put(HABIT_EXECS_DATE,mHabitExec.getDate());
        return values;
    }

    private List cursorToHabitExecList(Cursor cursor){
        List<HabitExec> mHabitExecList = new ArrayList<>();
        try{
            if(cursor.getCount()>1){
                Log.d(TAG, "cursorToHabitExecList: Found more than one habit execution record");
            }
            do{
                HabitExec mHabitExec = new HabitExec();
                mHabitExec.setId(cursor.getInt(cursor.getColumnIndex(HABIT_EXECS_ID)));
                mHabitExec.setHabitId(cursor.getInt(cursor.getColumnIndex(HABIT_EXECS_HABIT_ID)));
                mHabitExec.setDate(cursor.getString(cursor.getColumnIndex(HABIT_EXECS_DATE)));
                mHabitExecList.add(mHabitExec);
            }while(cursor.moveToNext());
        }catch(CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            Log.e(TAG, "cursorToHabitExecList: No such Habit execution record", e);
        }
        return mHabitExecList;
    }

    public void insertHabitExec(HabitExec mHabitExec){
        db.insert(TABLE_HABIT_EXECS,null,habitExecToValues(mHabitExec));
    }

    public void deleteHabitExec(HabitExec mHabitExec){
        db.delete(TABLE_HABIT_EXECS,HABIT_ID + " = ?",new String[]{Integer.toString(mHabitExec.getId())});
    }

    public HabitExec findHabitExecById(int id){
        Cursor cursor = db.query(TABLE_HABIT_EXECS, null, HABIT_EXECS_ID + " = ?", new String[]{Integer.toString(id)},null,null,null);
        HabitExec mHabitExec = (HabitExec)cursorToHabitExecList(cursor).get(0);
        cursor.close();
        return mHabitExec;
    }
    //========Habit_Exec相关功能:end========

    //========Event_Tag相关功能:Begin========
    //todo
    //========Event_Tag相关功能:end========

    //========Todo相关功能:Begin========
    //todo
    //========Todo相关功能:end========

//
//    public int getHabitID(String habitName){
//        Cursor cursor = db.query("Habit",new String[]{"id"},"name = ?",new String[]{habitName},null,null,null);
//        cursor.moveToFirst();
//        int id = 0;
//        try{
//            id = cursor.getInt(cursor.getColumnIndex("id"));
//        }catch (CursorIndexOutOfBoundsException e){
//            e.printStackTrace();
//            Log.e(TAG, "getHabitID: habitName not found", e);
//        }
//        cursor.close();
//        return id;
//    }
//
//    public String getHabitName(int ID){
//        Cursor cursor = db.query("Habit",new String[]{"name"},"id = ?",new String[]{Integer.toString(ID)},null,null,null);
//        cursor.moveToFirst();
//        String habitName = "";
//        try{
//            habitName = cursor.getString(cursor.getColumnIndex("name"));
//        }catch (CursorIndexOutOfBoundsException e){
//            e.printStackTrace();
//            Log.e(TAG, "getHabitName: habitID not found", e);
//        }
//        cursor.close();
//        return habitName;
//    }
//
//    public String getHabitTag(int ID){
//        Cursor cursor = db.query("Habit",new String[]{"tag"},"id = ?",new String[]{Integer.toString(ID)},null,null,null);
//        cursor.moveToFirst();
//        String habitName = "";
//        try{
//            habitName = cursor.getString(cursor.getColumnIndex("tag"));
//        }catch (CursorIndexOutOfBoundsException e){
//            e.printStackTrace();
//            Log.e(TAG, "getHabitTag: habitID not found", e);
//        }
//        cursor.close();
//        return habitName;
//    }
//
//    public String getHabitTag(String habitName){
//        Cursor cursor = db.query("Habit",new String[]{"tag"},"name = ?",new String[]{habitName},null,null,null);
//        cursor.moveToFirst();
//        String habitTag = "";
//        try{
//            habitTag = cursor.getString(cursor.getColumnIndex("tag"));
//        }catch(CursorIndexOutOfBoundsException e){
//            e.printStackTrace();
//            Log.e(TAG, "getHabitTag: habitName not found", e);
//        }
//        cursor.close();
//        return habitTag;
//    }
//
//    public boolean isHabitDoneToday(int habitID){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//        Date now = new Date();
//        String date = dateFormat.format(now);
//        Cursor cursor = db.query("HabitExecs",new String[]{"date"},"habit_id = ? and date = ?", new String[]{Integer.toString(habitID),date},null,null,null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count > 0;
//    }
//
//    public boolean switchHabitExec(int habitID){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//        Date now = new Date();
//        String date = dateFormat.format(now);
//        Cursor cursor = db.query("HabitExecs",new String[]{"date"},"habit_id = ? and date = ?", new String[]{Integer.toString(habitID),date},null,null,null);
//        int count = cursor.getCount();
//        cursor.close();
//        Log.d(TAG, "switchHabitExec: " + habitID + " " + count);
//        if(count > 0){
//            db.delete("HabitExecs","habit_id = ? and date = ?",new String[]{Integer.toString(habitID),date});
//            return false;
//        }
//        else{
//            ContentValues value = new ContentValues();
//            value.put("habit_id", habitID);
//            value.put("date",date);
//            db.insert("HabitExecs",null,value);
//            return true;
//        }
//    }
//
//    public boolean switchHabitExec(int habitID,String date){
//        Cursor cursor = db.query("HabitExecs",new String[]{"date"},"habit_id = ? and date = ?", new String[]{Integer.toString(habitID),date},null,null,null);
//        int count = cursor.getCount();
//        cursor.close();
//        if(count > 0){
//            db.delete("HabitExecs","habit_id = ? and date = ?",new String[]{Integer.toString(habitID),date});
//            return false;
//        }
//        else{
//            ContentValues value = new ContentValues();
//            value.put("habit_id", habitID);
//            value.put("date",date);
//            db.insert("HabitExecs",null,value);
//            return true;
//        }
//    }
//
//    public int getHabitExecCount(int habitID){
//        Cursor cursor = db.query("HabitExecs",null,"habit_id = ?",new String[]{Integer.toString(habitID)},null,null,null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count;
//    }
}

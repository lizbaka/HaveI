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

import java.text.SimpleDateFormat;
import java.util.Date;

public class HabitDBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DEBUG";

    public static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "Habit.db";

    private static final String CREATE_HABIT =
            "create table Habit(" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "tag text)";

    private static final String CREATE_HABIT_EXECS =
            "create table HabitExecs(" +
                    "habit_id integer," +
                    "date text)";

    private Context mContext;

    private SQLiteDatabase db = getReadableDatabase();

    public HabitDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HABIT);
        db.execSQL(CREATE_HABIT_EXECS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Habit");
        db.execSQL("drop table if exists HabitExecs");
        onCreate(db);
    }


    public int getHabitID(String habitName){
        Cursor cursor = db.query("Habit",new String[]{"id"},"name = ?",new String[]{habitName},null,null,null);
        cursor.moveToFirst();
        int id = 0;
        try{
            id = cursor.getInt(cursor.getColumnIndex("id"));
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            Log.e(TAG, "getHabitID: habitName not found", e);
        }
        cursor.close();
        return id;
    }

    public String getHabitName(int ID){
        Cursor cursor = db.query("Habit",new String[]{"name"},"id = ?",new String[]{Integer.toString(ID)},null,null,null);
        cursor.moveToFirst();
        String habitName = "";
        try{
            habitName = cursor.getString(cursor.getColumnIndex("name"));
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            Log.e(TAG, "getHabitName: habitID not found", e);
        }
        cursor.close();
        return habitName;
    }

    public String getHabitTag(int ID){
        Cursor cursor = db.query("Habit",new String[]{"tag"},"id = ?",new String[]{Integer.toString(ID)},null,null,null);
        cursor.moveToFirst();
        String habitName = "";
        try{
            habitName = cursor.getString(cursor.getColumnIndex("tag"));
        }catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            Log.e(TAG, "getHabitTag: habitID not found", e);
        }
        cursor.close();
        return habitName;
    }

    public String getHabitTag(String habitName){
        Cursor cursor = db.query("Habit",new String[]{"tag"},"name = ?",new String[]{habitName},null,null,null);
        cursor.moveToFirst();
        String habitTag = "";
        try{
            habitTag = cursor.getString(cursor.getColumnIndex("tag"));
        }catch(CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            Log.e(TAG, "getHabitTag: habitName not found", e);
        }
        cursor.close();
        return habitTag;
    }

    public boolean isHabitDoneToday(int habitID){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String date = dateFormat.format(now);
        Cursor cursor = db.query("HabitExecs",new String[]{"date"},"habit_id = ? and date = ?", new String[]{Integer.toString(habitID),date},null,null,null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean switchHabitExec(int habitID){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String date = dateFormat.format(now);
        Cursor cursor = db.query("HabitExecs",new String[]{"date"},"habit_id = ? and date = ?", new String[]{Integer.toString(habitID),date},null,null,null);
        int count = cursor.getCount();
        cursor.close();
        Log.d(TAG, "switchHabitExec: " + habitID + " " + count);
        if(count > 0){
            db.delete("HabitExecs","habit_id = ? and date = ?",new String[]{Integer.toString(habitID),date});
            return false;
        }
        else{
            ContentValues value = new ContentValues();
            value.put("habit_id", habitID);
            value.put("date",date);
            db.insert("HabitExecs",null,value);
            return true;
        }
    }

    public boolean switchHabitExec(int habitID,String date){
        Cursor cursor = db.query("HabitExecs",new String[]{"date"},"habit_id = ? and date = ?", new String[]{Integer.toString(habitID),date},null,null,null);
        int count = cursor.getCount();
        cursor.close();
        if(count > 0){
            db.delete("HabitExecs","habit_id = ? and date = ?",new String[]{Integer.toString(habitID),date});
            return false;
        }
        else{
            ContentValues value = new ContentValues();
            value.put("habit_id", habitID);
            value.put("date",date);
            db.insert("HabitExecs",null,value);
            return true;
        }
    }

    public int getHabitExecCount(int habitID){
        Cursor cursor = db.query("HabitExecs",null,"habit_id = ?",new String[]{Integer.toString(habitID)},null,null,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}

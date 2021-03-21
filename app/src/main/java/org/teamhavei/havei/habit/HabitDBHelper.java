/*可能需要的数据表
* Habit(id integer, name text, tag text)
* HabitExecs(habit_id integer, date date)
 */
package org.teamhavei.havei.habit;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

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
                    "date datetime)";

    private Context mContext;

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
    }


    public int getHabitID(String habitName){
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = getWritableDatabase();
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
        SQLiteDatabase db = getWritableDatabase();
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
        SQLiteDatabase db = getWritableDatabase();
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
}

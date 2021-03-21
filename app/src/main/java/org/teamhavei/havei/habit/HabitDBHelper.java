/*可能需要的数据表
* Habit(name text, tag text)
* HabitExecs(name text, date date)
 */
package org.teamhavei.havei.habit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HabitDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "Habit.db";

    private static final String CREATE_HABIT =
            "create table Habit(" +
                    "name text," +
                    "tag text)";

    private static final String CREATE_HABITEXECS =
            "create table HabitExecs(" +
                    "name text," +
                    "date datetime)";

    private Context mContext;


    public HabitDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HABIT);
        db.execSQL(CREATE_HABITEXECS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

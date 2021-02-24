/*可能需要的数据表
* Habit(name text, tag text, frequency integer, frequency_per integer, last date)
* HabitTags(name text)
* HabitExecs(name text, tag text, date date)
 */
package org.teamhavei.havei;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 0;

    public static final String CREATE_HABITS =
            "create table Habit(" +
                    "name text," +
                    "tag text," +
                    "frequency integer," +
                    "frequency_per integer" +
                    "exec_times integer";


    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package org.teamhavei.havei.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.Habit;


import java.util.ArrayList;
import java.util.List;


public class BookkeepingDBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DEBUG";

    public static final int DATABASE_VERSION = 2;
    public static final String DB_NAME = "Bookkeeping.db";
    private static final String TABLE_BOOKKEEP = "Account";
    private static final String BOOKKEEP_ID = "id";
    private static final String BOOKKEEP_NAME = "name";
    private static final String BOOKKEEP_TAG_ID = "tagid";
    private static final String BOOKKEEP_TIME = "time";
    private static final String BOOKKEEP_ICON_ID = "iconid";
    private static final String BOOKKEEP_PM = "pm";
    private static final String CREATE_ACCOUNT =
            "create table " + TABLE_BOOKKEEP + "(" +
                    BOOKKEEP_ID + " integer primary key autoincrement," +//id
                    BOOKKEEP_NAME + " text," +//习惯名
                    BOOKKEEP_TAG_ID + " integer," +//标签id
                    BOOKKEEP_TIME + "text,"+
                    BOOKKEEP_ICON_ID +"integer,"+
                    BOOKKEEP_PM + " integer)"; //计算参数

    private Context mContext;
    private SQLiteDatabase db = getReadableDatabase();
    public BookkeepingDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT);


    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("drop table if exists Account");

        }
        onCreate(db);
    }
    private ContentValues accountToValues(Bookkeep mBookkeep) {
        ContentValues values = new ContentValues();
        values.put(BOOKKEEP_NAME, mBookkeep.getname());
        values.put(BOOKKEEP_TAG_ID, mBookkeep.gettag());
        values.put(BOOKKEEP_TIME, mBookkeep.gettime());
        values.put(BOOKKEEP_ICON_ID, mBookkeep.getIconId());
        values.put(BOOKKEEP_PM, mBookkeep.getPM());
        return values;
    }

    private List cursorToBookkeepList(Cursor cursor) {
        List<Bookkeep> mBookkeepList = new ArrayList<>();
        if(cursor!=null && cursor.getCount()>0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToBookkeep: Found more than one");
                }
                while(cursor.moveToNext()){
                    Bookkeep mBookkeep = new Bookkeep();
                    mBookkeep.setId(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_ID)));
                    mBookkeep.setname(cursor.getString(cursor.getColumnIndex(BOOKKEEP_NAME)));
                    mBookkeep.settag(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_TAG_ID)));
                    mBookkeep.setTime(cursor.getString(cursor.getColumnIndex(BOOKKEEP_TIME)));
                    mBookkeep.setIconId(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_ICON_ID)));
                    mBookkeep.setPM(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_PM)));
                    mBookkeepList.add(mBookkeep);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d(TAG, "cursorToBookkeepList: No such keep");
        }
        return mBookkeepList;
    }
    public void insertBookkeep(Bookkeep mBookkeep) {
        db.insert(TABLE_BOOKKEEP, null, accountToValues(mBookkeep));
    }
    public Bookkeep findBookkeepById(int id) {
        Cursor cursor = db.query(TABLE_BOOKKEEP, null, BOOKKEEP_ID + "= ?", new String[]{Integer.toString(id)}, null, null, null);
        Bookkeep mBookkeep = (Bookkeep) cursorToBookkeepList(cursor).get(0);
        cursor.close();
        return mBookkeep;
    }
    public void updateBookkeep(Bookkeep oldBookkeep, Bookkeep newBookkeep) {
        db.update(TABLE_BOOKKEEP, accountToValues(newBookkeep), BOOKKEEP_ID + " = ?", new String[]{Integer.toString(oldBookkeep.getid())});
    }

    public void deleteBookkeep(Bookkeep mBookkeep) {
        db.delete(TABLE_BOOKKEEP, BOOKKEEP_ID + " = ? ", new String[]{Integer.toString(mBookkeep.getid())});
    }


}




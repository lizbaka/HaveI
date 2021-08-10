package org.teamhavei.havei.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.EventTag;
import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.UniToolKit;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BookkeepDBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DEBUG";

    public static final int DATABASE_VERSION = 2;
    public static final String DB_NAME = "Bookkeep.db";
    private static final String TABLE_BOOKKEEP = "Bookkeep";
    private static final String BOOKKEEP_ID = "id";
    private static final String BOOKKEEP_NAME = "name";
    private static final String BOOKKEEP_TAG_ID = "tagid";
    private static final String BOOKKEEP_TIME = "time";
    private static final String BOOKKEEP_ICON_ID = "iconid";
    private static final String BOOKKEEP_PM = "pm";
    private static final String CREATE_BOOKKEEP =
            "create table " + TABLE_BOOKKEEP + "(" +
                    BOOKKEEP_ID + " integer primary key autoincrement," +//id
                    BOOKKEEP_NAME + " text," +
                    BOOKKEEP_TAG_ID + " integer," +
                    BOOKKEEP_TIME + "text,"+
                    BOOKKEEP_ICON_ID +"integer,"+
                    BOOKKEEP_PM + " integer)";

    private static final String TABLE_BOOK_TAGS = "BookTags";
    private static final String BOOK_TAGS_ID = "id";
    private static final String BOOK_TAGS_ICON_ID = "icon_id";
    private static final String BOOK_TAGS_NAME = "name";
    private static final String BOOK_TAGS_DELETE = "del";
    private static final String CREATE_BOOK_TAGS =
            "create table " + TABLE_BOOK_TAGS + "(" +
                    BOOK_TAGS_ID + " integer primary key autoincrement," +//标签id
                    BOOK_TAGS_ICON_ID + " integer," +//图标id
                    BOOK_TAGS_NAME + " text," +//标签名称
                    BOOK_TAGS_DELETE + " integer)";//标签是否被删除 0:未删除 1:已删除

    private Context mContext;
    private SQLiteDatabase db = getReadableDatabase();
    public BookkeepDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKKEEP);
        db.execSQL(CREATE_BOOK_TAGS);


    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("drop table if exists bookkeep");

        }
        db.execSQL("drop table if exists " + TABLE_BOOK_TAGS);
        db.execSQL("drop table if exists " + TABLE_BOOKKEEP);

        onCreate(db);
    }
    //========Bookkeep相关功能:Begin========
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
    public List<Habit> findBookkeepByTime(Date time){
        String sTime = UniToolKit.eventTimeFormatter(time);
        Cursor cursor = db.query(TABLE_BOOKKEEP,null,BOOKKEEP_TIME + " = ?",new String[]{sTime},null,null,null);
        return cursorToBookkeepList(cursor);
    }
    public void updateBookkeep(Bookkeep oldBookkeep, Bookkeep newBookkeep) {
        db.update(TABLE_BOOKKEEP, accountToValues(newBookkeep), BOOKKEEP_ID + " = ?", new String[]{Integer.toString(oldBookkeep.getid())});
    }

    public void deleteBookkeep(Bookkeep mBookkeep) {
        db.delete(TABLE_BOOKKEEP, BOOKKEEP_ID + " = ? ", new String[]{Integer.toString(mBookkeep.getid())});
    }
    //========Bookkeep相关功能:end===============


    //========Book_Tag相关功能:Begin========
    private ContentValues bookTagToValues(BookTag mBookTag) {
        ContentValues values = new ContentValues();
        values.put(BOOK_TAGS_NAME, mBookTag.getName());
        values.put(BOOK_TAGS_ICON_ID, mBookTag.getIconId());
        values.put(BOOK_TAGS_DELETE, mBookTag.isDel()?1:0);
        return values;
    }

    private BookTag cursorToBookTag(Cursor cursor) {
        BookTag mBookTag = new BookTag();
        if(cursor!=null && cursor.getCount()>0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToEventTag: Found more than one event tag");
                }
                cursor.moveToFirst();
                mBookTag.setId(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_ID)));
                mBookTag.setName(cursor.getString(cursor.getColumnIndex(BOOK_TAGS_NAME)));
                mBookTag.setIconId(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_ICON_ID)));
                mBookTag.setDel(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_DELETE)) == 1);
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d(TAG, "cursorToBookTag: No such BookTag");
        }
        return mBookTag;
    }

    public void insertBookTag(BookTag mBookTag) {
        db.insert(TABLE_BOOK_TAGS, null, bookTagToValues(mBookTag));
    }

    public BookTag findBookTagById(int id) {
        Cursor cursor = db.query(TABLE_BOOK_TAGS, null, BOOK_TAGS_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        BookTag mBookTag = cursorToBookTag(cursor);
        cursor.close();
        return mBookTag;
    }

    public void updateBookTag(BookTag oldBookTag, BookTag newBookTag) {
        db.update(TABLE_BOOK_TAGS, bookTagToValues(newBookTag), BOOK_TAGS_ID + " = ?", new String[]{Integer.toString(oldBookTag.getId())});
    }

    public void deleteBookTag(BookTag mBookTag) {
        BookTag delBookTag = mBookTag;
        delBookTag.setDel(true);
        updateBookTag(mBookTag,delBookTag);
    }
    //========Book_Tag相关功能:end========

}




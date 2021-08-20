package org.teamhavei.havei.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.teamhavei.havei.Event.BookPlan;
import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.BookCou;
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
                    BOOKKEEP_PM + " double)";

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
                    BOOK_TAGS_DELETE + " integer)";
    private static final String TABLE_BOOK_COUS = "BookCous";
    private static final String BOOK_COUS_ID = "id";
    private static final String BOOK_COUS_TIME = "time";
    private static final String BOOK_COUS_IN = "in";
    private static final String BOOK_COUS_OUT = "out";
    private static final String CREATE_BOOK_COUS =
            "create table "+TABLE_BOOK_COUS+"("+
                        BOOK_COUS_ID+" integer primary key autoincrement,"+
                        BOOK_COUS_TIME+" text," +
                        BOOK_COUS_IN+"integer"+
                        BOOK_COUS_OUT+"integer)";

    private static final String TABLE_BOOK_PLAN = "BookPlan";
    private static final String BOOK_PLAN_ID = "id";
    private static final String BOOK_PLAN_NUM = "num";
    private static final String BOOK_PLAN_NEED = "need";
    private static final String CREATE_BOOK_PLAN =
            "create table "+TABLE_BOOK_PLAN+"("+
                    BOOK_PLAN_ID+" integer primary key autoincrement,"+
                    BOOK_PLAN_NUM+"integer"+
                    BOOK_PLAN_NEED+"integer)";

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
        db.execSQL(CREATE_BOOK_COUS);
        db.execSQL(CREATE_BOOK_PLAN);


    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("drop table if exists bookkeep");

        }
        db.execSQL("drop table if exists " + TABLE_BOOK_TAGS);
        db.execSQL("drop table if exists " + TABLE_BOOKKEEP);
        db.execSQL("drop table if exists " + TABLE_BOOK_COUS);

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
                    mBookkeep.setPM(cursor.getDouble(cursor.getColumnIndex(BOOKKEEP_PM)));
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
    public List<Bookkeep> findBookkeepByTime(Date time){
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
                    Log.d(TAG, "cursorToEventTag: Found more than one");
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

    //========Bookcous相关功能:Begin========


    private ContentValues bookCousToValues(BookCou mBookCou) {
        ContentValues values = new ContentValues();
        values.put(BOOK_COUS_TIME, mBookCou.getTime());
        values.put(BOOK_COUS_IN, mBookCou.getIn());
        values.put(BOOK_COUS_OUT, mBookCou.getOut());

        return values;
    }

    private BookCou cursorToBookCou(Cursor cursor) {
        BookCou mBookCou = new BookCou();
        if(cursor!=null && cursor.getCount()>0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToEventTag: Found more than one ");
                }
                cursor.moveToFirst();
                mBookCou.setId(cursor.getInt(cursor.getColumnIndex(BOOK_COUS_ID)));
                mBookCou.setTime(cursor.getString(cursor.getColumnIndex(BOOK_COUS_TIME)));
                mBookCou.setIn(cursor.getInt(cursor.getColumnIndex(BOOK_COUS_IN)));
                mBookCou.setOut(cursor.getInt(cursor.getColumnIndex(BOOK_COUS_OUT)));

            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d(TAG, "cursorToBookCou: No such BookCou");
        }
        return mBookCou;
    }

    public void insertBookCou(BookCou mBookCou) {
        db.insert(TABLE_BOOK_COUS, null, bookCousToValues(mBookCou));
    }

    public BookCou findBookCouById(int id) {
        Cursor cursor = db.query(TABLE_BOOK_COUS, null, BOOK_COUS_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        BookCou mBookCou = cursorToBookCou(cursor);
        cursor.close();
        return mBookCou;
    }

    public void updateBookCou(BookCou oldBookCou, BookCou newBookCou) {
        db.update(TABLE_BOOK_COUS, bookCousToValues(newBookCou), BOOK_COUS_ID + " = ?", new String[]{Integer.toString(oldBookCou.getId())});
    }

    public void deleteBookcous(BookCou mBookCou) {
        db.delete(TABLE_BOOK_COUS, BOOK_COUS_ID + " = ? ", new String[]{Integer.toString(mBookCou.getId())});
    }

    //========Bookcous相关功能:end========


    //========BookpLAN相关功能:start========
    private ContentValues bookPlanToValues(BookPlan mBookPlan) {
        ContentValues values = new ContentValues();

        values.put(BOOK_PLAN_NEED, mBookPlan.getNeed());
        values.put(BOOK_PLAN_NEED, mBookPlan.getNum());

        return values;
    }

    private BookPlan cursorToBookPlan(Cursor cursor) {
        BookPlan mBookPlan = new BookPlan();
        if(cursor!=null && cursor.getCount()>0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToEventTag: Found more than one ");
                }
                cursor.moveToFirst();
                mBookPlan.setId(cursor.getInt(cursor.getColumnIndex(BOOK_PLAN_ID)));

                mBookPlan.setNum(cursor.getInt(cursor.getColumnIndex(BOOK_PLAN_NUM)));
                mBookPlan.setNeed(cursor.getInt(cursor.getColumnIndex(BOOK_PLAN_NEED)));

            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d(TAG, "cursorToBookPAN: No such BookPLAN");
        }
        return mBookPlan;
    }

    public void insertBookPlan(BookPlan mBookPlan) {
        db.insert(TABLE_BOOK_PLAN, null, bookPlanToValues(mBookPlan));
    }

    public BookPlan findBookPlanById(int id) {
        Cursor cursor = db.query(TABLE_BOOK_PLAN, null, BOOK_PLAN_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        BookPlan mBookPlan = cursorToBookPlan(cursor);
        cursor.close();
        return mBookPlan;
    }

    public void updateBookplan(BookPlan oldBookPlan, BookPlan newBookPlan) {
        db.update(TABLE_BOOK_PLAN, bookPlanToValues(newBookPlan), BOOK_PLAN_ID + " = ?", new String[]{Integer.toString(oldBookPlan.getId())});
    }

    public void deleteBookplan(BookPlan mBookplan) {
        db.delete(TABLE_BOOK_PLAN, BOOK_PLAN_ID + " = ? ", new String[]{Integer.toString(mBookplan.getId())});
    }

}




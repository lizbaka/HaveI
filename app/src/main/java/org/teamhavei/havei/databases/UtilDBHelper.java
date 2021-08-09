package org.teamhavei.havei.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UtilDBHelper extends SQLiteOpenHelper {

    private final static String TAG = "DEBUG";

    public final static String DB_NAME = "UtilDBHelper";
    public final static int DB_VERSION = 1;

    //========const column and table names:Begin========
    private static final String TABLE_PROVERB = "Proverb";
    private static final String PROVERB_ID = "id";
    private static final String PROVERB_CONTENT = "content";
    //========const column and table names:end==========

    private static final String CREATE_PROVERB =
            "create table " + TABLE_PROVERB + "(" +
                    PROVERB_ID + " integer primary key autoincrement," +//id
                    PROVERB_CONTENT + " text)";//内容

    private SQLiteDatabase db = getReadableDatabase();

    public UtilDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVERB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_PROVERB);
        onCreate(db);
    }

    public List<String> getProverbs(){
        Cursor cursor = db.query(TABLE_PROVERB,new String[]{PROVERB_CONTENT},null,null,null,null,null);
        List<String> proverb = new ArrayList<String>();
        if(cursor==null){
            Log.d(TAG, "getProverb: cursor null");
            return proverb;
        }
        while(cursor.moveToNext()){
            proverb.add(cursor.getString(cursor.getColumnIndex(PROVERB_CONTENT)));
        }
        cursor.close();
        return proverb;
    }

    public void deleteProverbs(List<String> proverb){
        for(String s:proverb)
            db.delete(TABLE_PROVERB,PROVERB_CONTENT + " = ?", new String[]{s});
    }

    public void insertProverb(String s){
        ContentValues value = new ContentValues();
        value.put(PROVERB_CONTENT,s);
        db.insert(TABLE_PROVERB,null,value);
    }

    public String pickOneProverb(){
        List<String> proverb = getProverbs();
        return proverb.get((int)System.currentTimeMillis()%proverb.size());
    }
}

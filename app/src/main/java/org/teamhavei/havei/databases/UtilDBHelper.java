package org.teamhavei.havei.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.teamhavei.havei.R;

import java.util.ArrayList;
import java.util.List;

public class UtilDBHelper extends SQLiteOpenHelper {

    private final static String TAG = "DEBUG";

    public final static String DB_NAME = "Util.db";
    public final static int DB_VERSION = 1;

    Context context;

    //========const column and table names:Begin========
    private static final String TABLE_PROVERB = "Proverb";
    private static final String PROVERB_ID = "id";
    private static final String PROVERB_CONTENT = "content";
    //========const column and table names:end==========

    private static final String CREATE_PROVERB =
            "create table " + TABLE_PROVERB + "(" +
                    PROVERB_ID + " integer primary key autoincrement," +//id
                    PROVERB_CONTENT + " text)";//内容

    private final SQLiteDatabase db = getReadableDatabase();

    public UtilDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        preventEmpty();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVERB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //使用alter语句修改字段
    }

    private void preventEmpty() {
        Cursor cursor = db.query(TABLE_PROVERB, null, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            insertProverb(context.getString(R.string.slogan));
        }
    }

    public List<String> getProverbs() {
        Cursor cursor = db.query(TABLE_PROVERB, new String[]{PROVERB_CONTENT}, null, null, null, null, null);
        List<String> proverb = new ArrayList<>();
        if (cursor == null) {
            Log.d(TAG, "getProverb: cursor null");
            return proverb;
        }
        while (cursor.moveToNext()) {
            proverb.add(cursor.getString(cursor.getColumnIndex(PROVERB_CONTENT)));
        }
        cursor.close();
        return proverb;
    }

    public void deleteProverb(String proverb) {
        db.delete(TABLE_PROVERB, PROVERB_CONTENT + " = ?", new String[]{proverb});
    }

    public void deleteProverbs(List<String> proverb) {
        db.delete(TABLE_PROVERB, PROVERB_CONTENT + " = ?", proverb.toArray(new String[0]));
    }

    public boolean isProverbStored(String proverb) {
        Cursor cursor = db.query(TABLE_PROVERB, null, PROVERB_CONTENT + " = ?", new String[]{proverb}, null, null, null);
        return cursor.getCount() > 0;
    }

    public void insertProverb(String proverb) {
        if (!isProverbStored(proverb)) {
            ContentValues value = new ContentValues();
            value.put(PROVERB_CONTENT, proverb);
            db.insert(TABLE_PROVERB, null, value);
        }
    }

    public boolean switchProverb(String proverb) {
        if (!isProverbStored(proverb)) {
            insertProverb(proverb);
            return true;
        } else {
            deleteProverb(proverb);
            return false;
        }
    }

    public String pickOneProverb() {
        List<String> proverb = getProverbs();
        if (proverb == null || proverb.isEmpty()) {
            return context.getString(R.string.slogan);
        }
        return proverb.get((int) (System.currentTimeMillis() % proverb.size()));
    }
}

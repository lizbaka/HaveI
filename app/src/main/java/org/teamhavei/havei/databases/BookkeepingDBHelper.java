package org.teamhavei.havei.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.teamhavei.havei.Event.Account;
import org.teamhavei.havei.Event.Habit;


import java.util.ArrayList;
import java.util.List;


public class BookkeepingDBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DEBUG";

    public static final int DATABASE_VERSION = 2;
    public static final String DB_NAME = "Bookkeeping.db";
    private static final String TABLE_ACCOUNT = "Account";
    private static final String ACCOUNT_ID = "id";
    private static final String ACCOUNT_NAME = "name";
    private static final String ACCOUNT_TAG_ID = "tagid";
    private static final String ACCOUNT_TIME = "time";
    private static final String ACCOUNT_ICON_ID = "iconid";
    private static final String ACCOUNT_PM = "pm";
    private static final String CREATE_ACCOUNT =
            "create table " + TABLE_ACCOUNT + "(" +
                    ACCOUNT_ID + " integer primary key autoincrement," +//id
                    ACCOUNT_NAME + " text," +//习惯名
                    ACCOUNT_TAG_ID + " integer," +//标签id
                    ACCOUNT_TIME+ "text,"+
                    ACCOUNT_ICON_ID+"integer,"+
                    ACCOUNT_PM + " integer)"; //计算参数

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
    private ContentValues accountToValues(Account mAccount) {
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NAME,mAccount.getname());
        values.put(ACCOUNT_TAG_ID, mAccount.gettag());
        values.put(ACCOUNT_TIME, mAccount.gettime());
        values.put(ACCOUNT_ICON_ID, mAccount.getIconId());
        values.put(ACCOUNT_PM, mAccount.getPM());
        return values;
    }
    private List cursorToAccountList(Cursor cursor) {
        List<Account> mAccountList = new ArrayList<>();
        try {
            if (cursor.getCount() > 1) {
                Log.d(TAG, "cursorToAccont: Found more than one habit");
            }
            cursor.moveToFirst();
            do {
                Account mAccount = new Account();
                mAccount.setId(cursor.getInt(cursor.getColumnIndex(ACCOUNT_ID)));
                mAccount.setname(cursor.getString(cursor.getColumnIndex(ACCOUNT_NAME)));
                mAccount.settag(cursor.getInt(cursor.getColumnIndex(ACCOUNT_TAG_ID)));
                mAccount.setTime(cursor.getString(cursor.getColumnIndex(ACCOUNT_TIME)));
                mAccount.setIconId(cursor.getInt(cursor.getColumnIndex(ACCOUNT_ICON_ID)));
                mAccount.setPM(cursor.getInt(cursor.getColumnIndex(ACCOUNT_PM)));
                mAccountList.add(mAccount);
            } while (cursor.moveToNext());
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e(TAG, "cursorToHabitList: No such account", e);
        }
        return mAccountList;
    }
    public void insertAccount(Account mAccount) {
        db.insert(TABLE_ACCOUNT, null, accountToValues(mAccount));
    }
    public Account findAccountById(int id) {
        Cursor cursor = db.query(TABLE_ACCOUNT, null, ACCOUNT_ID + "= ?", new String[]{Integer.toString(id)}, null, null, null);
        Account mAccount = (Account) cursorToAccountList(cursor).get(0);
        cursor.close();
        return mAccount;
    }
    public void updateAccount(Account oldAccount, Account newAccount) {
        db.update(TABLE_ACCOUNT, accountToValues(newAccount), ACCOUNT_ID + " = ?", new String[]{Integer.toString(oldAccount.getid())});
    }

    public void deleteAccount(Account mAccount) {
        db.delete(TABLE_ACCOUNT, ACCOUNT_ID + " = ? ", new String[]{Integer.toString(mAccount.getid())});
    }


}




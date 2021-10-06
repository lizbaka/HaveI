package org.teamhavei.havei.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.teamhavei.havei.Event.BookAccount;
import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.IconAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kotlin.Unit;

public class BookkeepDBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DEBUG";

    public static final int DATABASE_VERSION = 6;
    public static final String DB_NAME = "Bookkeep.db";

    private static final String TABLE_BOOKKEEP = "Bookkeep";
    private static final String BOOKKEEP_ID = "id";
    private static final String BOOKKEEP_NAME = "name";
    private static final String BOOKKEEP_TAG_ID = "tagid";
    private static final String BOOKKEEP_TIME = "time";
    private static final String BOOKKEEP_PM = "pm";
    private static final String BOOKKEEP_ACCOUNT_BEL = "account";
    private static final String CREATE_BOOKKEEP =
            "create table " + TABLE_BOOKKEEP + "(" +
                    BOOKKEEP_ID + " integer primary key autoincrement," +//id
                    BOOKKEEP_NAME + " text," +//账目备注
                    BOOKKEEP_TAG_ID + " integer," +//账目标签
                    BOOKKEEP_TIME + " text," +//账目时间
                    BOOKKEEP_PM + " double," +//账目收支
                    BOOKKEEP_ACCOUNT_BEL + " integer)";//账目从属账户

    private static final String TABLE_BOOK_TAGS = "BookTags";
    private static final String BOOK_TAGS_ID = "id";
    private static final String BOOK_TAGS_ICON_ID = "icon_id";
    private static final String BOOK_TAGS_NAME = "name";
    private static final String BOOK_TAGS_DELETE = "del";
    private static final String BOOK_TAGS_TYPE = "type";
    private static final String CREATE_BOOK_TAGS =
            "create table " + TABLE_BOOK_TAGS + "(" +
                    BOOK_TAGS_ID + " integer primary key autoincrement," +//标签id
                    BOOK_TAGS_ICON_ID + " integer," +//图标id
                    BOOK_TAGS_NAME + " text," +//标签名称
                    BOOK_TAGS_DELETE + " integer," +//已删除：1 未删除：0
                    BOOK_TAGS_TYPE + " integer)";//支出：0 收入：1

    private static final String TABLE_BOOK_ACCOUNT = "BookAccount";
    private static final String BOOK_ACCOUNT_ID = "id";
    private static final String BOOK_ACCOUNT_NAME = "name";
    private static final String BOOK_ACCOUNT_ICON = "iconId";
    private static final String BOOK_ACCOUNT_INIT = "init";
    private static final String CREATE_BOOK_ACCOUNT =
            "create table " + TABLE_BOOK_ACCOUNT + "(" +
                    BOOK_ACCOUNT_ID + " integer primary key autoincrement," +//账户id
                    BOOK_ACCOUNT_NAME + " text," +//账户名称
                    BOOK_ACCOUNT_ICON + " integer," +//账户图标id
                    BOOK_ACCOUNT_INIT + " double)";//账户初值

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
        db.execSQL(CREATE_BOOK_ACCOUNT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //使用alter语句修改字段
    }

    //========Bookkeep相关功能:Begin========
    private ContentValues bookkeepToValues(Bookkeep mBookkeep) {
        ContentValues values = new ContentValues();
        values.put(BOOKKEEP_NAME, mBookkeep.getname());
        values.put(BOOKKEEP_TAG_ID, mBookkeep.gettag());
        values.put(BOOKKEEP_TIME, mBookkeep.gettime());
        values.put(BOOKKEEP_PM, mBookkeep.getPM());
        values.put(BOOKKEEP_ACCOUNT_BEL, mBookkeep.getAccount());
        return values;
    }

    private List<Bookkeep> cursorToBookkeepList(Cursor cursor) {
        List<Bookkeep> mBookkeepList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToBookkeepList: Found more than one");
                }
                while (cursor.moveToNext()) {
                    Bookkeep mBookkeep = new Bookkeep();
                    mBookkeep.setId(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_ID)));
                    mBookkeep.setname(cursor.getString(cursor.getColumnIndex(BOOKKEEP_NAME)));
                    mBookkeep.settag(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_TAG_ID)));
                    mBookkeep.setTime(cursor.getString(cursor.getColumnIndex(BOOKKEEP_TIME)));
                    mBookkeep.setPM(cursor.getDouble(cursor.getColumnIndex(BOOKKEEP_PM)));
                    mBookkeep.setAccount(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_ACCOUNT_BEL)));
                    mBookkeepList.add(mBookkeep);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "cursorToBookkeepList: No such keep");
        }
        return mBookkeepList;
    }

    public void insertBookkeep(Bookkeep mBookkeep) {
        db.insert(TABLE_BOOKKEEP, null, bookkeepToValues(mBookkeep));
    }

    public Bookkeep findBookkeepById(int id) {
        Cursor cursor = db.query(TABLE_BOOKKEEP, null, BOOKKEEP_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        Bookkeep mBookkeep = (Bookkeep) cursorToBookkeepList(cursor).get(0);
        cursor.close();
        return mBookkeep;
    }

    public double getIncomeByMonth(Date yearMonth) {
        String sYearMonth = UniToolKit.eventYearMonthFormatter(yearMonth);
        Cursor cursor = db.query(TABLE_BOOKKEEP, new String[]{"SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_PM + " > 0" + " AND " + BOOKKEEP_TIME + " LIKE ?", new String[]{sYearMonth + "%"}, null, null, null);
        cursor.moveToNext();
        return cursor.getDouble(cursor.getColumnIndex("SUM(" + BOOKKEEP_PM + ")"));
    }

    public double getExpenditureByMonth(Date yearMonth) {
        String sYearMonth = UniToolKit.eventYearMonthFormatter(yearMonth);
        Cursor cursor = db.query(TABLE_BOOKKEEP, new String[]{"SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_PM + " < 0" + " AND " + BOOKKEEP_TIME + " LIKE ?", new String[]{sYearMonth + "%"}, null, null, null);
        cursor.moveToNext();
        return -cursor.getDouble(cursor.getColumnIndex("SUM(" + BOOKKEEP_PM + ")"));
    }

    public double getExpenditureByDay(Date date) {
        String sDate = UniToolKit.eventDateFormatter(date);
        Cursor cursor = db.query(TABLE_BOOKKEEP, new String[]{"SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_PM + " < 0" + " AND " + BOOKKEEP_TIME + " = ?", new String[]{sDate}, null, null, null);
        cursor.moveToNext();
        return -cursor.getDouble(cursor.getColumnIndex("SUM(" + BOOKKEEP_PM + ")"));
    }

    public double getIncomeByDay(Date date) {
        String sDate = UniToolKit.eventDateFormatter(date);
        Cursor cursor = db.query(TABLE_BOOKKEEP, new String[]{"SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_PM + " > 0" + " AND " + BOOKKEEP_TIME + " = ?", new String[]{sDate}, null, null, null);
        cursor.moveToNext();
        return -cursor.getDouble(cursor.getColumnIndex("SUM(" + BOOKKEEP_PM + ")"));
    }

    public double getPMByAccount(int accountId) {
        Cursor cursor = db.query(TABLE_BOOKKEEP, new String[]{"SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_ACCOUNT_BEL + " = ?", new String[]{Integer.toString(accountId)}, BOOKKEEP_ACCOUNT_BEL, null, null);
        if (cursor.getCount() <= 0) {
            return 0;
        }
        cursor.moveToNext();
        return cursor.getDouble(cursor.getColumnIndex("SUM(" + BOOKKEEP_PM + ")"));
    }

    public int getBookkeepDay() {
        Cursor cursor = db.query(TABLE_BOOKKEEP, new String[]{"COUNT(" + BOOKKEEP_TIME + ")"}, null, null, BOOKKEEP_TIME, null, null);
        return cursor.getCount();
    }

    public List<Bookkeep> findAllBookkeep() {
        Cursor cursor = db.query(TABLE_BOOKKEEP, null, null, null, null, null, null);
        return cursorToBookkeepList(cursor);
    }

    //search by month
    public List<Bookkeep> findBookkeepByMonth(String sDate) {
        Cursor cursor = db.query(TABLE_BOOKKEEP, null, BOOKKEEP_TIME + " LIKE ?", new String[]{sDate + "%"}, null, null, BOOKKEEP_TIME + " DESC");
        return cursorToBookkeepList(cursor);
    }

    public List<Bookkeep> findBookkeepByMonth(Date date) {
        String sDate = UniToolKit.eventYearMonthFormatter(date);
        return findBookkeepByMonth(sDate);
    }

    public List<Bookkeep> findBookkeepByAccount(int accountId) {
        Cursor cursor = db.query(TABLE_BOOKKEEP, null, BOOKKEEP_ACCOUNT_BEL + " = ?", new String[]{Integer.toString(accountId)}, null, null, null);
        return cursorToBookkeepList(cursor);
    }

    public void updateBookkeep(Bookkeep oldBookkeep, Bookkeep newBookkeep) {
        db.update(TABLE_BOOKKEEP, bookkeepToValues(newBookkeep), BOOKKEEP_ID + " = ?", new String[]{Integer.toString(oldBookkeep.getid())});
    }

    public void updateBookkeep(int bookkeepId, Bookkeep newBookkeep) {
        db.update(TABLE_BOOKKEEP, bookkeepToValues(newBookkeep), BOOKKEEP_ID + " = ?", new String[]{Integer.toString(bookkeepId)});
    }

    public void deleteBookkeep(Bookkeep mBookkeep) {
        db.delete(TABLE_BOOKKEEP, BOOKKEEP_ID + " = ? ", new String[]{Integer.toString(mBookkeep.getid())});
    }

    public ArrayList<Double> getPMListByYear(int type, Date date) {
        ArrayList<Double> data = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        if (type == UniToolKit.BOOKKEEP_TAG_EXPENDITURE) {
            for (int i = 0; i < 12; i++) {
                data.add(getExpenditureByMonth(calendar.getTime()));
                calendar.add(Calendar.MONTH, 1);
            }
        } else {
            for (int i = 0; i < 12; i++) {
                data.add(getIncomeByMonth(calendar.getTime()));
                calendar.add(Calendar.MONTH, 1);
            }
        }
        return data;
    }

    public ArrayList<Double> getSurplusListByYear(Date date) {
        ArrayList<Double> surplusData = getPMListByYear(UniToolKit.BOOKKEEP_TAG_INCOME, date);
        ArrayList<Double> expenditureData = getPMListByYear(UniToolKit.BOOKKEEP_TAG_EXPENDITURE, date);
        for (int i = 0; i < surplusData.size(); i++) {
            surplusData.set(i, surplusData.get(i) - expenditureData.get(i));
        }
        return surplusData;
    }
    //========Bookkeep相关功能:end===============


    //========Book_Tag相关功能:Begin========
    private ContentValues bookTagToValues(BookTag mBookTag) {
        ContentValues values = new ContentValues();
        values.put(BOOK_TAGS_NAME, mBookTag.getName());
        values.put(BOOK_TAGS_ICON_ID, mBookTag.getIconId());
        values.put(BOOK_TAGS_DELETE, mBookTag.isDel() ? 1 : 0);
        values.put(BOOK_TAGS_TYPE, mBookTag.getType());
        return values;
    }

    private BookTag cursorToBookTag(Cursor cursor) {
        BookTag mBookTag = new BookTag();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToBookTag: Found more than one");
                }
                cursor.moveToFirst();
                mBookTag.setId(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_ID)));
                mBookTag.setName(cursor.getString(cursor.getColumnIndex(BOOK_TAGS_NAME)));
                mBookTag.setIconId(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_ICON_ID)));
                mBookTag.setDel(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_DELETE)) == 1);
                mBookTag.setType(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_TYPE)));
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "cursorToBookTag: No such BookTag");
        }
        return mBookTag;
    }

    private List<BookTag> cursorToBookTagList(Cursor cursor) {
        List<BookTag> tagList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToBookTagList: Found more than one");
                }
                while (cursor.moveToNext()) {
                    BookTag mBookTag = new BookTag();
                    mBookTag.setId(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_ID)));
                    mBookTag.setName(cursor.getString(cursor.getColumnIndex(BOOK_TAGS_NAME)));
                    mBookTag.setIconId(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_ICON_ID)));
                    mBookTag.setDel(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_DELETE)) == 1);
                    mBookTag.setType(cursor.getInt(cursor.getColumnIndex(BOOK_TAGS_TYPE)));
                    tagList.add(mBookTag);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "cursorToBookTagList: No suck BookTag");
        }
        return tagList;
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

    public List<BookTag> findAllBookTag(boolean excludeDeleted) {
        Cursor cursor;
        if (excludeDeleted) {
            cursor = db.query(TABLE_BOOK_TAGS, null, BOOK_TAGS_DELETE + " = ?", new String[]{"0"}, null, null, null);
        } else {
            cursor = db.query(TABLE_BOOK_TAGS, null, null, null, null, null, null);
        }
        if (cursor.getCount() <= 0) {
            BookTag defaultBookTag = new BookTag();
            defaultBookTag.setName(mContext.getString(R.string.default_expenditure_tag));
            defaultBookTag.setDel(false);
            defaultBookTag.setIconId(IconAdapter.DEFAULT_BOOKKEEP_TAG_ICON_ID);
            defaultBookTag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
            db.insert(TABLE_BOOK_TAGS, null, bookTagToValues(defaultBookTag));
            defaultBookTag.setName(mContext.getString(R.string.default_income_tag));
            defaultBookTag.setType(UniToolKit.BOOKKEEP_TAG_INCOME);
            insertBookTag(defaultBookTag);
            cursor.close();
            return findAllBookTag(excludeDeleted);
        }
        return cursorToBookTagList(cursor);
    }

    public List<BookTag> findAllBookTag(boolean excludeDeleted, int type) {
        Cursor cursor;
        if (excludeDeleted) {
            cursor = db.query(TABLE_BOOK_TAGS, null, BOOK_TAGS_DELETE + " = ?" + " AND " + BOOK_TAGS_TYPE + "= ?", new String[]{"0", Integer.toString(type)}, null, null, null);
        } else {
            cursor = db.query(TABLE_BOOK_TAGS, null, BOOK_TAGS_TYPE + " = ?", new String[]{Integer.toString(type)}, null, null, null);
        }
        if (cursor.getCount() <= 0) {
            BookTag defaultBookTag = new BookTag();
            if (type == UniToolKit.BOOKKEEP_TAG_EXPENDITURE) {
                defaultBookTag.setName(mContext.getString(R.string.default_expenditure_tag));
            } else {
                defaultBookTag.setName(mContext.getString(R.string.default_income_tag));
            }
            defaultBookTag.setDel(false);
            defaultBookTag.setIconId(IconAdapter.DEFAULT_BOOKKEEP_TAG_ICON_ID);
            defaultBookTag.setType(type);
            insertBookTag(defaultBookTag);
            cursor.close();
            return findAllBookTag(excludeDeleted, type);
        }
        return cursorToBookTagList(cursor);
    }

    public List<BookTag> findAllBookTag(boolean excludeDeleted, int type, boolean sortByPM) {
        if (!sortByPM) {
            return findAllBookTag(excludeDeleted, type);
        }
        List<BookTag> bookTagList = new ArrayList<>();
        Cursor cursor;
        if (type == UniToolKit.BOOKKEEP_TAG_EXPENDITURE) {
            cursor = db.query(TABLE_BOOKKEEP, new String[]{BOOKKEEP_TAG_ID, "SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_PM + " < 0", null, BOOKKEEP_TAG_ID, null, "SUM(" + BOOKKEEP_PM + ")");
        } else {
            cursor = db.query(TABLE_BOOKKEEP, new String[]{BOOKKEEP_TAG_ID, "SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_PM + " > 0", null, BOOKKEEP_TAG_ID, null, "SUM(" + BOOKKEEP_PM + ") DESC");
        }
        while (cursor.moveToNext()) {
            bookTagList.add(findBookTagById(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_TAG_ID))));
        }
        cursor.close();
        return bookTagList;
    }

    public void updateBookTag(BookTag oldBookTag, BookTag newBookTag) {
        db.update(TABLE_BOOK_TAGS, bookTagToValues(newBookTag), BOOK_TAGS_ID + " = ?", new String[]{Integer.toString(oldBookTag.getId())});
    }

    public void updateBookTag(int tagId, BookTag newBookTag) {
        db.update(TABLE_BOOK_TAGS, bookTagToValues(newBookTag), BOOK_TAGS_ID + " = ?", new String[]{Integer.toString(tagId)});
    }

    public void deleteBookTag(BookTag mBookTag) {
        BookTag delBookTag = mBookTag;
        delBookTag.setDel(true);
        updateBookTag(mBookTag, delBookTag);
    }

    public HashMap<String, Double> getTagDataByMonth(int type, Date date) {
        HashMap<String, Double> data = new HashMap<String, Double>();
        String sDate = UniToolKit.eventYearMonthFormatter(date);
        Cursor cursor;
        if (type == UniToolKit.BOOKKEEP_TAG_EXPENDITURE) {
            cursor = db.query(TABLE_BOOKKEEP, new String[]{BOOKKEEP_TAG_ID, "SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_PM + " < 0" + " AND " + BOOKKEEP_TIME + " LIKE ?", new String[]{sDate + "%"}, BOOKKEEP_TAG_ID, null, "SUM(" + BOOKKEEP_PM + ")");
        } else {
            cursor = db.query(TABLE_BOOKKEEP, new String[]{BOOKKEEP_TAG_ID, "SUM(" + BOOKKEEP_PM + ")"}, BOOKKEEP_PM + " > 0" + " AND " + BOOKKEEP_TIME + " LIKE ?", new String[]{sDate + "%"}, BOOKKEEP_TAG_ID, null, "SUM(" + BOOKKEEP_PM + ") DESC");
        }
        while (cursor.moveToNext()) {
            BookTag tag = findBookTagById(cursor.getInt(cursor.getColumnIndex(BOOKKEEP_TAG_ID)));
            data.put(tag.getName(), Math.abs(cursor.getDouble(cursor.getColumnIndex("SUM(" + BOOKKEEP_PM + ")"))));
        }
        return data;
    }
    //========Book_Tag相关功能:end========

    //========Book_Account相关功能:Begin========

    private ContentValues bookAccountToValues(BookAccount mBookAccount) {
        ContentValues values = new ContentValues();
        values.put(BOOK_ACCOUNT_NAME, mBookAccount.getName());
        values.put(BOOK_ACCOUNT_ICON, mBookAccount.getIconId());
        values.put(BOOK_ACCOUNT_INIT, mBookAccount.getInit());
        return values;
    }

    private List<BookAccount> cursorToBookAccountList(Cursor cursor) {
        List<BookAccount> accountList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            try {
                if (cursor.getCount() > 1) {
                    Log.d(TAG, "cursorToBookAccountList: Found more than one");
                }
                while (cursor.moveToNext()) {
                    BookAccount mBookAccount = new BookAccount();
                    mBookAccount.setId(cursor.getInt(cursor.getColumnIndex(BOOK_ACCOUNT_ID)));
                    mBookAccount.setName(cursor.getString(cursor.getColumnIndex(BOOK_ACCOUNT_NAME)));
                    mBookAccount.setIconId(cursor.getInt(cursor.getColumnIndex(BOOK_ACCOUNT_ICON)));
                    mBookAccount.setInit(cursor.getInt(cursor.getColumnIndex(BOOK_ACCOUNT_INIT)));
                    accountList.add(mBookAccount);
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "cursorToBookAccountList: No suck BookAccount");
        }
        return accountList;
    }

    public void insertBookAccount(BookAccount bookAccount) {
        db.insert(TABLE_BOOK_ACCOUNT, null, bookAccountToValues(bookAccount));
    }

    public BookAccount findBookAccountById(int id) {
        Cursor cursor = db.query(TABLE_BOOK_ACCOUNT, null, BOOK_ACCOUNT_ID + " = ?", new String[]{Integer.toString(id)}, null, null, null);
        List<BookAccount> list = cursorToBookAccountList(cursor);
        cursor.close();
        if (list.size() <= 0) {
            return new BookAccount();
        }
        return list.get(0);
    }

    public List<BookAccount> findAllBookAccount() {
        Cursor cursor = db.query(TABLE_BOOK_ACCOUNT, null, null, null, null, null, null);
        return cursorToBookAccountList(cursor);
    }

    public void updateBookAccount(int bookAccountId, BookAccount newAccount) {
        db.update(TABLE_BOOK_ACCOUNT, bookAccountToValues(newAccount), BOOK_ACCOUNT_ID + " = ?", new String[]{Integer.toString(bookAccountId)});
    }

    public void deleteBookAccount(int accountId) {
        if (accountId == UniToolKit.DEFAULT_ACCOUNT_ID) {
            return;
        }
        List<Bookkeep> bookkeepList = findBookkeepByAccount(accountId);
        for (Bookkeep bookkeep : bookkeepList) {
            bookkeep.setAccount(UniToolKit.DEFAULT_ACCOUNT_ID);
            updateBookkeep(bookkeep.getid(), bookkeep);
        }
        db.delete(TABLE_BOOK_ACCOUNT, BOOK_ACCOUNT_ID + " = ?", new String[]{Integer.toString(accountId)});
    }


    //========Book_Account相关功能:end==========

    public void initializeTag() {
        BookAccount account = new BookAccount();
        account.setId(UniToolKit.DEFAULT_ACCOUNT_ID);
        account.setName(mContext.getString(R.string.default_account));
        account.setIconId(IconAdapter.DEFAULT_ACCOUNT_ICON_ID);
        account.setInit(0);
        ContentValues values = bookAccountToValues(account);
        values.put(BOOK_ACCOUNT_ID, UniToolKit.DEFAULT_ACCOUNT_ID);
        db.insert(TABLE_BOOK_ACCOUNT, null, values);


        BookTag tag = new BookTag();
        tag.setName(mContext.getString(R.string.default_expenditure_tag));
        tag.setIconId(IconAdapter.DEFAULT_BOOKKEEP_TAG_ICON_ID);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName(mContext.getString(R.string.default_income_tag));
        tag.setIconId(IconAdapter.DEFAULT_BOOKKEEP_TAG_ICON_ID);
        tag.setType(UniToolKit.BOOKKEEP_TAG_INCOME);
        insertBookTag(tag);
        tag.setName("饮食");
        tag.setIconId(IconAdapter.ID_CS_STAPLE_FOOD);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("零食");
        tag.setIconId(IconAdapter.ID_CS_SNACKS);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("交通");
        tag.setIconId(IconAdapter.ID_CS_CAR_FARE);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("薪资");
        tag.setIconId(IconAdapter.ID_CS_SALARY);
        tag.setType(UniToolKit.BOOKKEEP_TAG_INCOME);
        insertBookTag(tag);
        tag.setName("奖金");
        tag.setIconId(IconAdapter.ID_CS_BONUS);
        tag.setType(UniToolKit.BOOKKEEP_TAG_INCOME);
        insertBookTag(tag);
        tag.setName("红包");
        tag.setIconId(IconAdapter.ID_CS_RED_PACKET);
        tag.setType(UniToolKit.BOOKKEEP_TAG_INCOME);
        insertBookTag(tag);
        tag.setName("收益");
        tag.setIconId(IconAdapter.ID_CS_LEND_OUT);
        tag.setType(UniToolKit.BOOKKEEP_TAG_INCOME);
        insertBookTag(tag);
        tag.setName("日用");
        tag.setIconId(IconAdapter.ID_CS_LAUNDRY_DETERGENT);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("住宿");
        tag.setIconId(IconAdapter.ID_CS_ACCOMMODATION);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("差旅");
        tag.setIconId(IconAdapter.ID_CS_AIR_TICKETS);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("衣物");
        tag.setIconId(IconAdapter.ID_CS_CLOTHING);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("娱乐");
        tag.setIconId(IconAdapter.ID_CS_GAME);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("锻炼");
        tag.setIconId(IconAdapter.ID_CS_EXERCISE);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("爱好");
        tag.setIconId(IconAdapter.ID_CS_HOBBY);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("医疗");
        tag.setIconId(IconAdapter.ID_CS_MEDICINE);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("宠物");
        tag.setIconId(IconAdapter.ID_CS_PETS);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
        tag.setName("学习");
        tag.setIconId(IconAdapter.ID_CS_TEXTBOOK);
        tag.setType(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        insertBookTag(tag);
    }
}




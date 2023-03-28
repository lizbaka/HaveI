package org.teamhavei.havei.UI.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.UI.fragment.FragmentBookkeepAccountList;
import org.teamhavei.havei.UI.fragment.FragmentNumPad;
import org.teamhavei.havei.UI.widget.WidgetBookkeepOverview;
import org.teamhavei.havei.event.BookAccount;
import org.teamhavei.havei.event.Bookkeep;
import org.teamhavei.havei.event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.util.Util;
import org.teamhavei.havei.util.IconManager;
import org.teamhavei.havei.UI.adapter.TagListAdapter;
import org.teamhavei.havei.database.BookkeepDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityBookkeepAdd extends BaseActivity {

    private static final int IO_TYPE_EXPENDITURE = Util.BOOKKEEP_TAG_EXPENDITURE;
    private static final int IO_TYPE_INCOME = Util.BOOKKEEP_TAG_INCOME;

    private static final int MODE_ADD = 0;
    private static final int MODE_MODIFY = 1;

    private static final String START_PARAM_BOOKKEEP_ID = "bookkeep_id";

    private int selectedTagId;
    private int ioJudge;
    private int mode = MODE_ADD;
    private int accountId;

    EditText bookTitleET;
    RecyclerView mTagRecycRV;
    RadioGroup IORG;
    TextView dateTV;
    MaterialCardView datePikMC;
    ImageView accountIcon;
    TextView accountName;

    Calendar calendar;
    BookkeepDBHelper bookDBHelper;
    List<HaveITag> mtaglist;
    TagListAdapter mTaglistAdapter;
    Bookkeep mBookkeep;

    FragmentNumPad numPad;
    FragmentBookkeepAccountList accountList;


    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityBookkeepAdd.class);
        context.startActivity(intent);
    }

    public static void startAction(Context context, int bookkeepId) {
        Intent intent = new Intent(context, ActivityBookkeepAdd.class);
        intent.putExtra(START_PARAM_BOOKKEEP_ID, bookkeepId);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_add);
        setSupportActionBar(findViewById(R.id.bookkeep_add_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        numPad = new FragmentNumPad(new FragmentNumPad.NumPadCallback() {
            @Override
            public void onConfirm(Double number) {
                updateBookkeep(number);
                finish();
            }
        }, FragmentNumPad.MODE_NORMAL);
        getSupportFragmentManager().beginTransaction().replace(R.id.bookkeep_add_numpad, numPad).commit();

        accountList = new FragmentBookkeepAccountList(new FragmentBookkeepAccountList.Callback() {
            @Override
            public void onBookAccountSelected(BookAccount bookAccount) {
                accountId = bookAccount.getId();
                accountIcon.setImageDrawable(new IconManager(ActivityBookkeepAdd.this).getIcon(bookDBHelper.findBookAccountById(accountId).getIconId()));
                accountName.setText(bookDBHelper.findBookAccountById(accountId).getName());
                accountList.dismiss();
            }

            @Override
            public void operate() {
                ActivitySettingsAccount.startAction(ActivityBookkeepAdd.this);
            }
        }, getString(R.string.account_mng));

        bookDBHelper = new BookkeepDBHelper(ActivityBookkeepAdd.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        calendar = Calendar.getInstance();

        initView();
        initListener();

        if (getIntent().getIntExtra(START_PARAM_BOOKKEEP_ID, -1) != -1) {
            mode = MODE_MODIFY;
            mBookkeep = bookDBHelper.findBookkeepById(getIntent().getIntExtra(START_PARAM_BOOKKEEP_ID, -1));
            selectedTagId = mBookkeep.gettag();
            bookTitleET.setText(mBookkeep.getname());
            calendar.setTime(Util.eventDateParser(mBookkeep.gettime()));
            ioJudge = mBookkeep.getPM() > 0 ? IO_TYPE_INCOME : IO_TYPE_EXPENDITURE;
            accountId = mBookkeep.getAccount();
            //与numpad相关的初始值操作放到onStart中，因为onCreate完成前fragment未完成创建
        } else {
            mode = MODE_ADD;
            mBookkeep = new Bookkeep();
            selectedTagId = bookDBHelper.findAllBookTag(true).get(0).getId();
            ioJudge = IO_TYPE_EXPENDITURE;
            accountId = Util.DEFAULT_ACCOUNT_ID;
        }
        mtaglist = new ArrayList<>();
        dateTV.setText(Util.eventDateFormatter(calendar.getTime()));
        mtaglist.addAll(bookDBHelper.findAllBookTag(true, ioJudge));
        accountIcon.setImageDrawable(new IconManager(ActivityBookkeepAdd.this).getIcon(bookDBHelper.findBookAccountById(accountId).getIconId()));
        accountName.setText(bookDBHelper.findBookAccountById(accountId).getName());

        initTagList();

    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onStart() {
        super.onStart();
        if (mode == MODE_MODIFY) {
            numPad.getNumberET().setText(String.format("%.2f", Math.abs(mBookkeep.getPM())));
            if (mBookkeep.getPM() > 0) {
                IORG.check(R.id.bookkeep_add_radio_income);
            } else {
                IORG.check(R.id.bookkeep_add_radio_expenditure);
            }
        } else {
            IORG.check(R.id.bookkeep_add_radio_expenditure);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        bookTitleET = findViewById(R.id.bookkeep_add_title);
        mTagRecycRV = findViewById(R.id.bookkeep_add_tag_recyc);
        IORG = findViewById(R.id.bookkeep_add_io);
        dateTV = findViewById(R.id.tv_select_date);
        datePikMC = findViewById(R.id.date_picker);
        accountIcon = findViewById(R.id.bookkeep_account_icon);
        accountName = findViewById(R.id.bookkeep_account_name);
    }

    public void initListener() {
        IORG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mtaglist.clear();
                switch (checkedId) {
                    case R.id.bookkeep_add_radio_expenditure:
                        ioJudge = IO_TYPE_EXPENDITURE;
                        numPad.getNumberET().setTextColor(ContextCompat.getColor(ActivityBookkeepAdd.this, R.color.red_500));
                        mtaglist.addAll(bookDBHelper.findAllBookTag(true, IO_TYPE_EXPENDITURE));
                        break;
                    case R.id.bookkeep_add_radio_income:
                        ioJudge = IO_TYPE_INCOME;
                        numPad.getNumberET().setTextColor(ContextCompat.getColor(ActivityBookkeepAdd.this, R.color.green_500));
                        mtaglist.addAll(bookDBHelper.findAllBookTag(true, IO_TYPE_INCOME));
                        break;
                }
                mTagRecycRV.getAdapter().notifyDataSetChanged();
            }
        });
        datePikMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(ActivityBookkeepAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        dateTV.setText(Util.eventDateFormatter(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                DatePicker picker = pickerDialog.getDatePicker();
                picker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                pickerDialog.show();
            }
        });
        findViewById(R.id.bookkeep_add_tag_mng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySettingsTagMng.startAction(ActivityBookkeepAdd.this, Util.TAG_TYPE_BOOKKEEP);
            }
        });

        findViewById(R.id.bookkeep_select_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountList.show(getSupportFragmentManager(), "TAG");
            }
        });
    }

    public void initTagList() {
        mTaglistAdapter = new TagListAdapter(mtaglist, ActivityBookkeepAdd.this, selectedTagId, TagListAdapter.ORIENTATION_VERTICAL, new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                selectedTagId = tag.getId();
            }
        });
        mTagRecycRV.setLayoutManager(new GridLayoutManager(ActivityBookkeepAdd.this, 4, GridLayoutManager.VERTICAL, false));
        mTagRecycRV.setAdapter(mTaglistAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mtaglist.clear();
        mtaglist.addAll(bookDBHelper.findAllBookTag(true, ioJudge));
        mTagRecycRV.getAdapter().notifyDataSetChanged();
    }

    private void updateBookkeep(Double value) {
        if (value == 0) {
            Toast.makeText(ActivityBookkeepAdd.this, R.string.bookkeep_add_invalid_money, Toast.LENGTH_SHORT).show();
            return;
        }
        double io = (ioJudge == IO_TYPE_INCOME ? 1 : -1);
        mBookkeep.setname(bookTitleET.getText().toString());
        mBookkeep.setPM(value * io);
        mBookkeep.settag(selectedTagId);
        mBookkeep.setTime(Util.eventDateFormatter(calendar.getTime()));
        mBookkeep.setAccount(accountId);
        if (mode == MODE_ADD) {
            bookDBHelper.insertBookkeep(mBookkeep);
        } else {
            bookDBHelper.updateBookkeep(mBookkeep.getid(), mBookkeep);
        }
    }

    @Override
    protected void onDestroy() {
        WidgetBookkeepOverview.updateWidgetAction(ActivityBookkeepAdd.this);
        super.onDestroy();
    }
}

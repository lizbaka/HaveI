package org.teamhavei.havei.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import org.teamhavei.havei.Event.BookCou;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.DatePickerDialog;
import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.sql.DatabaseMetaData;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ActivityBookkeepAnnualAccountDetail extends AppCompatActivity {
    private Button mShowDateBTN;
    private TextView mSelectDateTV;
    private String sDate;
    private BookkeepDBHelper dbHelper= new BookkeepDBHelper(ActivityBookkeepAnnualAccountDetail.this,BookkeepDBHelper.DB_NAME,null, BookkeepDBHelper.DATABASE_VERSION);
    private BookCou mBookCou;
    private List<Bookkeep> mBookList;
    private String initTime;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_annual_account_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_bookkeep_annual_account_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);




        mShowDateBTN.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivityBookkeepAnnualAccountDetail.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        String textString = String.format("选择年月：%d-%d\n", startYear,
                                startMonthOfYear + 1);
                        mSelectDateTV.setText(textString);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();

                sDate=mSelectDateTV.getText().toString();


            }
        });

    }


    void init()
    {   mShowDateBTN = (Button) findViewById(R.id.btn_show_date);
        mSelectDateTV = (TextView) findViewById(R.id.tv_select_date);




        @SuppressLint("SimpleDateFormat") SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyy-MM");
        sDate=sDateFormat.format(new java.util.Date());
        mBookCou=dbHelper.findBookcouByMonth(sDate);














    }
}
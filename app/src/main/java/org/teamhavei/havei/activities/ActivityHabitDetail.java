package org.teamhavei.havei.activities;

import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.teamhavei.havei.R;
import org.teamhavei.havei.habit.HabitDBHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ActivityHabitDetail extends BaseActivity {

    private Toolbar mToolbar;
    private MaterialCalendarView mCalendar;
    private String habitName;
    private HabitDBHelper dbHelper;

    public static void startAction(Context context, String habitName){
        Intent intent = new Intent(context, ActivityHabitDetail.class);
        intent.putExtra("habit_name",habitName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);


        mToolbar = findViewById(R.id.habit_detail_toolbar);
        mToolbar.inflateMenu(R.menu.habit_detail_toolbar);
        setSupportActionBar(mToolbar);
        mCalendar = findViewById(R.id.habit_detail_calendar);

        Intent intent = getIntent();
        habitName = intent.getStringExtra("habit_name");
        getSupportActionBar().setTitle(habitName);

        dbHelper = new HabitDBHelper(this,HabitDBHelper.DB_NAME,null,HabitDBHelper.DATABASE_VERSION);

        showExecutionDate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.habit_detail_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showExecutionDate(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("HabitExecs",new String[]{"date"},"name=?",new String[]{habitName},null,null,null);
        if(cursor.moveToFirst()){
            do {
                String dateString = cursor.getString(cursor.getColumnIndex("date"));
                int year = Integer.parseInt(dateString.substring(0,4));
                int month = Integer.parseInt(dateString.substring(5,7));
                int day = Integer.parseInt(dateString.substring(8,10));
                mCalendar.setDateSelected(CalendarDay.from(year,month,day),true);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
}
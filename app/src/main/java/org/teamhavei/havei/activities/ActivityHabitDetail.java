package org.teamhavei.havei.activities;

import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.teamhavei.havei.R;

public class ActivityHabitDetail extends BaseActivity {

    Toolbar mToolbar;
    MaterialCalendarView mCalendar;
    String habitName;

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
        mToolbar.setTitle(habitName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.habit_detail_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
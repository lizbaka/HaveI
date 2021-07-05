package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.EventDBHelper;

public class ActivityTest extends BaseActivity implements View.OnClickListener{

    static public void startAction(Context context){
        Intent intent = new Intent(context,ActivityTest.class);
        context.startActivity(intent);
    }

    EventDBHelper eventDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        eventDBHelper = new EventDBHelper(ActivityTest.this,EventDBHelper.DB_NAME,null,EventDBHelper.DATABASE_VERSION);
        Button btn1 = findViewById(R.id.test_btn1);
        btn1.setOnClickListener(this);
        Button btn2 = findViewById(R.id.test_btn2);
        btn2.setOnClickListener(this);
        Button btn3 = findViewById(R.id.test_btn3);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Habit habit;
        Habit habit1;
        switch(v.getId()){
            case R.id.test_btn1:
                habit = new Habit();
                habit.setName("name1");
                habit.setReminderTime("11:22");
                habit.setRepeatTimes(5);
                habit.setRepeatUnit(5);
                habit.setTagId(3);
                habit.showHabitInformation();
                eventDBHelper.insertHabit(habit);
                break;
            case R.id.test_btn2:
                habit = eventDBHelper.findHabitById(1);
                habit.showHabitInformation();
                break;
            case R.id.test_btn3:
                habit = eventDBHelper.findHabitById(1);
                habit1 = eventDBHelper.findHabitById(1);
                habit1.setId(3);
                habit1.setName("name2");
                eventDBHelper.updateHabit(habit,habit1);
        }
    }
}

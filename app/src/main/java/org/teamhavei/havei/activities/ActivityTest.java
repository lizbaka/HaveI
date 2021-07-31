package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.Todo;
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
        eventDBHelper = new EventDBHelper(ActivityTest.this,EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
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
        Todo todo;
        switch(v.getId()){
            case R.id.test_btn1:
                todo = new Todo();
                todo.setName("todo4");
                todo.setDateTime("2021-07-18 16:20");
                todo.setReminderDateTime("2021-07-18 16:02");
                todo.setTagId(1);
                todo.setDone(false);
                eventDBHelper.insertTodo(todo);
                break;
            case R.id.test_btn2:
                habit = eventDBHelper.findHabitById(1);
                habit.showHabitInformation();
                break;
            case R.id.test_btn3:
                ActivityHabitList.startAction(this);
        }
    }
}

package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    }

    @Override
    public void onClick(View v) {
        Habit habit;
        Todo todo;
        switch(v.getId()){
            case R.id.test_insert_todo:
                todo = new Todo();
                todo.setName("todo4");
                todo.setDateTime("2021-07-18 16:20");
                todo.setReminderDateTime("2021-07-18 16:02");
                todo.setTagId(1);
                todo.setDone(false);
                eventDBHelper.insertTodo(todo);
                break;
            case R.id.test_get_habit:
                habit = eventDBHelper.findHabitById(1);
                habit.showHabitInformation();
                break;
            case R.id.test_proverb_list:
                ActivityProverbList.startAction(this);
                break;
            case R.id.test_habit:
                ActivityHabitMain.startAction(this);
                break;
            case R.id.test_main_event:
                ActivityMainEvent.startAction(this);
                break;
        }
    }
}

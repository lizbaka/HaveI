package org.teamhavei.havei.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.HabitCardAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.List;

public class ActivityHabitList extends BaseActivity {

    RecyclerView habitCardList;
    HabitCardAdapter habitCardAdapter;
    EventDBHelper dbHelper;
    List<Habit> habitList;

    public static void startAction(Context context){
        Intent intent = new Intent(context, ActivityHabitList.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);
        habitCardList = findViewById(R.id.habit_list_card_list);
        dbHelper = new EventDBHelper(this,EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
        setSupportActionBar(findViewById(R.id.habit_list_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        habitList = dbHelper.findAllHabit();
        habitCardList.setLayoutManager(new LinearLayoutManager(this));
        habitCardAdapter = new HabitCardAdapter(dbHelper.findAllHabit(),ActivityHabitList.this);
        habitCardList.setAdapter(habitCardAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Habit> newHabitList = dbHelper.findAllHabit();
        if(!habitList.equals(newHabitList)){
            habitList = newHabitList;
        }
        habitCardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
package org.teamhavei.havei.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.HabitCardAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.List;

public class ActivityHabitList extends BaseActivity {

    RecyclerView habitCardListRV;
    HabitCardAdapter habitCardAdapter;
    ExtendedFloatingActionButton fab;
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
        dbHelper = new EventDBHelper(this,EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
        setSupportActionBar(findViewById(R.id.habit_list_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        habitCardListRV = findViewById(R.id.habit_list_card_list);
        fab = findViewById(R.id.habit_list_add);

        habitList = dbHelper.findAllHabit();
        habitCardListRV.setLayoutManager(new LinearLayoutManager(this));
        habitCardAdapter = new HabitCardAdapter(dbHelper.findAllHabit(),ActivityHabitList.this);
        habitCardListRV.setAdapter(habitCardAdapter);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ActivityModifyHabit.startAction(ActivityHabitList.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Habit> newHabitList = dbHelper.findAllHabit();
        habitList.clear();
        habitList.addAll(newHabitList);
        habitCardAdapter.notifyDataSetChanged();
        habitCardListRV.setAdapter(habitCardAdapter);
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
package org.teamhavei.havei.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.HabitCardAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

public class ActivityHabitList extends BaseActivity {

    RecyclerView habitCardList;
    EventDBHelper dbHelper;

    public static void startAction(Context context){
        Intent intent = new Intent(context, ActivityHabitList.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_list);
        habitCardList = findViewById(R.id.habit_list_card_list);
        dbHelper = new EventDBHelper(this,EventDBHelper.DB_NAME,null,EventDBHelper.DB);
        setSupportActionBar(findViewById(R.id.habit_list_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        habitCardList.setLayoutManager(new LinearLayoutManager(this));
        HabitCardAdapter habitCardAdapter = new HabitCardAdapter(dbHelper.findAllHabit(),ActivityHabitList.this);
        habitCardList.setAdapter(habitCardAdapter);
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
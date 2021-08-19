package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.teamhavei.havei.R;
import org.teamhavei.havei.fragments.FragmentHabitList;
import org.teamhavei.havei.fragments.FragmentTodayHabit;

public class ActivityHabitMain extends BaseActivity {

    BottomNavigationView navigationView;

    public static void startAction(Context context){
        Intent intent = new Intent(context, ActivityHabitMain.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_main);
        setSupportActionBar(findViewById(R.id.habit_main_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        navigationView.setItemIconTintList(null);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.habit_main_bottom_nav_today:
                        getSupportActionBar().setTitle(R.string.today_habit_title);
                        replaceFragment(new FragmentTodayHabit());
                        return true;
                    case R.id.habit_main_bottom_nav_lib:
                        getSupportActionBar().setTitle(R.string.habit_list_title);
                        replaceFragment(new FragmentHabitList());
                        return true;
                    case R.id.habit_main_bottom_nav_record:
                        // TODO: 2021.08.18 等待接入
                        return true;
                }
                return false;
            }
        });
        navigationView.setSelectedItemId(R.id.habit_main_bottom_nav_today);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    private void initView() {
        navigationView = findViewById(R.id.habit_main_bottom_navigation);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.habit_main_fragment, fragment);
        transaction.commit();
    }
}
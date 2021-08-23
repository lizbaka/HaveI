package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.teamhavei.havei.R;
import org.teamhavei.havei.fragments.FragmentHabitDoneRecordTable;
import org.teamhavei.havei.fragments.FragmentHabitList;
import org.teamhavei.havei.fragments.FragmentTodayHabit;

public class ActivityHabitMain extends BaseActivity {

    BottomNavigationView navigationView;
    TabLayout tabLayout;

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
                        tabLayout.setVisibility(View.GONE);
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_white);
                        findViewById(R.id.habit_main_appbar).setBackgroundColor(ContextCompat.getColor(ActivityHabitMain.this,R.color.amber_500));

                        getSupportActionBar().setTitle(R.string.today_habit_title);
                        replaceFragment(new FragmentTodayHabit());
                        return true;
                    case R.id.habit_main_bottom_nav_lib:
                        tabLayout.setVisibility(View.GONE);
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_white);
                        findViewById(R.id.habit_main_appbar).setBackgroundColor(ContextCompat.getColor(ActivityHabitMain.this,R.color.amber_500));


                        getSupportActionBar().setTitle(R.string.habit_list_title);
                        replaceFragment(new FragmentHabitList());
                        return true;
                    case R.id.habit_main_bottom_nav_record:
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24_black);
                        findViewById(R.id.habit_main_appbar).setBackgroundColor(ContextCompat.getColor(ActivityHabitMain.this,R.color.transparent));
                        tabLayout.setVisibility(View.VISIBLE);

                        getSupportActionBar().setTitle("");;
                        replaceFragment(new FragmentHabitDoneRecordTable(getSupportActionBar(),tabLayout));
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
        tabLayout = findViewById(R.id.habit_main_tab_layout);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.habit_main_fragment, fragment);
        transaction.commit();
    }

    private void configDoneRecordTable(){

    }
}
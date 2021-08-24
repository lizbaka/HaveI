package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.databases.EventDBHelper;
import org.teamhavei.havei.databases.UtilDBHelper;
import org.teamhavei.havei.services.HaveITimeWatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivityMain extends BaseActivity {

    private String habitNotificationChannelName;
    private String todoNotificationChannelName;

    EventDBHelper eventDBHelper;
    UtilDBHelper utilDBHelper;
    SharedPreferences pref;
    IconAdapter iconAdapter;

    Toolbar mToolbar;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
    NavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventDBHelper = new EventDBHelper(ActivityMain.this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        utilDBHelper = new UtilDBHelper(ActivityMain.this, UtilDBHelper.DB_NAME, null, UtilDBHelper.DB_VERSION);
        iconAdapter = new IconAdapter(ActivityMain.this);

        initView();

        initToolbar();
        initNavigationView();

        /* 处理基本组件 */
        initBasicPart();

        configGreetingCard();
        configProverb();
        configTodoCard();
        configHabitCard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configGreetingCard();
        configTodoCard();
        configHabitCard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_toolbar_analyze:
                // TODO: 2021.08.24 数据分析功能完成后接入
                Toast.makeText(this, "数据分析功能：敬请期待", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavView)) {
            mDrawerLayout.closeDrawer(mNavView);
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNotificationChannel() {
        todoNotificationChannelName = getResources().getString(R.string.todo_reminder_notification_channel_name);
        habitNotificationChannelName = getResources().getString(R.string.habit_reminder_notification_channel_name);
        NotificationChannel todoChannel = new NotificationChannel(UniToolKit.TODO_NOTIFICATION_CHANNEL_ID, todoNotificationChannelName, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationChannel habitChannel = new NotificationChannel(UniToolKit.HABIT_NOTIFICATION_CHANNEL_ID, habitNotificationChannelName, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(todoChannel);
        manager.createNotificationChannel(habitChannel);

    }

    private void startServices() {
        Intent startTimeWatcher = new Intent(ActivityMain.this, HaveITimeWatcher.class);
        startService(startTimeWatcher);
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.main_drawer_open, R.string.main_drawer_close);
        mActionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
    }

    private void initNavigationView() {
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_settings:
                        Toast.makeText(ActivityMain.this, "设置：敬请期待", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_menu_help:
                        ActivityTest.startAction(ActivityMain.this);
                        return true;
                }
                return false;
            }
        });
        mNavView.setItemIconTintList(null);
    }

    private void initView() {
        mNavView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.main_drawer);
        mToolbar = findViewById(R.id.main_toolbar);
    }

    private void initBasicPart() {
        startServices();
        if (Build.VERSION.SDK_INT >= 26) {
            initNotificationChannel();
        }
        pref = getSharedPreferences("settings", MODE_PRIVATE);
        if (pref.getBoolean(getString(R.string.pref_first_run), true)) {
            firstRun();
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(getString(R.string.pref_first_run), false);
            editor.apply();
        }
    }

    void configGreetingCard() {
        TextView greetingTimeTV = findViewById(R.id.greeting_card_time);
        TextView greetingSecTV = findViewById(R.id.greeting_card_secondary);
        ImageView greetingIconIV = findViewById(R.id.greeting_card_icon);
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 5 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            greetingTimeTV.setText(R.string.greeting_morning);
            greetingSecTV.setText(R.string.greeting_morning_secondary);
            greetingIconIV.setImageResource(R.drawable.star);
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 12 && calendar.get(Calendar.HOUR_OF_DAY) < 14) {
            greetingTimeTV.setText(R.string.greeting_noon);
            greetingSecTV.setText(R.string.greeting_noon_secondary);
            greetingIconIV.setImageResource(R.drawable.hs_sun3);
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 14 && calendar.get(Calendar.HOUR_OF_DAY) < 18) {
            greetingTimeTV.setText(R.string.greeting_afternoon);
            greetingSecTV.setText(R.string.greeting_afternoon_secondary);
            greetingIconIV.setImageResource(R.drawable.hs_paper_airplane);
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 18 && calendar.get(Calendar.HOUR_OF_DAY) < 22) {
            greetingTimeTV.setText(R.string.greeting_evening);
            greetingSecTV.setText(R.string.greeting_evening_secondary);
            greetingIconIV.setImageResource(R.drawable.cs_astronomy);
        } else if (calendar.get(Calendar.HOUR_OF_DAY) >= 22 || calendar.get(Calendar.HOUR_OF_DAY) < 5) {
            greetingTimeTV.setText(R.string.greeting_midnight);
            greetingSecTV.setText(R.string.greeting_midnight_secondary);
            greetingIconIV.setImageResource(R.drawable.hs_moon3);
        }
    }

    private void configTodoCard() {
        List<Todo> todoList = eventDBHelper.findUndoneTodoByDate(new Date());
        if (todoList != null && !todoList.isEmpty()) {
            Todo todo = todoList.get(0);
            findViewById(R.id.main_todo_card).setVisibility(View.VISIBLE);
            findViewById(R.id.main_empty_todo).setVisibility(View.GONE);
            TextView todoTitleTV = findViewById(R.id.todo_card_title);
            TextView todoTagTV = findViewById(R.id.todo_card_tag);
            TextView todoTimeTV = findViewById(R.id.todo_card_time);
            ImageView todoIconIV = findViewById(R.id.todo_card_icon);
            todoTitleTV.setText(todo.getName());
            todoTagTV.setText(eventDBHelper.findEventTagById(todo.getTagId()).getName());
            todoTimeTV.setText(todo.getDateTime().substring(11, 16));
            todoIconIV.setImageDrawable(iconAdapter.getIcon(eventDBHelper.findEventTagById(todo.getTagId()).getIconId()));

            findViewById(R.id.main_todo_card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityTodoDetail.startAction(ActivityMain.this,todo.getId());
                }
            });
        } else {
            findViewById(R.id.main_todo_card).setVisibility(View.GONE);
            findViewById(R.id.main_empty_todo).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.main_manage_todo_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMainEvent.startAction(ActivityMain.this);
            }
        });
    }

    private void configHabitCard(){
        List<Habit> habitList = eventDBHelper.findUnfinishedHabit(Calendar.getInstance());
        GridLayout habitGL = findViewById(R.id.main_habitGL);
        habitGL.removeAllViews();
        if (habitList != null && !habitList.isEmpty()) {
            habitGL.setVisibility(View.VISIBLE);
            findViewById(R.id.main_empty_habit).setVisibility(View.GONE);

            habitGL.setColumnCount(habitList.size() <= 4 ? habitList.size() : 4);
            View child;
            for (int i = 0; i < (habitList.size() <= 4 ? habitList.size() : 4); i++) {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.width = 0;
                params.columnSpec = GridLayout.spec(i, 1f);
                child = LayoutInflater.from(ActivityMain.this).inflate(R.layout.dynamic_icon_title_secondary, null);
                ImageView iconIV = child.findViewById(R.id.icon_title_icon);
                View iconContainerV = child.findViewById(R.id.icon_title_icon_container);
                iconContainerV.setBackgroundTintList(ContextCompat.getColorStateList(ActivityMain.this, R.color.habit_finish_icon_background_green));
                iconContainerV.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                TextView titleTV = child.findViewById(R.id.icon_title_title);

                Habit mHabit = habitList.get(i);
                iconIV.setImageDrawable(iconAdapter.getIcon(eventDBHelper.findEventTagById(mHabit.getTagId()).getIconId()));
                if (eventDBHelper.isHabitDone(mHabit.getId(), Calendar.getInstance().getTime())) {
                    iconContainerV.setSelected(true);
                }
                titleTV.setText(mHabit.getName());

                habitGL.addView(child, params);
            }
        } else {
            habitGL.setVisibility(View.GONE);
            findViewById(R.id.main_empty_habit).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.main_manage_habit_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHabitMain.startAction(ActivityMain.this);
            }
        });
    }

    // TODO: 2021.08.24 实现网络功能后考虑更改逻辑
    private void configProverb() {
        TextView proverbTV = findViewById(R.id.proverb_card_proverb);
        proverbTV.setText(utilDBHelper.pickOneProverb());
        findViewById(R.id.main_proverb_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityProverbList.startAction(ActivityMain.this, proverbTV.getText().toString());
            }
        });
        findViewById(R.id.proverb_card_favorite).setVisibility(View.GONE);
    }

    private void firstRun() {
        // TODO: 2021.08.07 首次运行函数。待实现功能：教程、从自带的数据库中引入数据
    }
}
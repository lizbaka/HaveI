package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.databases.EventDBHelper;
import org.teamhavei.havei.services.HaveITimeWatcher;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends BaseActivity{

    private String habitNotificationChannelName;
    private String todoNotificationChannelName;


    Toolbar mToolbar;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
    NavigationView mNavView;
    EventDBHelper eventDBHelper;


    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //处理界面，需要先执行
        initDrawer();
        initToolbar();
        initNavigationView();

        startServices();
        if(Build.VERSION.SDK_INT >= 26) {
            initNotificationChannel();
        }

        eventDBHelper = new EventDBHelper(ActivityMain.this,EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNotificationChannel(){
        todoNotificationChannelName = getResources().getString(R.string.todo_reminder_notification_channel_name);
        habitNotificationChannelName = getResources().getString(R.string.habit_reminder_notification_channel_name);
            NotificationChannel todoChannel = new NotificationChannel(UniToolKit.TODO_NOTIFICATION_CHANNEL_ID, todoNotificationChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel habitChannel = new NotificationChannel(UniToolKit.HABIT_NOTIFICATION_CHANNEL_ID, habitNotificationChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(todoChannel);
            manager.createNotificationChannel(habitChannel);

    }

    private void startServices(){
        Intent startTimeWatcher = new Intent(ActivityMain.this, HaveITimeWatcher.class);
        startService(startTimeWatcher);
    }

    private void initDrawer(){

        mDrawerLayout = findViewById(R.id.main_drawer);

    }

    private void initToolbar(){
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.main_drawer_open, R.string.main_drawer_close);
        mActionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
    }

    private void initNavigationView(){
        mNavView = findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_menu_theme:
                        Toast.makeText(ActivityMain.this,"主题切换功能：敬请期待",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_menu_settings:
                        Toast.makeText(ActivityMain.this,"设置：敬请期待",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_menu_help:
                        ActivityTest.startAction(ActivityMain.this);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_toolbar_analyze:
                Toast.makeText(this,"数据分析功能：敬请期待",Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mNavView)){
            mDrawerLayout.closeDrawer(mNavView);
        }
        else {
            super.onBackPressed();
        }
    }
}
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
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.databases.EventDBHelper;
import org.teamhavei.havei.databases.UtilDBHelper;
import org.teamhavei.havei.services.HaveITimeWatcher;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        configBookkeepCard();
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
                        ActivitySettings.startAction(ActivityMain.this);
                        return true;
                    case R.id.nav_menu_report:
                        ActivityReport.startAction(ActivityMain.this);
                        return true;
                    case R.id.nav_menu_help:
                        ActivityHelp.startAction(ActivityMain.this);
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
        pref = getSharedPreferences(UniToolKit.PREF_SETTINGS, MODE_PRIVATE);
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
        List<Todo> todoList = eventDBHelper.findTodoAfterToday();
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
                    ActivityTodoDetail.startAction(ActivityMain.this, todo.getId());
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

    private void configHabitCard() {
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
                iconContainerV.setBackgroundTintList(ContextCompat.getColorStateList(ActivityMain.this, R.color.habit_icon_state_list));
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

    private void configBookkeepCard(){
        findViewById(R.id.main_bookkeep_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this,ActivityBookkeep.class);
                startActivity(intent);
            }
        });

    }

    private void firstRun() {
        // TODO: 2021.08.07 首次运行函数。待实现功能：教程、从自带的数据库中引入数据
        eventDBHelper.initializeTag();
    }

    private void configProverb() {
        ImageView favIcon = findViewById(R.id.proverb_card_favorite);
        TextView proverbTV = findViewById(R.id.proverb_card_proverb);
        getProverbSentence((int) (Math.random() * 3) % 3, proverbTV, favIcon);
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utilDBHelper.switchProverb(proverbTV.getText().toString())) {
                    favIcon.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                } else {
                    favIcon.setImageResource(R.drawable.ic_baseline_favorite_24_white);
                }
            }
        });
        findViewById(R.id.main_proverb_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utilDBHelper.isProverbStored(proverbTV.getText().toString())) {
                    ActivityProverbList.startAction(ActivityMain.this, proverbTV.getText().toString());
                } else {
                    ActivityProverbList.startAction(ActivityMain.this);
                }
            }
        });
        findViewById(R.id.main_proverb_card).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getProverbSentence((int) (Math.random() * 3) % 3, proverbTV, favIcon);
                return true;
            }
        });
    }

    /**
     * @param type 0:from database; 1:hitokoto 2:shanbay;
     * @return
     */
    private void getProverbSentence(int type, TextView proverbTV, ImageView favIcon) {
        final int MSG_UPDATE_PROVERB = 1;
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MSG_UPDATE_PROVERB:
                        proverbTV.setText((String) msg.obj);
                        if (utilDBHelper.isProverbStored((String) msg.obj)) {
                            favIcon.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                        } else {
                            favIcon.setImageResource(R.drawable.ic_baseline_favorite_24_white);
                        }
                        break;
                }
            }
        };
        switch (type) {
            case 0:
                Message message = new Message();
                message.what = MSG_UPDATE_PROVERB;
                message.obj = utilDBHelper.pickOneProverb();
                handler.sendMessage(message);
                break;
            case 1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();
                            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://v1.hitokoto.cn/").newBuilder();
                            urlBuilder.addQueryParameter("c", "d");
                            urlBuilder.addQueryParameter("c", "h");
                            urlBuilder.addQueryParameter("c", "i");
                            urlBuilder.addQueryParameter("c", "k");
                            Request request = new Request.Builder()
                                    .url(urlBuilder.build()).build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    Message message = new Message();
                                    message.what = MSG_UPDATE_PROVERB;
                                    message.obj = utilDBHelper.pickOneProverb();
                                    handler.sendMessage(message);
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    String responseString = response.body().string();
                                    JSONObject jsonObject = null;
                                    Message message = new Message();
                                    message.what = MSG_UPDATE_PROVERB;
                                    try {
                                        jsonObject = new JSONObject(responseString);
                                        message.obj = jsonObject.getString("hitokoto") + "\n——" + jsonObject.getString("from");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        message.obj = utilDBHelper.pickOneProverb();
                                    }
                                    handler.sendMessage(message);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case 2:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final long START_DATE_MILLI = 1477756800000l;
                            long delta = Calendar.getInstance().getTimeInMillis() - START_DATE_MILLI;
                            Calendar goal = Calendar.getInstance();
                            goal.setTimeInMillis((long) (Math.random() * delta) + START_DATE_MILLI);
                            OkHttpClient client = new OkHttpClient();
                            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://apiv3.shanbay.com/weapps/dailyquote/quote/").newBuilder();
                            urlBuilder.addQueryParameter("date",UniToolKit.eventDateFormatter(goal.getTime()));
                            Request request = new Request.Builder()
                                    .url(urlBuilder.build()).build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    Message message = new Message();
                                    message.what = MSG_UPDATE_PROVERB;
                                    message.obj = utilDBHelper.pickOneProverb();
                                    handler.sendMessage(message);
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    String responseString = response.body().string();
                                    JSONObject jsonObject = null;
                                    Message message = new Message();
                                    message.what = MSG_UPDATE_PROVERB;
                                    try {
                                        jsonObject = new JSONObject(responseString);
                                        message.obj = jsonObject.getString("translation") + "\n——" + jsonObject.getString("author");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        message.obj = utilDBHelper.pickOneProverb();
                                    }
                                    handler.sendMessage(message);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }
}
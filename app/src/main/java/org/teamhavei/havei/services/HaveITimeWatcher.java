package org.teamhavei.havei.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.activities.ActivityHabitDetail;
import org.teamhavei.havei.activities.ActivityTodoDetail;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HaveITimeWatcher extends Service {

    private static final String TAG = "DEBUG";

    /*
        通知ID生成方法：
        date.getTime()+ SEED * [Event]id
     */
    private static final int HABIT_NOTIFICATION_SEED = 114;
    private static final int TODO_NOTIFICATION_SEED = 514;
    private static final int TODO_REMINDER_NOTIFICATION_SEED = 1919;

    private BroadcastReceiver timeChangeReceiver;
    private EventDBHelper eventDBHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: HaveITimeWatcher created");

        initEventReminder();

    }

    private void initEventReminder(){
        eventDBHelper = new EventDBHelper(this,EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        timeChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(Intent.ACTION_TIME_TICK.equals(intent.getAction()) || Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) || Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())){
                    checkTodo();
                    checkHabit();
                }
            }
        };
        registerReceiver(timeChangeReceiver,intentFilter);
    }

    private void checkTodo() {
        Date date = new Date();
        List<Todo> todoList = eventDBHelper.findTodoByDatetime(date);
        List<Todo> todoListReminder = eventDBHelper.findTodoByReminderDatetime(date);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        for (Todo i : todoList) {
            if(i.isDone()){
                continue;
            }
            Notification notification = new NotificationCompat.Builder(this, UniToolKit.TODO_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.todo_notification_title))
                    .setContentText(i.getName() + getResources().getString(R.string.todo_notification_content))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
            manager.notify((int)new Date().getTime() + TODO_NOTIFICATION_SEED * i.getId(), notification);
        }
        for (Todo i : todoListReminder) {
            if(i.isDone()){
                continue;
            }
            Intent intent = new Intent(HaveITimeWatcher.this, ActivityTodoDetail.class);
            intent.putExtra(ActivityTodoDetail.START_PARAM_TODO_ID,i.getId());
            PendingIntent pi = PendingIntent.getActivity(HaveITimeWatcher.this,0,intent, 0);
            Notification notification = new NotificationCompat.Builder(this, UniToolKit.TODO_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.todo_advance_notification_title))
                    .setContentText(i.getName() + getResources().getString(R.string.todo_advance_notification_content1) + i.getDateTime() + getResources().getString(R.string.todo_advance_notification_content2))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .build();
            manager.notify((int)new Date().getTime() + TODO_REMINDER_NOTIFICATION_SEED * i.getId(), notification);
        }
    }

    private void checkHabit(){
        List<Habit> habitList = eventDBHelper.findHabitByReminderTime(new Date());
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        for (Habit i : habitList) {
            if(eventDBHelper.checkHabitFinishAt(i.getId(), Calendar.getInstance())){
                continue;
            }
            Intent intent = new Intent(HaveITimeWatcher.this, ActivityHabitDetail.class);
            intent.putExtra(ActivityHabitDetail.START_PARAM_HABIT_ID,i.getId());
            PendingIntent pi = PendingIntent.getActivity(HaveITimeWatcher.this,0,intent,0);
            Notification notification = new NotificationCompat.Builder(this, UniToolKit.HABIT_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.habit_notification_title))
                    .setContentText(getResources().getString(R.string.habit_notification_content) + i.getName())
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .build();
            manager.notify((int)new Date().getTime() + HABIT_NOTIFICATION_SEED * i.getId(), notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeChangeReceiver);
    }
}
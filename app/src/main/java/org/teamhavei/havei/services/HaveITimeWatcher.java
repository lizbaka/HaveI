/*
    todo:尝试防杀后台？
    大量常量位于ActivityMain和此class中，需要降低耦合
 */
package org.teamhavei.havei.services;

import android.app.Notification;
import android.app.NotificationManager;
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
import org.teamhavei.havei.activities.ActivityMain;
import org.teamhavei.havei.databases.EventDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HaveITimeWatcher extends Service {

    private static final String TAG = "DEBUG";

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
        eventDBHelper = new EventDBHelper(this,EventDBHelper.DB_NAME,null,EventDBHelper.DB);
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
        Date dateTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 30);
        Date dateTimeAdvance = cal.getTime();
        List<Todo> todoList = eventDBHelper.findTodoByDatetime(new Date());
        List<Todo> todoListAdvance = eventDBHelper.findTodoByDatetime(dateTimeAdvance);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        for (Todo i : todoList) {
            Notification notification = new NotificationCompat.Builder(this, ActivityMain.TODO_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.todo_notification_title))
                    .setContentText(i.getName() + getResources().getString(R.string.todo_notification_content))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
            manager.notify(ActivityMain.NOTIFICATION_ID, notification);
        }
        for (Todo i : todoListAdvance) {
            Notification notification = new NotificationCompat.Builder(this, ActivityMain.TODO_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.todo_advance_notification_title))
                    .setContentText(i.getName() + getResources().getString(R.string.todo_advance_notification_content))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
            manager.notify(ActivityMain.NOTIFICATION_ID, notification);
        }
    }

    private void checkHabit(){
        SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm");
        List<Habit> habitList = eventDBHelper.findHabitByReminderTime(timeSDF.format(new Date()));
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        for (Habit i : habitList) {
            Notification notification = new NotificationCompat.Builder(this, ActivityMain.TODO_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getResources().getString(R.string.habit_notification_title))
                    .setContentText(getResources().getString(R.string.habit_notification_content) + i.getName())
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
            manager.notify(ActivityMain.NOTIFICATION_ID, notification);
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
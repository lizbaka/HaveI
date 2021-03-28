package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import org.teamhavei.havei.R;
import org.teamhavei.havei.habit.HabitDBHelper;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ActivityHabitDetail extends BaseActivity {

    private Toolbar mToolbar;
    private MaterialCalendarView mCalendar;

    private HabitDBHelper dbHelper;
    SQLiteDatabase db;

    private String habitName;
    private int habitID;

    public static void startAction(Context context, String habitName){
        Intent intent = new Intent(context, ActivityHabitDetail.class);
        intent.putExtra("habit_name",habitName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        mToolbar = findViewById(R.id.habit_detail_toolbar);
        mToolbar.inflateMenu(R.menu.habit_detail_toolbar);
        setSupportActionBar(mToolbar);
        mCalendar = findViewById(R.id.habit_detail_calendar);
        dbHelper = new HabitDBHelper(this,HabitDBHelper.DB_NAME,null,HabitDBHelper.DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        habitName = getIntent().getStringExtra("habit_name");
        habitID = dbHelper.getHabitID(habitName);

        getSupportActionBar().setTitle(habitName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCalendar.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                if(date.isAfter(CalendarDay.today())){
                    Toast.makeText(ActivityHabitDetail.this,"不能提前标记哦！",Toast.LENGTH_SHORT).show();
                    return;
                }

                LocalDate localDate = date.getDate();
                String dateString = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
                boolean done = dbHelper.switchHabitExec(habitID,dateString);
                widget.setDateSelected(date,done);
                String toastMessage;
                if(done){
                    toastMessage = "习惯执行记录成功！";
                }
                else{
                    toastMessage = "习惯执行记录已删除";
                }
                Toast.makeText(ActivityHabitDetail.this,toastMessage,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        habitName = dbHelper.getHabitName(habitID);
        showExecutionDate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.habit_detail_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.habit_detail_modify:
                ActivityModifyHabit.StartAction(this,ActivityModifyHabit.MODE_MODIFY,habitName);
                return true;
            case R.id.habit_detail_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("删除习惯");
                builder.setMessage("习惯将被永久删除，且对应执行记录也将被删除，您确定要删除" + habitName + "吗？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHabit();
                        finish();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setCancelable(true);
                builder.show();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return false;
    }

    private void showExecutionDate(){
        Cursor cursor = db.query("HabitExecs",new String[]{"date"},"habit_id = ?",new String[]{Integer.toString(habitID)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                String dateString = cursor.getString(cursor.getColumnIndex("date"));
                int year = Integer.parseInt(dateString.substring(0,4));
                int month = Integer.parseInt(dateString.substring(4,6));
                int day = Integer.parseInt(dateString.substring(6,8));
                mCalendar.setDateSelected(CalendarDay.from(year,month,day),true);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void deleteHabit(){
        db.delete("HabitExecs","habit_id = ?",new String[]{Integer.toString(habitID)});
        db.delete("Habit","id = ?",new String[]{Integer.toString(habitID)});
    }
}
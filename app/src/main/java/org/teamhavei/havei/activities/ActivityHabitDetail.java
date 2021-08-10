package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.HabitExec;
import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.EventDBHelper;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class ActivityHabitDetail extends BaseActivity {

    private static final String START_PARAM_HABIT_ID = "habit_id";
    private static final int NULL_HABIT_ID = -1;

    private Toolbar mToolbar;
    private MaterialCalendarView mCalendar;

    private EventDBHelper dbHelper;
    List<HabitExec> habitExecList;

    private Habit mHabit = null;

    public static void startAction(Context context, Habit habit) {
        Intent intent = new Intent(context, ActivityHabitDetail.class);
        intent.putExtra(START_PARAM_HABIT_ID, habit.getId());
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
        dbHelper = new EventDBHelper(this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        mHabit = dbHelper.findHabitById(getIntent().getIntExtra(START_PARAM_HABIT_ID, NULL_HABIT_ID));
        habitExecList = dbHelper.findHabitExecByHabitId(mHabit.getId());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCalendar.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                if (date.isAfter(CalendarDay.today())) {
                    Toast.makeText(ActivityHabitDetail.this, getString(R.string.habit_detail_advanced), Toast.LENGTH_SHORT).show();
                    return;
                }

                LocalDate localDate = date.getDate();
                /* BASIC_ISO_DATE == yyyyMMdd */
                String dateString = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
                boolean done = dbHelper.switchHabitExec(mHabit.getId(), dateString);
                widget.setDateSelected(date, done);
                String toastMessage;
                if (done) {
                    toastMessage = getString(R.string.habit_detail_marked);
                } else {
                    toastMessage = getString(R.string.habit_detail_unmarked);
                }
                Toast.makeText(ActivityHabitDetail.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHabit = dbHelper.findHabitById(getIntent().getIntExtra(START_PARAM_HABIT_ID, NULL_HABIT_ID));
        getSupportActionBar().setTitle(mHabit.getName());
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
            case R.id.habit_detail_toolbar_modify:
                // TODO: 2021.08.10 完成ModifyHabit后接入
//                ActivityModifyHabit.startAction(this, ActivityModifyHabit.MODE_MODIFY, mHabit.getName());
                return true;
            case R.id.habit_detail_toolbar_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.habit_detail_delete_dialog_title));
                builder.setMessage(getString(R.string.habit_detail_delete_dialog_msg1) + mHabit.getName() + getString(R.string.habit_detail_delete_dialog_msg2));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(HabitExec i:habitExecList){
                            dbHelper.deleteHabitExec(i);
                        }
                        dbHelper.deleteHabit(mHabit);
                        finish();
                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
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

    private void showExecutionDate() {
        for (HabitExec i:habitExecList) {
            String dateString = i.getDate();
            int year = Integer.parseInt(dateString.substring(0, 4));
            int month = Integer.parseInt(dateString.substring(4, 6));
            int day = Integer.parseInt(dateString.substring(6, 8));
            mCalendar.setDateSelected(CalendarDay.from(year, month, day), true);
        }
    }
}
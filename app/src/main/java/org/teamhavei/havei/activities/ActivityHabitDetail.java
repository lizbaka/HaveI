package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.teamhavei.havei.event.Habit;
import org.teamhavei.havei.event.HabitExec;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.YearCountPageAdapter;
import org.teamhavei.havei.databases.EventDBHelper;
import org.teamhavei.havei.fragments.FragmentYearCount;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityHabitDetail extends BaseActivity {

    public static final String START_PARAM_HABIT_ID = "habit_id";

    private static final int NULL_HABIT_ID = -1;

    private Toolbar mToolbar;
    private MaterialCalendarView mCalendar;
    private TextView actualTimesView;
    private TextView planTimesView;
    private TextView rankView;
    private TextView frequencyInfoView;
    private TextView reminderInfoView;
    private ViewPager yearCountVP;

    private EventDBHelper dbHelper;
    List<HabitExec> habitExecList;
    List<FragmentYearCount> yearCountFragmentList = new ArrayList<>();

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

        dbHelper = new EventDBHelper(this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);

        initView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mHabit = dbHelper.findHabitById(getIntent().getIntExtra(START_PARAM_HABIT_ID, NULL_HABIT_ID));
        getSupportActionBar().setTitle(mHabit.getName());
        habitExecList = dbHelper.findHabitExecByHabitId(mHabit.getId());

        mCalendar.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                if (date.isAfter(CalendarDay.today())) {
                    Toast.makeText(ActivityHabitDetail.this, getString(R.string.habit_detail_advanced), Toast.LENGTH_SHORT).show();
                    return;
                }

                LocalDate localDate = date.getDate();
                /* ISO_LOCAL_DATE == yyyy-MM-dd */
                String dateString = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
                boolean done = dbHelper.switchHabitExec(mHabit.getId(), dateString);
                widget.setDateSelected(date, done);

                String toastMessage;
                if (done) {
                    toastMessage = getString(R.string.habit_done);
                } else {
                    toastMessage = getString(R.string.habit_undone);
                }
                Toast.makeText(ActivityHabitDetail.this, toastMessage, Toast.LENGTH_SHORT).show();

                habitExecList = dbHelper.findHabitExecByHabitId(mHabit.getId());
                showAnalyze();
                rankView.setText(R.string.habit_detail_rank_changed);

                yearCountFragmentList.get(date.getYear() - 1970).update();
                yearCountVP.getAdapter().notifyDataSetChanged();
            }
        });
        mCalendar.state().edit()
                .setMinimumDate(CalendarDay.from(1970,1,1))
                .setMaximumDate(CalendarDay.today());
        mCalendar.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.isAfter(CalendarDay.today());
            }
            @Override
            public void decorate(DayViewFacade view) {
                view.setDaysDisabled(true);
            }
        });
        mCalendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                if (date.getMonth() == 1 || date.getMonth() == 12) {
                    yearCountVP.setCurrentItem(Math.max(date.getYear() - 1970, 0), true);
                }
            }
        });

        findViewById(R.id.habit_detail_ranking_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankView.setText("No." + calculateHabitRank());
            }
        });

        initYearCountFragments();
        yearCountVP.setAdapter(new YearCountPageAdapter(getSupportFragmentManager(), yearCountFragmentList, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        yearCountVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LocalDate localDate = mCalendar.getCurrentDate().getDate();
                mCalendar.setCurrentDate(CalendarDay.from(1970 + position, localDate.getMonthValue(), localDate.getDayOfMonth()), false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        yearCountVP.setCurrentItem(Calendar.getInstance().get(Calendar.YEAR) - 1970);
        rankView.setText("No." + calculateHabitRank());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHabit = dbHelper.findHabitById(mHabit.getId());
        Log.d(TAG, "onResume: " + mHabit.getId()+mHabit.getName());
        getSupportActionBar().setTitle(mHabit.getName());
        frequencyInfoView.setText(new StringBuilder()
                .append(mHabit.getRepeatTimes())
                .append(getString(R.string.times) + getString(R.string.each))
                .append(mHabit.getRepeatUnit())
                .append(getString(R.string.day))
                .toString());
        if (mHabit.getReminderTime() == null) {
            reminderInfoView.setText(getString(R.string.habit_detail_empty_reminder_hint));
        } else {
            reminderInfoView.setText(mHabit.getReminderTime());
        }
        showExecutionDate();
        showAnalyze();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_delete_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_detail_toolbar_modify:
                ActivityModifyHabit.startAction(this, mHabit.getId());
                return true;
            case R.id.event_detail_toolbar_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.habit_detail_delete_dialog_title));
                builder.setMessage(getString(R.string.habit_detail_delete_dialog_msg1) + mHabit.getName() + getString(R.string.habit_detail_delete_dialog_msg2));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (HabitExec i : habitExecList) {
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

    private void initView() {
        mToolbar = findViewById(R.id.habit_detail_toolbar);
        mToolbar.inflateMenu(R.menu.modify_delete_toolbar);
        actualTimesView = findViewById(R.id.actual_clock_in);
        planTimesView = findViewById(R.id.plan_clock_in);
        rankView = findViewById(R.id.habit_ranking);
        frequencyInfoView = findViewById(R.id.habit_detail_frequency_info);
        reminderInfoView = findViewById(R.id.habit_detail_reminder_info);
        mCalendar = findViewById(R.id.habit_detail_calendar);
        yearCountVP = findViewById(R.id.habit_detail_yearly_pager);
    }

    private void showExecutionDate() {
        mCalendar.clearSelection();
        for (HabitExec i : habitExecList) {
            String dateString = i.getDate();
            int year = Integer.parseInt(dateString.substring(0, 4));
            int month = Integer.parseInt(dateString.substring(5, 7));
            int day = Integer.parseInt(dateString.substring(8, 10));
            mCalendar.setDateSelected(CalendarDay.from(year, month, day), true);
        }
    }

    private void showAnalyze() {
        /* 无打卡记录的习惯 */
        if (habitExecList.size() == 0) {
            planTimesView.setText(R.string.habit_detail_empty_execution_hint);
            actualTimesView.setText(R.string.habit_detail_empty_execution_hint);
            return;
        }
        actualTimesView.setText(Integer.toString(habitExecList.size()));
        Calendar firstExecDate = Calendar.getInstance();
        firstExecDate.setTime(UniToolKit.eventDateParser(habitExecList.get(0).getDate()));
        Calendar todayDate = Calendar.getInstance();
        todayDate.set(Calendar.HOUR_OF_DAY, 0);
        todayDate.set(Calendar.MINUTE, 0);
        todayDate.set(Calendar.SECOND, 0);
        /* 24h/day, 60min/h, 60s/min, 1000ms/s */
        final int dayMilli = 24 * 60 * 60 * 1000;
        int planTimes = (int) ((todayDate.getTimeInMillis() - firstExecDate.getTimeInMillis()) / dayMilli + 1);
        planTimes = (int) Math.ceil(1.0 * planTimes / mHabit.getRepeatUnit()) * mHabit.getRepeatTimes();
        planTimesView.setText(Integer.toString(planTimes));
    }

    private int calculateHabitRank() {
        List<Habit> habitList = dbHelper.findAllHabitOrderByRank();
        for (int i = 0; i < habitList.size(); i++) {
            if (habitList.get(i).getId() == mHabit.getId()) {
                return i + 1;
            }
        }
        Log.d(TAG, "calculateHabitRank: failed!");
        return 0;
    }

    private void initYearCountFragments() {
        for (int curYear = 1970, todayYear = Calendar.getInstance().get(Calendar.YEAR); curYear <= todayYear; curYear++) {
            yearCountFragmentList.add(new FragmentYearCount(curYear, new FragmentYearCount.FragmentYearCountSocket() {
                @Override
                public void updateCountList(int year, List<Integer> count) {
                    Calendar curDate = Calendar.getInstance();
                    curDate.set(Calendar.YEAR, year);
                    count.clear();
                    for (int month = 0; month <= 11; month++) {
                        curDate.set(Calendar.MONTH, month);
                        count.add(month, dbHelper.findHabitExecByHabitIdWithYearMonth(mHabit.getId(), curDate.getTime()).size());
                    }
                }
            }));
        }
    }
}
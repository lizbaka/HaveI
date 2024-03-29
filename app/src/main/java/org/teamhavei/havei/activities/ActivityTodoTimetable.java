package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.teamhavei.havei.event.HaveIEvent;
import org.teamhavei.havei.event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.TimeTablePageAdapter;
import org.teamhavei.havei.databases.EventDBHelper;
import org.teamhavei.havei.fragments.FragmentTimeTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityTodoTimetable extends BaseActivity {

    ViewPager timetableVP;
    TabLayout tabLayout;
    FloatingActionButton fab;

    EventDBHelper dbHelper;
    List<FragmentTimeTable> fragmentList = new ArrayList<>();

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityTodoTimetable.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_timetable);

        dbHelper = new EventDBHelper(ActivityTodoTimetable.this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);

        setSupportActionBar(findViewById(R.id.event_timetable_toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        Calendar startOfYear = Calendar.getInstance();
        int curYear = startOfYear.get(Calendar.YEAR);
        startOfYear.set(Calendar.DAY_OF_YEAR, 1);
        /* 用于更新其他字段 */
        startOfYear.getTime();
        startOfYear.set(Calendar.DAY_OF_WEEK, 1);
        /* 同上 */
        startOfYear.getTime();

        fragmentList.clear();
        while (startOfYear.get(Calendar.YEAR) <= curYear) {

            fragmentList.add(new FragmentTimeTable(startOfYear, new FragmentTimeTable.TimetableSocket() {
                @Override
                public void updateEventList(Calendar startOfWeek, List<FragmentTimeTable.TimeTableEvent> eventList) {
                    eventList.clear();
                    //左闭右开
                    String sStartOfWeek = UniToolKit.eventDateFormatter(startOfWeek.getTime());
                    startOfWeek.add(Calendar.DAY_OF_YEAR, 7);
                    String sEndOfWeek = UniToolKit.eventDateFormatter(startOfWeek.getTime());
                    startOfWeek.add(Calendar.DAY_OF_YEAR, -7);
                    List<Todo> todoList = dbHelper.findTodoByDateRange(sStartOfWeek, sEndOfWeek);
                    int[] eventCount = new int[8];
                    for (int i = 1; i <= 7; i++) {
                        eventCount[i] = 0;
                    }
                    for (Todo todo : todoList) {
                        Calendar startTime = Calendar.getInstance();
                        startTime.setTime(UniToolKit.eventDatetimeParser(todo.getDateTime()));
                        eventList.add(new FragmentTimeTable.TimeTableEvent(startTime, todo.getName(), todo));
                    }
                }

                @Override
                public void onScrollBehavior(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        fab.hide();
                    } else {
                        fab.show();
                    }
                }

                @Override
                public void onCardClick(HaveIEvent event) {
                    ActivityTodoDetail.startAction(ActivityTodoTimetable.this, event.getId());
                }
            }, true));
            startOfYear.add(Calendar.DAY_OF_YEAR, 7);
        }

        timetableVP.setAdapter(new TimeTablePageAdapter(getSupportFragmentManager(), fragmentList, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        tabLayout.setupWithViewPager(timetableVP);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText("第" + (i + 1) + "周");
            if (fragmentList.get(i).getStartOfWeek().get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
                tabLayout.selectTab(tabLayout.getTabAt(i));
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityModifyTodo.startAction(ActivityTodoTimetable.this);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        timetableVP = findViewById(R.id.event_timetable_viewpager);
        tabLayout = findViewById(R.id.event_timetable_tabs);
        fab = findViewById(R.id.event_timetable_fab);
    }
}

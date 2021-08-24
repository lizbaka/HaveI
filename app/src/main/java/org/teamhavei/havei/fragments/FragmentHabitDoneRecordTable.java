package org.teamhavei.havei.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.HabitExec;
import org.teamhavei.havei.Event.HaveIEvent;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.activities.ActivityHabitDetail;
import org.teamhavei.havei.adapters.TimeTablePageAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FragmentHabitDoneRecordTable extends BaseFragment {

    EventDBHelper dbHelper;
    ViewPager doneRecordVP;
    ActionBar mActionBar;
    TabLayout mTabLayout;
    List<FragmentTimeTable> fragmentList = new ArrayList<>();

    public FragmentHabitDoneRecordTable(ActionBar actionBar, TabLayout tabLayout) {
        this.mActionBar = actionBar;
        this.mTabLayout = tabLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: main");
        View view = inflater.inflate(R.layout.fragment_habit_done_record_table, container, false);

        dbHelper = new EventDBHelper(getContext(), EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        initView(view);

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
                    String sStartOfWeek = UniToolKit.eventDateFormatter(startOfWeek.getTime());
                    startOfWeek.add(Calendar.DAY_OF_YEAR, 6);
                    String sEndOfWeek = UniToolKit.eventDateFormatter(startOfWeek.getTime());
                    startOfWeek.add(Calendar.DAY_OF_YEAR, -6);
                    List<HabitExec> execList = dbHelper.findHabitExecByDateRange(sStartOfWeek, sEndOfWeek);
                    HashMap<Integer,Integer> HP = new HashMap<>();
                    int used = -1;
                    for (HabitExec exec : execList) {
                        Habit habit = dbHelper.findHabitById(exec.getHabitId());
                        Integer cur = HP.get(habit.getId());
                        if(cur == null){
                            cur = ++used;
                            HP.put(habit.getId(),cur);
                        }
                        Calendar startTime = Calendar.getInstance();
                        startTime.setTime(UniToolKit.eventDateParser(exec.getDate()));
                        startTime.set(Calendar.HOUR_OF_DAY, cur);
                        eventList.add(new FragmentTimeTable.TimeTableEvent(startTime, habit.getName(), habit));
                    }
                }

                @Override
                public void onCardClick(HaveIEvent event) {
                    ActivityHabitDetail.startAction(getActivity(), (Habit) event);
                }
            }, false));
            startOfYear.add(Calendar.DAY_OF_YEAR, 7);
        }

        doneRecordVP.setAdapter(new TimeTablePageAdapter(getChildFragmentManager(), fragmentList, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mTabLayout.setupWithViewPager(doneRecordVP);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setText("第" + Integer.toString(i + 1) + "周");
            if(fragmentList.get(i).getStartOfWeek().get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)){
                mTabLayout.selectTab(mTabLayout.getTabAt(i));
            }
        }

        return view;
    }


    private void initView(View view) {
        doneRecordVP = view.findViewById(R.id.habit_done_record_viewpager);
    }
}

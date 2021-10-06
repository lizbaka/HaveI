package org.teamhavei.havei.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.activities.ActivityHabitDetail;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.Calendar;
import java.util.List;

public class FragmentTodayHabit extends BaseFragment {

    List<Habit> todayHabitList;
    EventDBHelper dbHelper;

    GridLayout todayHabitContainerGL;
    TextView noUnfinishedTV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new EventDBHelper(getContext(), EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        todayHabitList = dbHelper.findUnfinishedHabit(Calendar.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_habit, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTodayHabitContainer();
    }

    private void initView(View view) {
        todayHabitContainerGL = view.findViewById(R.id.today_habit_container);
        noUnfinishedTV = view.findViewById(R.id.today_habit_no_unfinished);
    }

    private void updateTodayHabitContainer() {
        View child;
        IconAdapter iconAdapter = new IconAdapter(getContext());


        if(todayHabitList.isEmpty()){
            noUnfinishedTV.setVisibility(View.VISIBLE);
        }else{
            noUnfinishedTV.setVisibility(View.GONE);
        }
        Log.d(TAG, "updateTodayHabitContainer: " + todayHabitList.size());

        for (int i = 0; i < todayHabitList.size(); i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            child = LayoutInflater.from(getContext()).inflate(R.layout.dynamic_icon_title_secondary_card, null);
            ImageView iconIV = child.findViewById(R.id.icon_title_icon);
            View iconContainerV = child.findViewById(R.id.icon_title_icon_container);
            TextView titleTV = child.findViewById(R.id.icon_title_title);

            Habit mHabit = todayHabitList.get(i);
            params.rowSpec = GridLayout.spec(i / 4);
            params.columnSpec = GridLayout.spec(i % 4, 1f);
            iconIV.setImageDrawable(iconAdapter.getIcon(dbHelper.findEventTagById(mHabit.getTagId()).getIconId()));
            iconContainerV.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.habit_icon_state_list));
            iconContainerV.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            if (dbHelper.isHabitDone(mHabit.getId(), Calendar.getInstance().getTime())) {
                iconContainerV.setSelected(true);
            }
            titleTV.setText(mHabit.getName());

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityHabitDetail.startAction(getContext(), mHabit);
                }
            });
            child.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    iconContainerV.setSelected(!iconContainerV.isSelected());
                    if (dbHelper.switchHabitExec(mHabit.getId(), Calendar.getInstance().getTime())) {
                        Toast.makeText(getContext(), R.string.habit_done, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.habit_undone, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            todayHabitContainerGL.addView(child, params);
        }
    }
}

package org.teamhavei.havei.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.activities.ActivityModifyHabit;
import org.teamhavei.havei.adapters.HabitCardAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.List;

public class FragmentHabitList extends BaseFragment {

    EventDBHelper dbHelper;
    List<Habit> habitList;

    ExtendedFloatingActionButton fab;
    RecyclerView habitCardListRV;
    HabitCardAdapter habitCardAdapter;
    NestedScrollView containerNSV;
    TextView emptyTV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new EventDBHelper(getContext(), EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        habitList = dbHelper.findAllHabit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit_list, container, false);
        initView(view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityModifyHabit.startAction(getContext());
            }
        });
        containerNSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        habitList = dbHelper.findAllHabit();
        if (habitList.isEmpty()) {
            containerNSV.setVisibility(View.INVISIBLE);
            emptyTV.setVisibility(View.VISIBLE);
        } else {
            containerNSV.setVisibility(View.VISIBLE);
            emptyTV.setVisibility(View.GONE);
        }
        habitCardAdapter.setHabitList(dbHelper.findAllHabit());
        habitCardAdapter.notifyDataSetChanged();
    }

    void initView(View view) {
        habitCardListRV = view.findViewById(R.id.habit_list_card_list);
        fab = view.findViewById(R.id.habit_list_add);
        containerNSV = view.findViewById(R.id.habit_list_container);
        emptyTV = view.findViewById(R.id.habit_list_empty);

        habitCardListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        habitCardAdapter = new HabitCardAdapter(dbHelper.findAllHabit(), getContext());
        habitCardListRV.setAdapter(habitCardAdapter);
    }
}

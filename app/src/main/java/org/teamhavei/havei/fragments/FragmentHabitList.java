package org.teamhavei.havei.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.activities.ActivityModifyHabit;
import org.teamhavei.havei.adapters.HabitCardAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.List;

public class FragmentHabitList extends BaseFragment{

    EventDBHelper dbHelper;
    List<Habit> habitList;

    ExtendedFloatingActionButton fab;
    RecyclerView habitCardListRV;
    HabitCardAdapter habitCardAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new EventDBHelper(getContext(),EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
        habitList = dbHelper.findAllHabit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit_list,container,false);
        initView(view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityModifyHabit.startAction(getContext());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        habitList= dbHelper.findAllHabit();
        habitCardAdapter.setHabitList(dbHelper.findAllHabit());
        habitCardAdapter.notifyDataSetChanged();
    }

    void initView(View view){
        habitCardListRV = view.findViewById(R.id.habit_list_card_list);
        fab = view.findViewById(R.id.habit_list_add);
        habitCardListRV.setLayoutManager(new LinearLayoutManager(getContext()));
        habitCardAdapter = new HabitCardAdapter(dbHelper.findAllHabit(), getContext());
        habitCardListRV.setAdapter(habitCardAdapter);
    }
}

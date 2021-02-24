package org.teamhavei.havei.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.DashboardCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentDashBoard extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        RecyclerView dashboardCardList = (RecyclerView) view.findViewById(R.id.dashboard_card_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        dashboardCardList.setLayoutManager(layoutManager);
        DashboardCardAdapter adapter = new DashboardCardAdapter(getCards());
        dashboardCardList.setAdapter(adapter);
        return view;
    }

    private List<Habit> getCards(){
        List<Habit> habitList = new ArrayList<>();
        for(int i = 0; i<3; i++){
            Habit habit = new Habit();
            habit.setHabitName("习惯" + i);
            habit.setHabitTag("标签1");
            habit.setHabitFrequency(7-i);
            habit.setHabitFrequencyPer(Habit.FREQUENCY_PER_MONTH);
            habit.setHabitExecTimes(20-i);
            habitList.add(habit);
        }
        return habitList;
    }
}

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
import org.teamhavei.havei.adapters.HabitCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentHabit extends Fragment {

    private List<Habit> habitList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        habitList = getHabitList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        RecyclerView habitCardList = (RecyclerView) view.findViewById(R.id.habit_card_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        habitCardList.setLayoutManager(layoutManager);
        HabitCardAdapter adapter = new HabitCardAdapter(habitList);
        habitCardList.setAdapter(adapter);
        return view;
    }

    private List<Habit> getHabitList(){
        List<Habit> habitList = new ArrayList<>();
        for(int i = 0; i<10; i++){
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
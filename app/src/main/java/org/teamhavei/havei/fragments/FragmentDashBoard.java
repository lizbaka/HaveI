package org.teamhavei.havei.fragments;

import androidx.fragment.app.Fragment;

public class FragmentDashBoard extends Fragment {

//    private List<Habit> habitList;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        habitList = getHabits();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//
//        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        RecyclerView dashboardCardList = (RecyclerView) view.findViewById(R.id.dashboard_card_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        dashboardCardList.setLayoutManager(layoutManager);
//        HabitCardAdapter adapter = new HabitCardAdapter(habitList);
//        dashboardCardList.setAdapter(adapter);
//        Log.d("TAG", "onCreateView: onCreateView executed");
//        return view;
//    }
//
//    private List<Habit> getHabits(){
//        List<Habit> habitList = new ArrayList<>();
//        for(int i = 0; i<10; i++){
//            Habit habit = new Habit();
//            habit.setHabitName("习惯" + i);
//            habit.setHabitTag("标签1");
//            habit.setHabitFrequency(7-i);
//            habit.setHabitFrequencyPer(Habit.FREQUENCY_PER_MONTH);
//            habit.setHabitExecTimes(20-i);
//            habitList.add(habit);
//        }
//        return habitList;
//    }
}

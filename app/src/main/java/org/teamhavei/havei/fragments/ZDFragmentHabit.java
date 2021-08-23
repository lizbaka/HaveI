//package org.teamhavei.havei.fragments;
//
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.teamhavei.havei.activities.ActivityModifyHabit;
//import org.teamhavei.havei.Event.Habit;
//import org.teamhavei.havei.R;
//import org.teamhavei.havei.adapters.HabitCardAdapter;
//import org.teamhavei.havei.databases.EventDBHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FragmentHabit extends BaseFragment {
//
//    private List<Habit> mHabitList;
//    private EventDBHelper mHabitDBHelper;
//    HabitCardAdapter adapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mHabitDBHelper = new EventDBHelper(getActivity(), EventDBHelper.DB_NAME,null, EventDBHelper.DATABASE_VERSION);
//        mHabitList = new ArrayList<>();
//        updateMHabitList();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        View view = inflater.inflate(R.layout.fragment_habit, container, false);
//        RecyclerView habitCardList = view.findViewById(R.id.habit_card_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        habitCardList.setLayoutManager(layoutManager);
//        adapter = new HabitCardAdapter(mHabitList,getContext());
//        habitCardList.setAdapter(adapter);
//        if(mHabitList.size()>0){
//            view.findViewById(R.id.habit_empty_hint).setVisibility(View.INVISIBLE);
//        }
//        else{
//            view.findViewById(R.id.habit_empty_hint).setVisibility(View.VISIBLE);
//        }
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        updateMHabitList();
//        adapter.notifyDataSetChanged();
//        if(mHabitList.size()>0){
//            getView().findViewById(R.id.habit_empty_hint).setVisibility(View.INVISIBLE);
//        }
//        else{
//            getView().findViewById(R.id.habit_empty_hint).setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void updateMHabitList(){
//        mHabitList.clear();
//        SQLiteDatabase habitDB = mHabitDBHelper.getReadableDatabase();
//        Cursor cursor = habitDB.query("Habit",null,null,null,null,null,null);
//        if(cursor.moveToFirst()){
//            do{
//                Habit insertHabit = new Habit();
//                insertHabit.setHabitID(cursor.getInt(cursor.getColumnIndex("id")));
//                insertHabit.setHabitName(cursor.getString(cursor.getColumnIndex("name")));
//                insertHabit.setHabitTag(cursor.getString(cursor.getColumnIndex("tag")));
//                mHabitList.add(insertHabit);
//            }while(cursor.moveToNext());
//        }
//        cursor.close();
//    }
//
//    public void addHabit(){
//        ActivityModifyHabit.startAction(getActivity(),ActivityModifyHabit.MODE_ADD);
//    }
//
//}
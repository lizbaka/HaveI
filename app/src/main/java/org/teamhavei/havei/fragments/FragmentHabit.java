package org.teamhavei.havei.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.habit.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.HabitCardAdapter;
import org.teamhavei.havei.habit.HabitDBHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentHabit extends Fragment {

    private List<Habit> habitList;

    private HabitDBHelper mHabitDBHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHabitDBHelper = new HabitDBHelper(getActivity(),"Habit.db",null,HabitDBHelper.DATABASE_VERSION);
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
        if(habitList.size()>0){
            view.findViewById(R.id.habit_empty_hint).setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private List<Habit> getHabitList(){
        List<Habit> habitList = new ArrayList<>();
        SQLiteDatabase habitDB = mHabitDBHelper.getWritableDatabase();
        Cursor cursor = habitDB.query("Habit",null,null,null,null,null,null);
        Habit insertHabit = new Habit();
        if(cursor.moveToFirst()){
            do{
                insertHabit.setHabitName(cursor.getString(cursor.getColumnIndex("name")));
                insertHabit.setHabitTag(cursor.getString(cursor.getColumnIndex("tag")));
                insertHabit.setHabitFrequency(cursor.getInt(cursor.getColumnIndex("frequency")));
                insertHabit.setHabitFrequencyType(cursor.getInt(cursor.getColumnIndex("frequency_type")));
                insertHabit.setHabitExecTimes(cursor.getInt(cursor.getColumnIndex("exec_times")));
                habitList.add(insertHabit);
            }while(cursor.moveToNext());
        }
        return habitList;
    }
}
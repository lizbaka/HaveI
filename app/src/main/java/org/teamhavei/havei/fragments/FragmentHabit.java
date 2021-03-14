package org.teamhavei.havei.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.activities.ActivityModifyHabit;
import org.teamhavei.havei.habit.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.HabitCardAdapter;
import org.teamhavei.havei.habit.HabitDBHelper;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FragmentHabit extends BaseFragment {

    static final int REQUEST_CODE_ADD = 1001;

    private List<Habit> mHabitList;
    private HabitDBHelper mHabitDBHelper;
    HabitCardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHabitDBHelper = new HabitDBHelper(getActivity(),"Habit.db",null,HabitDBHelper.DATABASE_VERSION);
        mHabitList = getmHabitList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        RecyclerView habitCardList = (RecyclerView) view.findViewById(R.id.habit_card_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        habitCardList.setLayoutManager(layoutManager);
        adapter = new HabitCardAdapter(mHabitList);
        habitCardList.setAdapter(adapter);
        if(mHabitList.size()>0){
            view.findViewById(R.id.habit_empty_hint).setVisibility(View.INVISIBLE);
        }
        else{
            view.findViewById(R.id.habit_empty_hint).setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mHabitList.size()>0){
            getView().findViewById(R.id.habit_empty_hint).setVisibility(View.INVISIBLE);
        }
        else{
            getView().findViewById(R.id.habit_empty_hint).setVisibility(View.VISIBLE);
        }
    }

    private List<Habit> getmHabitList(){
        List<Habit> habitList = new ArrayList<>();
        SQLiteDatabase habitDB = mHabitDBHelper.getReadableDatabase();
        Cursor cursor = habitDB.query("Habit",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Habit insertHabit = new Habit();
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

    public void addHabit(){
        Intent intent = new Intent(getActivity(), ActivityModifyHabit.class);
        startActivityForResult(intent,REQUEST_CODE_ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case REQUEST_CODE_ADD:
                if(resultCode == RESULT_OK){
                    ContentValues values = (ContentValues)data.getParcelableExtra("new_habit");
                    Habit newHabit = new Habit();
                    newHabit.setHabitName(values.getAsString("name"));
                    newHabit.setHabitTag(values.getAsString("tag"));
                    newHabit.setHabitFrequency(values.getAsInteger("frequency"));
                    newHabit.setHabitFrequencyType(values.getAsInteger("frequency_type"));
                    mHabitList.add(newHabit);
                    adapter.notifyItemInserted(mHabitList.size()-1);
                }
        }
    }
}
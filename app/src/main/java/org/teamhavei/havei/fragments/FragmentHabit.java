package org.teamhavei.havei.fragments;

import android.app.Activity;
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

    private List<Habit> mHabitList;
    private HabitDBHelper mHabitDBHelper;
    HabitCardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHabitDBHelper = new HabitDBHelper(getActivity(),HabitDBHelper.DB_NAME,null,HabitDBHelper.DATABASE_VERSION);
        mHabitList = new ArrayList<>();
        updatemHabitList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        RecyclerView habitCardList = view.findViewById(R.id.habit_card_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        habitCardList.setLayoutManager(layoutManager);
        adapter = new HabitCardAdapter(mHabitList,getContext());
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

        updatemHabitList();
        adapter.notifyDataSetChanged();
        if(mHabitList.size()>0){
            getView().findViewById(R.id.habit_empty_hint).setVisibility(View.INVISIBLE);
        }
        else{
            getView().findViewById(R.id.habit_empty_hint).setVisibility(View.VISIBLE);
        }
    }

    private void updatemHabitList(){
        mHabitList.clear();
        SQLiteDatabase habitDB = mHabitDBHelper.getReadableDatabase();
        Cursor cursor = habitDB.query("Habit",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Habit insertHabit = new Habit();
                insertHabit.setHabitID(cursor.getInt(cursor.getColumnIndex("id")));
                insertHabit.setHabitName(cursor.getString(cursor.getColumnIndex("name")));
                insertHabit.setHabitTag(cursor.getString(cursor.getColumnIndex("tag")));
                mHabitList.add(insertHabit);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    public void addHabit(){
        ActivityModifyHabit.StartAction(getActivity(),ActivityModifyHabit.MODE_ADD);
    }

}
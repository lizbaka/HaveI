package org.teamhavei.havei.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.teamhavei.havei.R;
import org.teamhavei.havei.habit.*;

public class ActivityModifyHabit extends BaseActivity {

    EditText habitNameLayout;
    EditText habitTagLayout;
    EditText habitFrequencyView;
    Spinner habitFrequencyTypeSelector;
    Button modifyHabitDone;
    HabitDBHelper mHabitDBHelper;
    int habitFrequencyType;

    int mode = 0;//0:add 1:modify
    //modify 待实现

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_habit);
        habitNameLayout = ((TextInputLayout)findViewById(R.id.modify_habit_name)).getEditText();
        habitTagLayout = ((TextInputLayout) findViewById(R.id.modify_habit_tag)).getEditText();
        habitFrequencyView = (EditText) findViewById(R.id.modify_habit_frequency);
        habitFrequencyTypeSelector = (Spinner) findViewById(R.id.modify_habit_frequency_type);
        modifyHabitDone = (Button) findViewById(R.id.modify_habit_done_button);
        mHabitDBHelper = new HabitDBHelper(this,"Habit.db",null,HabitDBHelper.DATABASE_VERSION);

        habitFrequencyTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        habitFrequencyType = Habit.FREQUENCY_TYPE_DAY;
                        break;
                    case 1:
                        habitFrequencyType = Habit.FREQUENCY_TYPE_MONTH;
                        break;
                    case 2:
                        habitFrequencyType = Habit.FREQUENCY_TYPE_WEEK;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        modifyHabitDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mHabitDBHelper.getWritableDatabase();
                int frequency = Integer.parseInt(habitFrequencyView.getText().toString());
                if(frequency <= 0){
                    Toast.makeText(ActivityModifyHabit.this,"频率应大于0！",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                values.put("name",habitNameLayout.getText().toString());
                values.put("tag", habitTagLayout.getText().toString());
                values.put("frequency",frequency);
                values.put("frequency_type",habitFrequencyType);
                if(mode == 0) {
                    db.insert("Habit", null, values);
                    Intent intent = new Intent();
                    intent.putExtra("new_habit",values);
                    setResult(RESULT_OK, intent);
                }
                if(mode==1){

                }
                finish();
            }
        });
    }
}
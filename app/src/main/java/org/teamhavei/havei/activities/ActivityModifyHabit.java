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

    static final int HABIT_FREQUENCY_OK = 0;
    static final int HABIT_FREQUENCY_EMPTY = 1;
    static final int HABIT_FREQUENCY_ZERO = 2;

    static final int MODE_ADD = 0;
    static final int MODE_MODIFY = 1;

    EditText habitNameLayout;
    EditText habitTagLayout;
    EditText habitFrequencyView;
    Spinner habitFrequencyTypeSelector;
    Button modifyHabitDone;
    HabitDBHelper mHabitDBHelper;
    int habitFrequencyType;

    int mode = MODE_ADD;//0:add 1:modify
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
                switch(checkHabitFrequencyValidate()){
                    case HABIT_FREQUENCY_EMPTY:
                        habitFrequencyView.setError("请输入次数！");
                        habitFrequencyView.requestFocus();
                        return;
                    case HABIT_FREQUENCY_ZERO:
                        habitFrequencyView.setError("次数应大于0！");
                        habitFrequencyView.requestFocus();
                        return;
                    case HABIT_FREQUENCY_OK:
                        habitFrequencyView.setError(null);
                        break;
                }
                int frequency = Integer.parseInt(habitFrequencyView.getText().toString());
                SQLiteDatabase db = mHabitDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name",habitNameLayout.getText().toString());
                values.put("tag", habitTagLayout.getText().toString());
                values.put("frequency",frequency);
                values.put("frequency_type",habitFrequencyType);
                if(mode == MODE_ADD) {
                    db.insert("Habit", null, values);
                    Intent intent = new Intent();
                    intent.putExtra("new_habit",values);
                    setResult(RESULT_OK, intent);
                }
                else if(mode == MODE_MODIFY){

                }
                finish();
            }
        });
    }

    private int checkHabitFrequencyValidate(){
        int result = HABIT_FREQUENCY_OK;
        String originHabitFrequency = habitFrequencyView.getText().toString();
        if(originHabitFrequency.length()==0)
            result = HABIT_FREQUENCY_EMPTY;
        else if(Integer.parseInt(originHabitFrequency)<=0)
            result = HABIT_FREQUENCY_ZERO;
        return result;
    }
}
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

    static final int MODE_ADD = 0;
    static final int MODE_MODIFY = 1;

    EditText habitNameLayout;
    EditText habitTagLayout;
    Button modifyHabitDone;
    HabitDBHelper mHabitDBHelper;

    int mode = MODE_ADD;//0:add 1:modify
    //modify 待实现

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_habit);
        habitNameLayout = ((TextInputLayout) findViewById(R.id.modify_habit_name)).getEditText();
        habitTagLayout = ((TextInputLayout) findViewById(R.id.modify_habit_tag)).getEditText();
        modifyHabitDone = (Button) findViewById(R.id.modify_habit_done_button);
        mHabitDBHelper = new HabitDBHelper(this, "Habit.db", null, HabitDBHelper.DATABASE_VERSION);


        modifyHabitDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mHabitDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", habitNameLayout.getText().toString());
                values.put("tag", habitTagLayout.getText().toString());
                if (mode == MODE_ADD) {
                    db.insert("Habit", null, values);
                    Intent intent = new Intent();
                    intent.putExtra("new_habit", values);
                    setResult(RESULT_OK, intent);
                } else if (mode == MODE_MODIFY) {

                }
                finish();
            }
        });
    }
}
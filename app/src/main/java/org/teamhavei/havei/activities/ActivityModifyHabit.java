package org.teamhavei.havei.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import org.teamhavei.havei.R;
import org.teamhavei.havei.habit.*;

public class ActivityModifyHabit extends BaseActivity {

    public static final int MODE_ADD = 0;
    public static final int MODE_MODIFY = 1;

    EditText habitNameLayout;
    EditText habitTagLayout;
    Button modifyHabitDone;
    HabitDBHelper mHabitDBHelper;

    int mMode = MODE_ADD;//0:add 1:modify
    //modify 待实现

    static public void StartAction(Context context, int mode){
        Intent intent = new Intent(context, ActivityModifyHabit.class);
        intent.putExtra("mode",mode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_habit);

        mMode = getIntent().getIntExtra("mode",MODE_ADD);
        habitNameLayout = ((TextInputLayout) findViewById(R.id.modify_habit_name)).getEditText();
        habitTagLayout = ((TextInputLayout) findViewById(R.id.modify_habit_tag)).getEditText();
        modifyHabitDone = findViewById(R.id.modify_habit_done_button);
        mHabitDBHelper = new HabitDBHelper(this, "Habit.db", null, HabitDBHelper.DATABASE_VERSION);


        modifyHabitDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mHabitDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", habitNameLayout.getText().toString());
                values.put("tag", habitTagLayout.getText().toString());
                if (mMode == MODE_ADD) {
                    db.insert("Habit", null, values);
                } else if (mMode == MODE_MODIFY) {

                }
                finish();
            }
        });
    }
}
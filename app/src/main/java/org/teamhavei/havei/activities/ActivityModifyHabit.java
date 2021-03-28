package org.teamhavei.havei.activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import org.teamhavei.havei.R;
import org.teamhavei.havei.habit.*;

public class ActivityModifyHabit extends BaseActivity {

    public static final int MODE_ADD = 0;
    public static final int MODE_MODIFY = 1;

    EditText habitNameET;
    EditText habitTagET;
    Button modifyHabitDone;

    HabitDBHelper dbHelper;
    SQLiteDatabase db;

    String habitName;
    String habitTag;

    int mode = MODE_ADD;//0:add 1:modify
    //modify 待实现

    static public void StartAction(Context context, int mode){
        Intent intent = new Intent(context, ActivityModifyHabit.class);
        intent.putExtra("mode",mode);
        context.startActivity(intent);
    }

    static public void StartAction(Context context, int mode, String habitName){
        Intent intent = new Intent(context, ActivityModifyHabit.class);
        intent.putExtra("mode", mode);
        intent.putExtra("habit_name",habitName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_habit);

        setSupportActionBar((Toolbar)findViewById(R.id.modify_habit_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mode = getIntent().getIntExtra("mode",MODE_ADD);

        habitNameET = ((TextInputLayout) findViewById(R.id.modify_habit_name)).getEditText();
        habitTagET = ((TextInputLayout) findViewById(R.id.modify_habit_tag)).getEditText();
        modifyHabitDone = findViewById(R.id.modify_habit_done_button);

        dbHelper = new HabitDBHelper(this, HabitDBHelper.DB_NAME, null, HabitDBHelper.DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();

        if(mode == MODE_MODIFY) {
            habitName = getIntent().getStringExtra("habit_name");
            habitTag = dbHelper.getHabitTag(habitName);
            setOriginalText();
        }

        modifyHabitDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkHabitValidate()) {
                    ContentValues values = new ContentValues();
                    values.put("name", habitNameET.getText().toString());
                    values.put("tag", habitTagET.getText().toString());
                    if (mode == MODE_ADD) {
                        if(!isHabitDuplicated()) {
                            db.insert("Habit", null, values);
                        }
                        else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityModifyHabit.this);
                            dialog.setTitle("该习惯已存在");
                            dialog.setMessage("是否更新已有习惯？");
                            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    habitName = habitNameET.getText().toString();
                                    db.update("Habit", values, "name = ?", new String[]{habitName});
                                    finish();
                                }
                            });
                            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    habitNameET.setError("该习惯已存在");
                                    habitNameET.requestFocus();
                                }
                            });
                            dialog.show();
                            return;
                        }
                    }
                    else if (mode == MODE_MODIFY) {
                        if(!isHabitDuplicated() || habitNameET.getText().toString().equals(habitName)) {
                            db.update("Habit", values, "name = ?", new String[]{habitName});
                        }
                        else{
                            habitNameET.setError("该习惯已存在");
                            habitNameET.requestFocus();
                            return;
                        }
                    }
                    finish();
                }
            }
        });
    }

    private void setOriginalText(){
        habitNameET.setText(habitName);
        habitTagET.setText(habitTag);
    }

    private boolean checkHabitValidate(){
        String newHabitName = habitNameET.getText().toString();
        if(newHabitName.equals("")){
            habitNameET.setError("习惯名不能为空");
            habitNameET.requestFocus();
            return false;
        }
        else
            return true;
    }

    private boolean isHabitDuplicated(){
        String newHabitName = habitNameET.getText().toString();
        Cursor cursor = db.query("Habit",new String[]{"name"},"name = ?",new String[]{newHabitName},null,null,null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
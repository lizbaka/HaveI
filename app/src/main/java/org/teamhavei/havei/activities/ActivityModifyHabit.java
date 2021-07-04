//package org.teamhavei.havei.activities;
//
//import android.app.AlertDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.material.textfield.TextInputLayout;
//
//import org.teamhavei.havei.R;
//import org.teamhavei.havei.databases.EventDBHelper;
//
//public class ActivityModifyHabit extends BaseActivity {
//
//    public static final int MODE_ADD = 0;
//    public static final int MODE_MODIFY = 1;
//
//    public static final String START_PARAM_MODE = "mode";
//    public static final String START_PARAM_HABIT_NAME = "habit_name";
//
//    EditText habitNameET;
//    EditText habitTagET;
//    Button modifyHabitDone;
//
//    EventDBHelper dbHelper;
//    SQLiteDatabase db;
//
//    String habitName;
//    String habitTag;
//
//    int mode = MODE_ADD;//0:add 1:modify
//
//    static public void startAction(Context context, int mode){
//        Intent intent = new Intent(context, ActivityModifyHabit.class);
//        intent.putExtra(START_PARAM_MODE,mode);
//        context.startActivity(intent);
//    }
//
//    static public void startAction(Context context, int mode, String habitName){
//        Intent intent = new Intent(context, ActivityModifyHabit.class);
//        intent.putExtra(START_PARAM_MODE, mode);
//        intent.putExtra(START_PARAM_HABIT_NAME,habitName);
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_modify_habit);
//
//        setSupportActionBar(findViewById(R.id.modify_habit_toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        mode = getIntent().getIntExtra(START_PARAM_MODE,MODE_ADD);
//
//        habitNameET = ((TextInputLayout) findViewById(R.id.modify_habit_name)).getEditText();
//        habitTagET = ((TextInputLayout) findViewById(R.id.modify_habit_tag)).getEditText();
//        modifyHabitDone = findViewById(R.id.modify_habit_done_button);
//
//        dbHelper = new EventDBHelper(this, EventDBHelper.DB_NAME, null, EventDBHelper.DATABASE_VERSION);
//        db = dbHelper.getWritableDatabase();
//
//        if(mode == MODE_MODIFY) {
//            habitName = getIntent().getStringExtra(START_PARAM_HABIT_NAME);
//            habitTag = dbHelper.getHabitTag(habitName);
//            getSupportActionBar().setTitle(R.string.modify_habit_title_modify);
//            setOriginalText();
//        }
//
//        modifyHabitDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkHabitValidate()) {
//                    ContentValues values = new ContentValues();
//                    values.put("name", habitNameET.getText().toString());
//                    values.put("tag", habitTagET.getText().toString());
//                    if (mode == MODE_ADD) {
//                        if(!isHabitDuplicated()) {
//                            db.insert("Habit", null, values);
//                        }
//                        else {
//                            AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityModifyHabit.this);
//                            dialog.setTitle(getString(R.string.modify_habit_exist_dialog_title));
//                            dialog.setMessage(getString(R.string.modify_habit_exist_dialog_msg));
//                            dialog.setPositiveButton(getString(R.string.dialog_positive), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    habitName = habitNameET.getText().toString();
//                                    db.update("Habit", values, "name = ?", new String[]{habitName});
//                                    finish();
//                                }
//                            });
//                            dialog.setNegativeButton(getString(R.string.dialog_negative), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    habitNameET.setError(getString(R.string.modify_habit_exist_error));
//                                    habitNameET.requestFocus();
//                                }
//                            });
//                            dialog.show();
//                            return;
//                        }
//                    }
//                    else if (mode == MODE_MODIFY) {
//                        if(!isHabitDuplicated() || habitNameET.getText().toString().equals(habitName)) {
//                            db.update("Habit", values, "name = ?", new String[]{habitName});
//                        }
//                        else{
//                            habitNameET.setError(getString(R.string.modify_habit_exist_error));
//                            habitNameET.requestFocus();
//                            return;
//                        }
//                    }
//                    finish();
//                }
//            }
//        });
//    }
//
//    private void setOriginalText(){
//        habitNameET.setText(habitName);
//        habitTagET.setText(habitTag);
//    }
//
//    private boolean checkHabitValidate(){
//        String newHabitName = habitNameET.getText().toString();
//        if(newHabitName.equals("")){
//            habitNameET.setError(getString(R.string.modify_habit_empty_name));
//            habitNameET.requestFocus();
//            return false;
//        }
//        else
//            return true;
//    }
//
//    private boolean isHabitDuplicated(){
//        String newHabitName = habitNameET.getText().toString();
//        Cursor cursor = db.query("Habit",new String[]{"name"},"name = ?",new String[]{newHabitName},null,null,null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count > 0;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return false;
//    }
//}
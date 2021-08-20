package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.teamhavei.havei.Event.EventTag;
import org.teamhavei.havei.Event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

public class ActivityTodoDetail extends AppCompatActivity {

    public static final String START_PARAM_TODO_ID = "todo_id";

    private static final int NULL_TODO_ID = -1;

    EventDBHelper dbHelper;

    ImageView iconIV;
    TextView todoNameTV;
    TextView todoTagTV;
    TextView statusTV;
    TextView todoDatetimeTV;
    TextView remindDatetimeTV;
    TextView remarkTV;
    FloatingActionButton switchFab;

    Todo mTodo;

    public static void startAction(Context context, int todoId) {
        Intent intent = new Intent(context, ActivityTodoDetail.class);
        intent.putExtra(START_PARAM_TODO_ID, todoId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        setSupportActionBar(findViewById(R.id.todo_detail_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new EventDBHelper(ActivityTodoDetail.this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);

        initView();
        updateInfo();

        switchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTodo.isDone()) {
                    statusTV.setText(R.string.unfinished);
                    statusTV.setTextColor(getResources().getColor(R.color.red_500));
                    switchFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_24_white));
                } else {
                    statusTV.setText(R.string.finished);
                    statusTV.setTextColor(getResources().getColor(R.color.green_500));
                    switchFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_close_24_white));
                }
                mTodo.setDone(!mTodo.isDone());
                dbHelper.updateTodo(mTodo.getId(),mTodo);
            }
        });
    }

    @Override
    protected void onResume() {
        updateInfo();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_detail_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_detail_toolbar_modify:
                ActivityModifyTodo.startAction(this, mTodo.getId());
                return true;
            case R.id.event_detail_toolbar_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTodoDetail.this);
                builder.setTitle(R.string.todo_detail_delete_dialog_title);
                builder.setMessage(R.string.todo_detail_delete_dialog_msg1 + mTodo.getName() + R.string.todo_detail_delete_dialog_msg2);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteTodo(mTodo);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setCancelable(true);
                builder.show();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    private void initView() {
        iconIV = findViewById(R.id.todo_detail_icon);
        todoNameTV = findViewById(R.id.todo_detail_name);
        todoTagTV = findViewById(R.id.todo_detail_tag);
        statusTV = findViewById(R.id.todo_detail_status);
        todoDatetimeTV = findViewById(R.id.todo_detail_datetime);
        remindDatetimeTV = findViewById(R.id.todo_detail_remind_datetime);
        remarkTV = findViewById(R.id.todo_detail_remark);
        switchFab = findViewById(R.id.todo_detail_switch_fab);
    }

    private void updateInfo() {
        mTodo = dbHelper.findTodoById(getIntent().getIntExtra(START_PARAM_TODO_ID, NULL_TODO_ID));
        todoNameTV.setText(mTodo.getName());
        EventTag tag = dbHelper.findEventTagById(mTodo.getTagId());
        todoTagTV.setText(tag.getName());
        iconIV.setImageDrawable(new IconAdapter(ActivityTodoDetail.this).getIcon(tag.getIconId()));
        if (mTodo.isDone()) {
            statusTV.setText(R.string.finished);
            statusTV.setTextColor(getResources().getColor(R.color.green_500));
            switchFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_close_24_white));
        } else {
            statusTV.setText(R.string.unfinished);
            statusTV.setTextColor(getResources().getColor(R.color.red_500));
            switchFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_24_white));
        }
        todoDatetimeTV.setText(mTodo.getDateTime());
        if (mTodo.getReminderDateTime() != null) {
            remindDatetimeTV.setText(mTodo.getReminderDateTime());
        } else {
            remindDatetimeTV.setText(R.string.modify_todo_reminder_time_null);
        }
        remarkTV.setText(mTodo.getRemark());
    }
}
package org.teamhavei.havei.activities;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.Event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityModifyTodo extends BaseActivity {

    public static final String Start_PARAM_TODO_ID = "todo_id";
    public static final int MODE_ADD = 0;
    public static final int MODE_MODIFY = 1;

    private static final int NULL_TODO_ID = -1;
    private static final int DEFAULT_EVENT_TAG_ID = 1;
    private static final boolean DEFAULT_REMIND_TIME_NULL = true;

    TextInputEditText todoNameET;
    TextView todoDateTV;
    TextView todoTimeTV;
    TextInputEditText remarkET;
    LinearLayout reminderDatetimeContainer;
    TextView reminderDateTV;
    TextView reminderTimeTV;
    TextView reminderEmptyHintTV;
    RecyclerView tagListRV;
    TagListAdapter tagListAdapter;
    ImageView tagMng;

    EventDBHelper dbHelper;
    IconAdapter iconAdapter = new IconAdapter(ActivityModifyTodo.this);
    Todo mTodo;
    List<HaveITag> tagList;
    int mode = MODE_ADD;
    int selectedEventTagID = DEFAULT_EVENT_TAG_ID;
    boolean isRemindTimeNull = DEFAULT_REMIND_TIME_NULL;
    Calendar todoDatetime = Calendar.getInstance();
    Calendar remindDateTime = Calendar.getInstance();

    static public void startAction(Context context) {
        Intent intent = new Intent(context, ActivityModifyTodo.class);
        context.startActivity(intent);
    }

    static public void startAction(Context context, int todoId) {
        Intent intent = new Intent(context, ActivityModifyTodo.class);
        intent.putExtra(Start_PARAM_TODO_ID, todoId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_todo);

        setSupportActionBar(findViewById(R.id.modify_todo_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new EventDBHelper(this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);

        initView();
        if (getIntent().getIntExtra(Start_PARAM_TODO_ID, NULL_TODO_ID) != NULL_TODO_ID) {
            mTodo = dbHelper.findTodoById(getIntent().getIntExtra(Start_PARAM_TODO_ID, NULL_TODO_ID));
            mode = MODE_MODIFY;
            getSupportActionBar().setTitle(R.string.modify_todo_title_modify);
        } else {
            mode = MODE_ADD;
            getSupportActionBar().setTitle(R.string.modify_todo_title_add);
            mTodo = new Todo();
            mTodo.setTagId(dbHelper.findAllEventTag(true).get(0).getId());
            mTodo.setDateTime(UniToolKit.eventDatetimeFormatter(Calendar.getInstance().getTime()));
        }
        setOriginalInfo();

        tagList = new ArrayList<>();
        tagList.addAll(dbHelper.findAllEventTag(true));
        tagListAdapter = new TagListAdapter(tagList, ActivityModifyTodo.this, selectedEventTagID, TagListAdapter.ORIENTATION_HORIZONTAL,new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                selectedEventTagID = tag.getId();
            }
        });
        tagListRV.setLayoutManager(new GridLayoutManager(ActivityModifyTodo.this, 3, GridLayoutManager.HORIZONTAL, false));
        tagListRV.setAdapter(tagListAdapter);

        todoDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivityModifyTodo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        todoDatetime.set(Calendar.YEAR, year);
                        todoDatetime.set(Calendar.MONTH, month);
                        todoDatetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        todoDateTV.setText(UniToolKit.eventDateFormatter(todoDatetime.getTime()).substring(5, 10));
                    }

                }, todoDatetime.get(Calendar.YEAR), todoDatetime.get(Calendar.MONTH), todoDatetime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        todoTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ActivityModifyTodo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        todoDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        todoDatetime.set(Calendar.MINUTE, minute);

                        todoTimeTV.setText(UniToolKit.eventTimeFormatter(todoDatetime.getTime()));
                    }
                }, todoDatetime.get(Calendar.HOUR_OF_DAY), todoDatetime.get(Calendar.MINUTE), true).show();
            }
        });

        reminderDatetimeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRemindTimeNull) {
                    new DatePickerDialog(ActivityModifyTodo.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            remindDateTime.set(Calendar.YEAR, year);
                            remindDateTime.set(Calendar.MONTH, month);
                            remindDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            new TimePickerDialog(ActivityModifyTodo.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    remindDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    remindDateTime.set(Calendar.MINUTE, minute);

                                    isRemindTimeNull = false;
                                    reminderDateTV.setVisibility(View.VISIBLE);
                                    reminderDateTV.setText(UniToolKit.eventDatetimeFormatter(remindDateTime.getTime()).substring(5, 10));
                                    reminderTimeTV.setVisibility(View.VISIBLE);
                                    reminderTimeTV.setText(UniToolKit.eventDatetimeFormatter(remindDateTime.getTime()).substring(11, 16));
                                    reminderEmptyHintTV.setVisibility(View.GONE);

                                    Toast.makeText(ActivityModifyTodo.this,getString(R.string.modify_todo_clear_reminder_hint),Toast.LENGTH_SHORT).show();
                                }
                            }, remindDateTime.get(Calendar.HOUR_OF_DAY), remindDateTime.get(Calendar.MINUTE), true).show();
                        }
                    }, remindDateTime.get(Calendar.YEAR), remindDateTime.get(Calendar.MONTH), remindDateTime.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        reminderDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivityModifyTodo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        remindDateTime.set(Calendar.YEAR, year);
                        remindDateTime.set(Calendar.MONTH, month);
                        remindDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        reminderDateTV.setText(UniToolKit.eventDateFormatter(remindDateTime.getTime()).substring(5, 10));
                    }
                }, remindDateTime.get(Calendar.YEAR), remindDateTime.get(Calendar.MONTH), remindDateTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        reminderTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ActivityModifyTodo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        remindDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        remindDateTime.set(Calendar.MINUTE, minute);

                        reminderTimeTV.setText(UniToolKit.eventTimeFormatter(remindDateTime.getTime()));
                    }
                }, remindDateTime.get(Calendar.HOUR_OF_DAY), remindDateTime.get(Calendar.MINUTE), true).show();
            }
        });

        reminderDateTV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isRemindTimeNull = true;
                reminderDateTV.setVisibility(View.GONE);
                reminderTimeTV.setVisibility(View.GONE);
                reminderEmptyHintTV.setVisibility(View.VISIBLE);
                Toast.makeText(ActivityModifyTodo.this,getString(R.string.modify_todo_reminder_cleared),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        reminderTimeTV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isRemindTimeNull = true;
                reminderDateTV.setVisibility(View.GONE);
                reminderTimeTV.setVisibility(View.GONE);
                reminderEmptyHintTV.setVisibility(View.VISIBLE);
                Toast.makeText(ActivityModifyTodo.this,getString(R.string.modify_todo_reminder_cleared),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        reminderDatetimeContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isRemindTimeNull = true;
                reminderDateTV.setVisibility(View.GONE);
                reminderTimeTV.setVisibility(View.GONE);
                reminderEmptyHintTV.setVisibility(View.VISIBLE);
                Toast.makeText(ActivityModifyTodo.this,getString(R.string.modify_todo_reminder_cleared),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        tagMng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySettingsTagMng.startAction(ActivityModifyTodo.this,UniToolKit.TAG_TYPE_EVENT);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tagList.clear();
        tagList.addAll(dbHelper.findAllEventTag(true));
        tagListRV.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                if (checkTodoValidate()) {
                    mTodo.setName(todoNameET.getText().toString());
                    mTodo.setTagId(selectedEventTagID);
                    mTodo.setDateTime(UniToolKit.eventDatetimeFormatter(todoDatetime.getTime()));
                    mTodo.setRemark(remarkET.getText().toString());
                    if (!isRemindTimeNull) {
                        mTodo.setReminderDateTime(UniToolKit.eventDatetimeFormatter(remindDateTime.getTime()));
                    } else {
                        mTodo.setReminderDateTime(null);
                    }
                    if (mode == MODE_MODIFY) {
                        dbHelper.updateTodo(mTodo.getId(), mTodo);
                    } else if (mode == MODE_ADD) {
                        dbHelper.insertTodo(mTodo);
                    }
                    finish();
                }
                return true;
        }
        return false;
    }

    private void initView() {
        todoNameET = findViewById(R.id.modify_todo_name);
        todoDateTV = findViewById(R.id.modify_todo_date);
        todoTimeTV = findViewById(R.id.modify_todo_time);
        remarkET = findViewById(R.id.modify_todo_remark);
        reminderDatetimeContainer = findViewById(R.id.modify_todo_reminder_datetime_container);
        reminderDateTV = findViewById(R.id.modify_todo_reminder_date);
        reminderTimeTV = findViewById(R.id.modify_todo_reminder_time);
        reminderEmptyHintTV = findViewById(R.id.modify_todo_reminder_empty_hint);
        tagListRV = findViewById(R.id.modify_todo_tag_list);
        tagMng = findViewById(R.id.modify_todo_tag_mng);
    }

    private void setOriginalInfo() {
        todoNameET.setText(mTodo.getName());
        todoDateTV.setText(mTodo.getDateTime().substring(5, 10));
        todoTimeTV.setText(mTodo.getDateTime().substring(11, 16));
        todoDatetime.setTime(UniToolKit.eventDatetimeParser(mTodo.getDateTime()));
        remarkET.setText(mTodo.getRemark());
        if (mTodo.getReminderDateTime() != null) {
            isRemindTimeNull = false;
            reminderDateTV.setVisibility(View.VISIBLE);
            reminderDateTV.setText(mTodo.getReminderDateTime().substring(5, 10));
            reminderTimeTV.setVisibility(View.VISIBLE);
            reminderTimeTV.setText(mTodo.getReminderDateTime().substring(11, 16));
            reminderEmptyHintTV.setVisibility(View.GONE);
            remindDateTime.setTime(UniToolKit.eventDatetimeParser(mTodo.getReminderDateTime()));
        } else {
            isRemindTimeNull = true;
            reminderDateTV.setVisibility(View.GONE);
            reminderTimeTV.setVisibility(View.GONE);
            reminderEmptyHintTV.setVisibility(View.VISIBLE);
        }
        selectedEventTagID = mTodo.getTagId();
    }

    private boolean checkTodoValidate() {
        boolean status = true;
        if (todoNameET.getText().toString().equals("")) {
            todoNameET.setError(getString(R.string.modify_todo_empty_name));
            todoNameET.requestFocus();
            status = false;
        }
        if (!isRemindTimeNull) {
            if (remindDateTime.after(todoDatetime)) {
                Toast.makeText(ActivityModifyTodo.this, R.string.modify_todo_remind_after_todo, Toast.LENGTH_SHORT).show();
                remindDateTime.setTimeInMillis(todoDatetime.getTimeInMillis());
                reminderDateTV.setText(UniToolKit.eventDatetimeFormatter(remindDateTime.getTime()).substring(5, 10));
                reminderTimeTV.setText(UniToolKit.eventTimeFormatter(remindDateTime.getTime()));
                status = false;
            }
        }
        return status;
    }
}
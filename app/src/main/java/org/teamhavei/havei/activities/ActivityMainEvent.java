package org.teamhavei.havei.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.adapters.TodoCardAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ActivityMainEvent extends BaseActivity {

    EventDBHelper dbHelper;
    Calendar calendar;
    List<Todo> todayTodoList;
    List<Habit> todayHabitList;

    MaterialCardView dateSelectorCV;
    TextView dateSelectorDateTV;
    MaterialCardView todayHabitCV;
    GridLayout habitContainerGL;
    TextView habitEmptyHintTV;
    TextView todoDateTV;
    TextView todoWeekdayTV;
    RecyclerView todayTodoRV;
    TextView todoEmptyHintTV;
    TodoCardAdapter todayTodoRVAdapter;
    Button todoLibBtn;
    Button habitLibBtn;
    ExtendedFloatingActionButton addTodoFab;


    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityMainEvent.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);
        dbHelper = new EventDBHelper(ActivityMainEvent.this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        setSupportActionBar(findViewById(R.id.main_event_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        initView();
        todayTodoRVAdapter = new TodoCardAdapter(ActivityMainEvent.this, todayTodoList, TodoCardAdapter.TODO_CARD_TYPE_SIMPLE, new TodoCardAdapter.OnCardClickListener() {
            @Override
            public void onClick(Todo todo) {

            }
        });
        todayTodoRV.setAdapter(todayTodoRVAdapter);
        todayTodoRV.setLayoutManager(new LinearLayoutManager(ActivityMainEvent.this));
        todayTodoRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        calendar = Calendar.getInstance();
        updateSelectedDate();

        bindOnclickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSelectedDate();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    private void initView() {
        dateSelectorCV = findViewById(R.id.main_event_date_selector);
        dateSelectorDateTV = findViewById(R.id.main_event_date_selector_date);
        todayHabitCV = findViewById(R.id.main_event_habit_card);
        habitContainerGL = findViewById(R.id.main_event_habit_container);
        habitEmptyHintTV = findViewById(R.id.main_event_habit_empty_hint);
        todoDateTV = findViewById(R.id.main_event_todo_date);
        todoWeekdayTV = findViewById(R.id.main_event_todo_weekday);
        todayTodoRV = findViewById(R.id.main_event_todo_list);
        todoEmptyHintTV = findViewById(R.id.main_event_todo_empty_hint);
        todoLibBtn = findViewById(R.id.main_event_todo_library);
        habitLibBtn = findViewById(R.id.main_event_habit_library);
        addTodoFab = findViewById(R.id.main_event_todo_add);
    }

    private void updateSelectedDate() {
        dateSelectorDateTV.setText(UniToolKit.eventDateFormatter(calendar.getTime()));
        todoDateTV.setText(UniToolKit.eventDateFormatter(calendar.getTime()).substring(5));
        todoWeekdayTV.setText(new SimpleDateFormat("EEEE").format(calendar.getTime()));
        todayTodoList = dbHelper.findTodoByDate(calendar.getTime());
        if(todayTodoList.size()<=0){
            todayTodoRV.setVisibility(View.GONE);
            todoEmptyHintTV.setVisibility(View.VISIBLE);
        }else{
            todoEmptyHintTV.setVisibility(View.GONE);
            todayTodoRV.setVisibility(View.VISIBLE);
        }
        todayTodoRVAdapter.setTodoList(todayTodoList);
        todayTodoRVAdapter.notifyDataSetChanged();
        todayHabitList = dbHelper.findUnfinishedHabit(calendar);
        habitContainerGL.removeAllViews();
        if(todayHabitList.size()<=0){
            habitEmptyHintTV.setVisibility(View.VISIBLE);
        }else{
            habitEmptyHintTV.setVisibility(View.GONE);
            configHabitContainerGL();
        }
    }

    private void bindOnclickListener() {
        todoLibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2021.08.16 等待待办库后端完成后接入（界面9）
            }
        });
        addTodoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityModifyTodo.startAction(ActivityMainEvent.this);
            }
        });
        habitLibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2021.08.16 等待习惯库后端完成后接入 （界面10）
            }
        });
        todayHabitCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2021.08.16 等待习惯库后端完成后接入 （界面10）
            }
        });
        dateSelectorCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ActivityMainEvent.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateSelectedDate();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void configHabitContainerGL() {
        if (todayHabitList.size() <= 4) {
            habitContainerGL.setColumnCount(todayHabitList.size());
            habitContainerGL.setRowCount(1);
        } else {
            habitContainerGL.setColumnCount(4);
            habitContainerGL.setRowCount(2);
        }
        View child;
        IconAdapter iconAdapter = new IconAdapter(ActivityMainEvent.this);

        Habit mHabit;
        for (int i = 0; i < (todayHabitList.size() < 4 ? todayHabitList.size() : 4); i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            child = LayoutInflater.from(ActivityMainEvent.this).inflate(R.layout.dynamic_icon_title_secondary, null);
            ImageView iconIV = child.findViewById(R.id.icon_title_icon);
            View iconContainerV = child.findViewById(R.id.icon_title_icon_container);
            TextView titleTV = child.findViewById(R.id.icon_title_title);

            mHabit = todayHabitList.get(i);
            params.rowSpec = GridLayout.spec(0);
            params.columnSpec = GridLayout.spec(i, 1f);
            iconIV.setImageDrawable(iconAdapter.getIcon(dbHelper.findEventTagById(mHabit.getTagId()).getIconId()));
            titleTV.setText(mHabit.getName());
            habitContainerGL.addView(child, params);
        }
        int secondRowColumnSize = 1;
        switch (todayHabitList.size()) {
            case 5:
                secondRowColumnSize = 4;
                break;
            case 6:
                secondRowColumnSize = 3;
                break;
            default:
                secondRowColumnSize = 1;
        }
        for (int i = 4; i < (todayHabitList.size() < 8 ? todayHabitList.size() : 8); i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            child = LayoutInflater.from(ActivityMainEvent.this).inflate(R.layout.dynamic_icon_title_secondary, habitContainerGL,false);
            ImageView iconIV = child.findViewById(R.id.icon_title_icon);
            View iconContainerV = child.findViewById(R.id.icon_title_icon_container);
            TextView titleTV = child.findViewById(R.id.icon_title_title);

            mHabit = todayHabitList.get(i);
            params.rowSpec = GridLayout.spec(1);
            params.columnSpec = GridLayout.spec(i % 4, secondRowColumnSize, 1f);
            iconIV.setImageDrawable(iconAdapter.getIcon(dbHelper.findEventTagById(mHabit.getTagId()).getIconId()));
            titleTV.setText(mHabit.getName());
            habitContainerGL.addView(child, params);
        }
    }
}

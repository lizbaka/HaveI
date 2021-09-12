package org.teamhavei.havei.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.teamhavei.havei.Event.EventTag;
import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityModifyHabit extends BaseActivity {

    public static final String START_PARAM_HABIT_ID = "habit_id";
    public static final int MODE_ADD = 0;
    public static final int MODE_MODIFY = 1;

    private static final int NULL_HABIT_ID = -1;
    private static final int DEFAULT_EVENT_TAG_ID = 1;
    private static final int NULL_REMIND_TIME = -1;

    TextInputEditText habitNameView;
    TextInputEditText habitTimesView;
    TextInputEditText habitUnitView;
    TextView habitTagView;
    ImageView iconView;
    MaterialButton remindTimeBtn;
    RecyclerView tagListRV;
    TagListAdapter tagListAdapter;
    ImageView tagMng;

    EventDBHelper dbHelper;
    IconAdapter iconAdapter = new IconAdapter(ActivityModifyHabit.this);
    Habit mHabit;
    List<HaveITag> tagList;
    int mode = MODE_ADD;
    int selectedEventTagID = DEFAULT_EVENT_TAG_ID;
    int remindHour = NULL_REMIND_TIME;
    int remindMin = NULL_REMIND_TIME;

    static public void startAction(Context context) {
        Intent intent = new Intent(context, ActivityModifyHabit.class);
        context.startActivity(intent);
    }

    static public void startAction(Context context, int habitId) {
        Intent intent = new Intent(context, ActivityModifyHabit.class);
        intent.putExtra(START_PARAM_HABIT_ID, habitId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_habit);

        setSupportActionBar(findViewById(R.id.modify_habit_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new EventDBHelper(this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);

        initView();

        if (getIntent().getIntExtra(START_PARAM_HABIT_ID, NULL_HABIT_ID) != NULL_HABIT_ID) {
            mHabit = dbHelper.findHabitById(getIntent().getIntExtra(START_PARAM_HABIT_ID, NULL_HABIT_ID));
            mode = MODE_MODIFY;
            getSupportActionBar().setTitle(R.string.modify_habit_title_modify);
        } else {
            mHabit = new Habit();
            mHabit.setTagId(dbHelper.findAllEventTag(true).get(0).getId());
            mode = MODE_ADD;
            getSupportActionBar().setTitle(R.string.modify_habit_title_add);
        }
        setOriginalInfo();

        tagList = new ArrayList<>();
        tagList.addAll(dbHelper.findAllEventTag(true));
        tagListAdapter = new TagListAdapter(tagList, ActivityModifyHabit.this, selectedEventTagID, TagListAdapter.ORIENTATION_HORIZONTAL,new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                selectedEventTagID = tag.getId();
                iconView.setImageDrawable(iconAdapter.getIcon(tag.getIconId()));
                habitTagView.setText(tag.getName());
            }
        });
        tagListRV.setLayoutManager(new GridLayoutManager(ActivityModifyHabit.this, 3, GridLayoutManager.HORIZONTAL, false));
        tagListRV.setAdapter(tagListAdapter);

        remindTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ActivityModifyHabit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        remindHour = hourOfDay;
                        remindMin = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, remindHour);
                        calendar.set(Calendar.MINUTE, remindMin);
                        remindTimeBtn.setText(UniToolKit.eventTimeFormatter(calendar.getTime()));
                        Toast.makeText(ActivityModifyHabit.this, R.string.modify_habit_clear_remind_hint, Toast.LENGTH_SHORT).show();
                    }
                }, remindHour == NULL_REMIND_TIME ? 0 : remindHour, remindMin == NULL_REMIND_TIME ? 0 : remindMin, true).show();
            }
        });
        remindTimeBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remindHour = NULL_REMIND_TIME;
                remindMin = NULL_REMIND_TIME;
                remindTimeBtn.setText(getString(R.string.modify_habit_reminder_time_null));
                Toast.makeText(ActivityModifyHabit.this, R.string.modify_habit_remind_cleared, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        tagMng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySettingsTagMng.startAction(ActivityModifyHabit.this, UniToolKit.TAG_TYPE_EVENT);
                tagList.clear();
                tagList.addAll(dbHelper.findAllEventTag(true));
                tagListAdapter.notifyDataSetChanged();
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
                if (checkHabitValidate()) {
                    Habit newHabit = new Habit();
                    newHabit.setName(habitNameView.getText().toString());
                    newHabit.setTagId(selectedEventTagID);
                    newHabit.setRepeatTimes(Integer.parseInt(habitTimesView.getText().toString()));
                    newHabit.setRepeatUnit(Integer.parseInt(habitUnitView.getText().toString()));
                    if (remindHour != NULL_REMIND_TIME && remindMin != NULL_REMIND_TIME) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, remindHour);
                        calendar.set(Calendar.MINUTE, remindMin);
                        newHabit.setReminderTime(UniToolKit.eventTimeFormatter(calendar.getTime()));
                    }
                    if (mode == MODE_ADD) {
                        dbHelper.insertHabit(newHabit);
                    } else {
                        dbHelper.updateHabit(mHabit, newHabit);
                    }
                    finish();
                }
                return true;
        }
        return false;
    }

    private void setOriginalInfo() {
        habitNameView.setText(mHabit.getName());
        habitTimesView.setText(Integer.toString(mHabit.getRepeatTimes()));
        habitUnitView.setText(Integer.toString(mHabit.getRepeatUnit()));
        IconAdapter iconAdapter = new IconAdapter(this);
        EventTag tag = dbHelper.findEventTagById(mHabit.getTagId());
        selectedEventTagID = tag.getId();
        iconView.setImageDrawable(iconAdapter.getIcon(tag.getIconId()));
        habitTagView.setText(tag.getName());
        if (mHabit.getReminderTime() != null) {
            remindTimeBtn.setText(mHabit.getReminderTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(UniToolKit.eventTimeParser(mHabit.getReminderTime()));
            remindHour = calendar.get(Calendar.HOUR_OF_DAY);
            remindMin = calendar.get(Calendar.MINUTE);
        }
    }

    /**
     * 检查输入的数据是否为空或有效
     *
     * @return true：输入正确； false：输入无效
     */
    private boolean checkHabitValidate() {
        boolean status = true;
        /* 空习惯名 */
        if (habitNameView.getText().toString().equals("")) {
            habitNameView.setError(getString(R.string.modify_habit_empty_name));
            habitNameView.requestFocus();
            status = false;
        }

        /* 空次数 */
        if (habitTimesView.getText().toString().equals("")) {
            habitTimesView.setError(getString(R.string.modify_habit_empty_frequency));
            habitTimesView.requestFocus();
            status = false;
        }
        /* 空单位 */
        else if (habitUnitView.getText().toString().equals("")) {
            habitUnitView.setError(getString(R.string.modify_habit_empty_frequency));
            habitUnitView.requestFocus();
            status = false;
        } else {
            int times = Integer.parseInt(habitTimesView.getText().toString());
            int unit = Integer.parseInt(habitUnitView.getText().toString());
            /* 计划打卡次数大于天数 */
            if (times > unit) {
                habitTimesView.setError(getString(R.string.modify_habit_too_many_times));
                habitTimesView.requestFocus();
                status = false;
            }
            /* 计划打卡次数大于天数 */
            if (times < 0 || unit < 0) {
                habitTimesView.setError(getString(R.string.modify_habit_illegal_frequency));
                habitTimesView.requestFocus();
                status = false;
            }
        }
        return status;
    }

    void initView() {
        habitTimesView = findViewById(R.id.modify_habit_times);
        habitUnitView = findViewById(R.id.modify_habit_unit);
        habitNameView = findViewById(R.id.modify_habit_name);
        habitTagView = findViewById(R.id.modify_habit_tag);
        remindTimeBtn = findViewById(R.id.modify_habit_remind_time_button);
        iconView = findViewById(R.id.modify_habit_icon);
        tagListRV = findViewById(R.id.modify_habit_tag_list);
        tagMng = findViewById(R.id.modify_habit_tag_mng);
    }
}
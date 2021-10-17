package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.databases.EventDBHelper;
import org.teamhavei.havei.databases.UtilDBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ActivityReport extends AppCompatActivity {

    SharedPreferences pref;
    EventDBHelper eventDBHelper;
    BookkeepDBHelper bookkeepDBHelper;
    UtilDBHelper utilDBHelper;
    IconAdapter iconAdapter;


    TextView userNameTV;
    TextView habitTimesTV;
    TextView habitNumberTV;
    TextView habitFinishTimeTV;
    TextView habitDayTV;
    TextView bookkeepSumTV;
    TextView bookkeepDayTV;
    TextView bookkeepSumSmallTV;
    TextView bookkeepMostTV;
    TextView IdTV;
    TextView dateTV;
    TextView habitOneTV;
    TextView habitTwoTV;
    TextView habitThreeTV;
    TextView sentenceTV;
    ImageView habitOneIV;
    ImageView habitTwoIV;
    ImageView habitThreeIV;
    ImageView avatarIV;
    ImageView QRCodeIV;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityReport.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        setSupportActionBar(findViewById(R.id.monthly_report_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.monthly_report_title);

        eventDBHelper = new EventDBHelper(ActivityReport.this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        bookkeepDBHelper = new BookkeepDBHelper(ActivityReport.this, BookkeepDBHelper.DB_NAME,null,BookkeepDBHelper.DATABASE_VERSION);
        utilDBHelper = new UtilDBHelper(ActivityReport.this, UtilDBHelper.DB_NAME, null, UtilDBHelper.DB_VERSION);
        pref = getSharedPreferences(UniToolKit.PREF_SETTINGS, MODE_PRIVATE);
        iconAdapter = new IconAdapter(ActivityReport.this);

        initView();
        showBasicPart();
        showEventPart();
        showBookkeepPart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Bitmap bitmap = createBitmap(findViewById(R.id.monthly_report_container));
                File imagesFolder = new File(getCacheDir(),"images");
                Uri uri = null;
                try{
                    imagesFolder.mkdirs();
                    File file = new File(imagesFolder, "shared_image.jpg");

                    FileOutputStream stream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    stream.flush();
                    stream.close();
                    uri = FileProvider.getUriForFile(this, "org.teamhavei.havei.FileProvider",file);
                    grantUriPermission(getPackageName(),uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        userNameTV = findViewById(R.id.monthly_report_user_name);//finish
        habitTimesTV = findViewById(R.id.monthly_report_habit_times);//finish
        habitNumberTV = findViewById(R.id.monthly_report_habit_number);//finish
        habitFinishTimeTV = findViewById(R.id.monthly_report_habit_finish_times);//finish
        habitDayTV = findViewById(R.id.monthly_report_habit_day);//
        bookkeepSumTV = findViewById(R.id.monthly_report_bookkeep_sum);
        bookkeepDayTV = findViewById(R.id.monthly_report_bookkeep_day);
        bookkeepSumSmallTV = findViewById(R.id.monthly_report_bookkeep_sum_small);
        bookkeepMostTV = findViewById(R.id.monthly_report_bookkeep_most);
        IdTV = findViewById(R.id.monthly_report_id);//finish
        dateTV = findViewById(R.id.monthly_report_date);//finish
        habitOneTV = findViewById(R.id.monthly_report_habit_one_name);
        habitTwoTV = findViewById(R.id.monthly_report_habit_two_name);
        habitThreeTV = findViewById(R.id.monthly_report_habit_three_name);
        sentenceTV = findViewById(R.id.monthly_report_sentence);//finish
        habitOneIV = findViewById(R.id.monthly_report_habit_one);
        habitTwoIV = findViewById(R.id.monthly_report_habit_two);
        habitThreeIV = findViewById(R.id.monthly_report_habit_three);
        avatarIV = findViewById(R.id.monthly_report_avatar);
        QRCodeIV = findViewById(R.id.monthly_report_QR_code);
    }

    private void showBasicPart() {
        userNameTV.setText(pref.getString(UniToolKit.PREF_SETTINGS_USER_NAME, getString(R.string.default_user_name)));
        IdTV.setText(pref.getString(UniToolKit.PREF_SETTINGS_USER_NAME, getString(R.string.default_user_name)));
        dateTV.setText(UniToolKit.eventDateFormatter(Calendar.getInstance().getTime()));
        // TODO: 2021.08.27 接入头像功能后在此处设定头像; 加入二维码下载支持
        sentenceTV.setText(utilDBHelper.pickOneProverb());
    }

    private void showEventPart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String sMonthBegin = UniToolKit.eventDateFormatter(calendar.getTime());
        calendar.add(Calendar.MONTH, 1);
        String sMonthEnd = UniToolKit.eventDateFormatter(calendar.getTime());
        habitTimesTV.setText(Integer.toString(eventDBHelper.findHabitExecByDateRange(sMonthBegin, sMonthEnd).size()));
        habitNumberTV.setText(Integer.toString(eventDBHelper.findAllHabit().size()));
        habitFinishTimeTV.setText(Integer.toString(eventDBHelper.getHabitExecCount()));
        habitDayTV.setText(Integer.toString(eventDBHelper.getHabitExecDayCount()));
        List<Habit> habitList = eventDBHelper.findAllHabitOrderByRank();
        if (habitList.size() >= 1) {
            habitOneIV.setImageDrawable(iconAdapter.getIcon(eventDBHelper.findEventTagById(habitList.get(0).getTagId()).getIconId()));
            habitOneTV.setText(habitList.get(0).getName());
        } else {
            habitOneIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_help_24));
            habitOneTV.setText(getString(R.string.monthly_report_no_habit));
        }
        if (habitList.size() >= 2) {
            habitTwoIV.setImageDrawable(iconAdapter.getIcon(eventDBHelper.findEventTagById(habitList.get(1).getTagId()).getIconId()));
            habitTwoTV.setText(habitList.get(1).getName());
        } else {
            habitTwoIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_help_24));
            habitTwoTV.setText(getString(R.string.monthly_report_no_habit));
        }
        if (habitList.size() >= 3) {
            habitThreeIV.setImageDrawable(iconAdapter.getIcon(eventDBHelper.findEventTagById(habitList.get(2).getTagId()).getIconId()));
            habitThreeTV.setText(habitList.get(2).getName());
        } else {
            habitThreeIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_help_24));
            habitThreeTV.setText(getString(R.string.monthly_report_no_habit));
        }
    }

    private void showBookkeepPart() {
        bookkeepSumTV.setText(Integer.toString(bookkeepDBHelper.findBookkeepByMonth(Calendar.getInstance().getTime()).size()));
        bookkeepSumSmallTV.setText(Integer.toString(bookkeepDBHelper.findAllBookkeep().size()));
        List<BookTag> bookTagList = bookkeepDBHelper.findAllBookTagSortByPM(UniToolKit.BOOKKEEP_TAG_EXPENDITURE);
        if(bookTagList.isEmpty()){
            bookkeepMostTV.setText(R.string.no_data);
        }else{
            bookkeepMostTV.setText(bookTagList.get(0).getName());
        }
        bookkeepDayTV.setText(Integer.toString(bookkeepDBHelper.getBookkeepDay()));
    }

    public Bitmap createBitmap(View v) {
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bmp;
    }
}
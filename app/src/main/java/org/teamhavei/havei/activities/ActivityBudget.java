package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.fragments.FragmentNumPad;

import java.util.Calendar;

public class ActivityBudget extends BaseActivity {

    MaterialCardView budgetOverallMCV;
    ProgressBar budgetOverallPB;
    TextView budgetOverallTV;
    TextView budgetOverallRemainTV;

    SharedPreferences pref;

    double budgetOverall;
    double expenditure;

    BookkeepDBHelper dbHelper;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityBudget.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        setSupportActionBar(findViewById(R.id.budget_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new BookkeepDBHelper(ActivityBudget.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        pref = getSharedPreferences(UniToolKit.PREF_SETTINGS, MODE_PRIVATE);

        initView();
        //低优先级实现
        findViewById(R.id.budget_category_container).setVisibility(View.GONE);

        budgetOverallMCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentNumPad(new FragmentNumPad.NumPadCallback() {
                    @Override
                    public void onConfirm(Double number) {
                        pref.edit().putFloat(UniToolKit.PREF_BUDGET, number.floatValue()).apply();
                        update();
                    }
                }, FragmentNumPad.MODE_BOTTOM_SHEET, budgetOverall).show(getSupportFragmentManager(), null);
            }
        });

        expenditure = dbHelper.getExpenditureByMonth(Calendar.getInstance().getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void update() {
        budgetOverall = pref.getFloat(UniToolKit.PREF_BUDGET, 0);
        if (budgetOverall == 0) {
            budgetOverallTV.setText(R.string.unset);
            budgetOverallRemainTV.setText(R.string.unset);
            budgetOverallPB.setProgress(0);
        } else {
            budgetOverallTV.setText(String.format("%.2f",budgetOverall));
            double remain = budgetOverall - expenditure;
            budgetOverallRemainTV.setText(String.format("%.2f",remain));
            if (expenditure < budgetOverall) {
                budgetOverallPB.setProgress((int) (expenditure / budgetOverall * 100));
            } else {
                budgetOverallPB.setProgress(100);
            }
        }
    }

    void initView() {
        budgetOverallMCV = findViewById(R.id.budget_overall);
        budgetOverallPB = findViewById(R.id.budget_progressbar);
        budgetOverallTV = findViewById(R.id.budget_value);
        budgetOverallRemainTV = findViewById(R.id.budget_remain);

        ((TextView) findViewById(R.id.budget_name)).setText(R.string.budget_overall);
    }
}
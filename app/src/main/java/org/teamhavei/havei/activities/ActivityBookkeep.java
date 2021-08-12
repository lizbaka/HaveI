package org.teamhavei.havei.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.teamhavei.havei.R;

public class ActivityBookkeep extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep);
    }
    public void onClickFindAnnualAccountDetail(View view){
        Intent intent = new Intent(this,ActivityBookkeepAnnualAccountDetail.class);
        startActivity(intent);
    }
    public void onClickFindStatisticsView(View view){
        Intent intent = new Intent(this,ActivityBookkeepStatisticsView.class);
        startActivity(intent);
    }
    public void onClickFindPropertyView(View view){
        Intent intent = new Intent(this,ActivityBookkeepPropertyView.class);
        startActivity(intent);
    }
}

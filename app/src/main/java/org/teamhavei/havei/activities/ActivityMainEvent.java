package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.EventDBHelper;

public class ActivityMainEvent extends BaseActivity{

    EventDBHelper dbHelper;

    MaterialCardView dateSelectorCV;
    TextView dateSelectorDateTV;

    public static void startAction(Context context){
        Intent intent = new Intent(context,ActivityMainEvent.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);
        dbHelper = new EventDBHelper(ActivityMainEvent.this,EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
        setSupportActionBar(findViewById(R.id.main_event_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

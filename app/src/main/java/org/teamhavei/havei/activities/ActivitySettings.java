package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.R;

public class ActivitySettings extends BaseActivity {

    MaterialCardView tagsMng;
    MaterialCardView proverbSrc;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivitySettings.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(findViewById(R.id.settings_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        setSettingsEntrances();
    }

    private void initView() {
        tagsMng = findViewById(R.id.settings_tags_mng);
        proverbSrc = findViewById(R.id.settings_proverb_src);
    }

    private void setSettingsEntrances() {
        tagsMng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySettingsTagMng.startAction(ActivitySettings.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
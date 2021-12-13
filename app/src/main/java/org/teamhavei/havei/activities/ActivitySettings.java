package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.R;

public class ActivitySettings extends BaseActivity {

    MaterialCardView tagsMng;
    MaterialCardView accountMng;
    TextView privacyPolicyTV;

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
        accountMng = findViewById(R.id.settings_account_mng);
        privacyPolicyTV = findViewById(R.id.settings_privacy_policy);
    }

    private void setSettingsEntrances() {
        tagsMng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySettingsTagMng.startAction(ActivitySettings.this);
            }
        });
        accountMng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySettingsAccount.startAction(ActivitySettings.this);
            }
        });
        privacyPolicyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://gitee.com/lizbaka/HaveI/blob/master/privacy-policy.md")));
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
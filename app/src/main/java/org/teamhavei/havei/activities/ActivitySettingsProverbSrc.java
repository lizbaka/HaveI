package org.teamhavei.havei.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.teamhavei.havei.R;

public class ActivitySettingsProverbSrc extends BaseActivity {

    public static void startAction(Context context){
        Intent intent = new Intent(context,ActivitySettingsProverbSrc.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_proverb_src);
    }
}
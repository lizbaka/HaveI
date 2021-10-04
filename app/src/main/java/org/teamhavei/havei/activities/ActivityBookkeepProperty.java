package org.teamhavei.havei.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import org.teamhavei.havei.R;

public class ActivityBookkeepProperty extends AppCompatActivity {

    public static void startAction(Context context){
        Intent intent = new Intent(context, ActivityBookkeepProperty.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_property);
        setSupportActionBar(findViewById(R.id.bookkeep_property_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

package org.teamhavei.havei.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import org.teamhavei.havei.R;

public class ActivityBookkeepPropertyView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_property_view);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_bookkeep_property_view);
        setSupportActionBar(toolbar);
    }
}

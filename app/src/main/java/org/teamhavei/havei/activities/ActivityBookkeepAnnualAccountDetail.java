package org.teamhavei.havei.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.sql.DatabaseMetaData;

public class ActivityBookkeepAnnualAccountDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_annual_account_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_bookkeep_annual_account_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}
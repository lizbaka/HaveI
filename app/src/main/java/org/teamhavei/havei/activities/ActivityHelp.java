package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.Event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.EventDBHelper;

public class ActivityHelp extends BaseActivity implements View.OnClickListener{

    static public void startAction(Context context){
        Intent intent = new Intent(context, ActivityHelp.class);
        context.startActivity(intent);
    }

    EventDBHelper eventDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        eventDBHelper = new EventDBHelper(ActivityHelp.this,EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
    }

    @Override
    public void onClick(View v) {

    }
}

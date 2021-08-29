package org.teamhavei.havei.activities;
// TODO: 2021.08.29

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.teamhavei.havei.R;

public class ActivityModifyTag extends AppCompatActivity {

    public static final int TAG_TYPE_EVENT = 0;
    public static final int TAG_TYPE_BOOKKEEP = 1;

    private static final String START_PARAM_TAG_TYPE = "tag_type";
    private static final String START_PARAM_TAG_ID = "tagID";

    public static final int MODE_ADD = 0;
    public static final int MODE_MODIFY = 1;

    public void startAction(Context context, int tagType){
        Intent intent = new Intent(context, ActivityModifyTag.class);
        intent.putExtra(START_PARAM_TAG_TYPE,tagType);
        startActivity(intent);
    }

    public void startAction(Context context, int tagType, int tagID){
        Intent intent = new Intent(context, ActivityModifyTag.class);
        intent.putExtra(START_PARAM_TAG_TYPE,tagType);
        intent.putExtra(START_PARAM_TAG_ID,tagID);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_tag);
        setSupportActionBar(findViewById(R.id.modify_eventtag_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


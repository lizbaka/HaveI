package org.teamhavei.havei.activities;
// TODO: 2021.08.29
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivitySettingsTagMng extends BaseActivity {

    public static final int MODE_EVENT_TAG = 0;
    public static final int MODE_BOOKKEEP_TAG = 1;

    private static final String START_PARAM_MODE = "mode";

    TextView eventTagTV;
    TextView bookkeepTagTV;
    RecyclerView tagListRV;
    FloatingActionButton tagAddFab;

    List<HaveITag> tagList = new ArrayList<>();

    EventDBHelper eventDBHelper;

    public static void startAction(Context context){
        Intent intent = new Intent(context,ActivitySettingsTagMng.class);
        context.startActivity(intent);
    }

    public static void startAction(Context context, int mode){
        Intent intent = new Intent(context,ActivitySettingsTagMng.class);
        intent.putExtra(START_PARAM_MODE,mode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_tags_mng);
        setSupportActionBar(findViewById(R.id.settings_tags_mng_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventDBHelper = new EventDBHelper(ActivitySettingsTagMng.this,EventDBHelper.DB_NAME,null,EventDBHelper.DB_VERSION);
        // TODO: 2021.08.29 initialize bookkeepDBHelper here

        initView();

        tagListRV.setLayoutManager(new GridLayoutManager(ActivitySettingsTagMng.this,4, LinearLayoutManager.VERTICAL,false));

        setViewsOnClickListener();

        switch(getIntent().getIntExtra(START_PARAM_MODE,MODE_EVENT_TAG)){
            case MODE_EVENT_TAG:
                eventTagTV.performClick();
                break;
            case MODE_BOOKKEEP_TAG:
                bookkeepTagTV.performClick();
                break;
        }
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

    private void setViewsOnClickListener(){
        eventTagTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTagTV.setSelected(true);
                bookkeepTagTV.setSelected(false);
                updateTagList();
            }
        });
        bookkeepTagTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTagTV.setSelected(false);
                bookkeepTagTV.setSelected(true);
                updateTagList();
            }
        });
    }

    private void updateTagList(){
        if(eventTagTV.isSelected()){
            tagList.clear();
            tagList.addAll(eventDBHelper.findAllEventTag(true));
            tagListRV.setAdapter(new TagListAdapter(tagList, ActivitySettingsTagMng.this, new TagListAdapter.OnTagClickListener() {
                @Override
                public void onClick(HaveITag tag) {
                    // TODO: 2021.08.29 待modifyTag完成后接入
                }
            }));
            tagAddFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2021.08.29 待modifyTag完成后接入
                }
            });
        }else{

        }
    }

    private void initView(){
        eventTagTV = findViewById(R.id.settings_tag_type_event);
        bookkeepTagTV = findViewById(R.id.settings_tag_type_bookkeep);
        tagListRV = findViewById(R.id.settings_tag_list);
        tagAddFab = findViewById(R.id.settings_tag_add);
    }
}
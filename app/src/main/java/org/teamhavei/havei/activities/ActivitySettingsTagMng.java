package org.teamhavei.havei.activities;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivitySettingsTagMng extends BaseActivity {

    private static final String START_PARAM_MODE = "mode";

    TextView eventTagTV;
    TextView bookkeepTagTV;
    RecyclerView tagListRV;
    FloatingActionButton tagAddFab;

    int tagType;
    List<HaveITag> tagList = new ArrayList<>();
    EventDBHelper eventDBHelper;
    BookkeepDBHelper bookkeepDBHelper;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivitySettingsTagMng.class);
        context.startActivity(intent);
    }

    public static void startAction(Context context, int mode) {
        Intent intent = new Intent(context, ActivitySettingsTagMng.class);
        intent.putExtra(START_PARAM_MODE, mode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_tags_mng);
        setSupportActionBar(findViewById(R.id.settings_tags_mng_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventDBHelper = new EventDBHelper(ActivitySettingsTagMng.this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        bookkeepDBHelper = new BookkeepDBHelper(ActivitySettingsTagMng.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);

        initView();

        tagListRV.setLayoutManager(new GridLayoutManager(ActivitySettingsTagMng.this, 4, LinearLayoutManager.VERTICAL, false));
        tagListRV.setAdapter(new TagListAdapter(tagList, ActivitySettingsTagMng.this, TagListAdapter.ORIENTATION_VERTICAL, new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                ActivityModifyTag.startAction(ActivitySettingsTagMng.this, tagType, tag.getId());
            }
        }));
        tagListRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    tagAddFab.hide();
                } else {
                    tagAddFab.show();
                }
            }
        });

        tagAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityModifyTag.startAction(ActivitySettingsTagMng.this, tagType);
            }
        });

        setViewsOnClickListener();

        switch (tagType = getIntent().getIntExtra(START_PARAM_MODE, UniToolKit.TAG_TYPE_EVENT)) {
            case UniToolKit.TAG_TYPE_EVENT:
                eventTagTV.performClick();
                break;
            case UniToolKit.TAG_TYPE_BOOKKEEP:
                bookkeepTagTV.performClick();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTagList();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewsOnClickListener() {
        eventTagTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTagTV.setSelected(true);
                bookkeepTagTV.setSelected(false);
                tagType = UniToolKit.TAG_TYPE_EVENT;
                updateTagList();
            }
        });
        bookkeepTagTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTagTV.setSelected(false);
                bookkeepTagTV.setSelected(true);
                tagType = UniToolKit.TAG_TYPE_BOOKKEEP;
                updateTagList();
            }
        });
    }

    private void updateTagList() {
        tagList.clear();
        switch (tagType) {
            case UniToolKit.TAG_TYPE_EVENT:
                tagList.addAll(eventDBHelper.findAllEventTag(true));
                break;
            case UniToolKit.TAG_TYPE_BOOKKEEP:
                tagList.addAll(bookkeepDBHelper.findAllBookTag(true));
                break;
        }
        tagListRV.getAdapter().notifyDataSetChanged();
    }

    private void initView() {
        eventTagTV = findViewById(R.id.settings_tag_type_event);
        bookkeepTagTV = findViewById(R.id.settings_tag_type_bookkeep);
        tagListRV = findViewById(R.id.settings_tag_list);
        tagAddFab = findViewById(R.id.settings_tag_add);
    }
}
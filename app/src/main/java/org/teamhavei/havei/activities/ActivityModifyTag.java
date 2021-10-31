package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.teamhavei.havei.event.BookTag;
import org.teamhavei.havei.event.EventTag;
import org.teamhavei.havei.event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.IconAdapter;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivityModifyTag extends BaseActivity {

    public static final String START_PARAM_TAG_TYPE = "tag_type";
    public static final String START_PARAM_TAG_ID = "tagID";

    private static final int EVENT_TAG_DEFAULT_ICON_ID = IconAdapter.DEFAULT_EVENT_TAG_ICON_ID;
    private static final int BOOKKEEP_TAG_DEFAULT_ICON_ID = IconAdapter.DEFAULT_BOOKKEEP_TAG_ICON_ID;
    private static final int NULL_TAG_ID = -1;

    public static final int MODE_ADD = 0;
    public static final int MODE_MODIFY = 1;

    int selectedIconID;
    int tagType;
    int mode;
    int bookkeepType = UniToolKit.BOOKKEEP_TAG_EXPENDITURE;

    EventDBHelper eventDBHelper;
    BookkeepDBHelper bookkeepDBHelper;
    IconAdapter iconAdapter;
    HaveITag mTag;

    EditText tagNameET;
    ImageView iconIV;
    RecyclerView iconListRV;

    public static void startAction(Context context, int tagType) {
        Intent intent = new Intent(context, ActivityModifyTag.class);
        intent.putExtra(START_PARAM_TAG_TYPE, tagType);
        context.startActivity(intent);
    }

    public static void startAction(Context context, int tagType, int tagID) {
        Intent intent = new Intent(context, ActivityModifyTag.class);
        intent.putExtra(START_PARAM_TAG_TYPE, tagType);
        intent.putExtra(START_PARAM_TAG_ID, tagID);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_tag);
        setSupportActionBar(findViewById(R.id.modify_tag_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tagType = getIntent().getIntExtra(START_PARAM_TAG_TYPE, UniToolKit.TAG_TYPE_EVENT);

        eventDBHelper = new EventDBHelper(ActivityModifyTag.this, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        bookkeepDBHelper = new BookkeepDBHelper(ActivityModifyTag.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        iconAdapter = new IconAdapter(ActivityModifyTag.this);

        initView();

        List<HaveITag> iconList = new ArrayList<>();
        List<Integer> tagIDList = new ArrayList<>();
        int tagId = getIntent().getIntExtra(START_PARAM_TAG_ID, NULL_TAG_ID);
        if (tagId == NULL_TAG_ID) {
            mode = MODE_ADD;
        } else {
            mode = MODE_MODIFY;
        }
        switch (tagType) {
            case UniToolKit.TAG_TYPE_EVENT:
                if (mode == MODE_ADD) {
                    getSupportActionBar().setTitle(R.string.modify_event_tag_title_add);
                    mTag = new EventTag();
                    selectedIconID = EVENT_TAG_DEFAULT_ICON_ID;
                } else {
                    getSupportActionBar().setTitle(R.string.modify_event_tag_title_modify);
                    mTag = eventDBHelper.findEventTagById(tagId);
                    selectedIconID = mTag.getIconId();
                }
                findViewById(R.id.modify_tag_bookkeep_type_container).setVisibility(View.GONE);
                tagIDList = iconAdapter.getEventIconIDList();
                break;
            case UniToolKit.TAG_TYPE_BOOKKEEP:
                if (mode == MODE_ADD) {
                    getSupportActionBar().setTitle(R.string.modify_bookkeep_tag_title_add);
                    mTag = new BookTag();
                    selectedIconID = BOOKKEEP_TAG_DEFAULT_ICON_ID;
                } else {
                    getSupportActionBar().setTitle(R.string.modify_bookkeep_tag_title_modify);
                    mTag = bookkeepDBHelper.findBookTagById(tagId);
                    bookkeepType = bookkeepDBHelper.findBookTagById(tagId).getType();
                    selectedIconID = mTag.getIconId();
                }
                tagIDList = iconAdapter.getBookkeepIconIDList();
                findViewById(R.id.modify_tag_bookkeep_type_container).setVisibility(View.VISIBLE);
                if (bookkeepType == UniToolKit.BOOKKEEP_TAG_EXPENDITURE) {
                    findViewById(R.id.modify_tag_bookkeep_expenditure).performClick();
                } else {
                    findViewById(R.id.modify_tag_bookkeep_income).performClick();
                }
                break;
        }

        tagNameET.setText(mTag.getName());
        iconIV.setImageDrawable(iconAdapter.getIcon(selectedIconID));

        for (int i : tagIDList) {
            HaveITag tag = new HaveITag();
            tag.setId(i);
            tag.setIconId(i);
            iconList.add(tag);
        }

        iconListRV.setLayoutManager(new GridLayoutManager(ActivityModifyTag.this, 4, LinearLayoutManager.HORIZONTAL, false));
        iconListRV.setAdapter(new TagListAdapter(iconList, ActivityModifyTag.this, selectedIconID, TagListAdapter.ORIENTATION_HORIZONTAL, new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                selectedIconID = tag.getId();
                iconIV.setImageDrawable(iconAdapter.getIcon(selectedIconID));
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_delete_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_delete_toolbar_save:
                if (checkTagValidate()) {
                    mTag.setName(tagNameET.getText().toString());
                    mTag.setIconId(selectedIconID);
                    if (tagType == UniToolKit.TAG_TYPE_EVENT) {
                        if (mode == MODE_ADD) {
                            eventDBHelper.insertEventTag((EventTag) mTag);
                        } else {
                            eventDBHelper.updateEventTag(mTag.getId(), (EventTag) mTag);
                        }
                    } else {
                        BookTag sTag = (BookTag) mTag;
                        sTag.setType(bookkeepType);
                        if (mode == MODE_ADD) {
                            bookkeepDBHelper.insertBookTag(sTag);
                        } else {
                            bookkeepDBHelper.updateBookTag(sTag.getId(), sTag);
                        }
                    }
                    finish();
                }
                return true;
            case R.id.save_delete_toolbar_delete:
                if (mode == MODE_ADD) {
                    Toast.makeText(ActivityModifyTag.this, R.string.modify_tag_mode_add_hint, Toast.LENGTH_SHORT).show();
                    return true;
                }
                new AlertDialog.Builder(ActivityModifyTag.this)
                        .setTitle(R.string.modify_tag_delete_dialog_title)
                        .setMessage(R.string.modify_tag_delete_dialog_msg)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (tagType == UniToolKit.TAG_TYPE_EVENT) {
                                    eventDBHelper.deleteEventTag((EventTag) mTag);
                                } else {
                                    bookkeepDBHelper.deleteBookTag((BookTag) mTag);
                                }
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    void initView() {
        tagNameET = findViewById(R.id.modify_tag_name);
        iconIV = findViewById(R.id.modify_tag_icon);
        iconListRV = findViewById(R.id.modify_tag_icon_list);

        findViewById(R.id.modify_tag_bookkeep_expenditure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.modify_tag_bookkeep_expenditure).setSelected(true);
                findViewById(R.id.modify_tag_bookkeep_income).setSelected(false);
                bookkeepType = UniToolKit.BOOKKEEP_TAG_EXPENDITURE;
            }
        });
        findViewById(R.id.modify_tag_bookkeep_income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.modify_tag_bookkeep_expenditure).setSelected(false);
                findViewById(R.id.modify_tag_bookkeep_income).setSelected(true);
                bookkeepType = UniToolKit.BOOKKEEP_TAG_INCOME;
            }
        });
    }

    private boolean checkTagValidate() {
        boolean result = true;
        if (tagNameET.getText().toString().equals("")) {
            tagNameET.setError(getString(R.string.modify_tag_empty_name));
            result = false;
        }
        return result;
    }
}


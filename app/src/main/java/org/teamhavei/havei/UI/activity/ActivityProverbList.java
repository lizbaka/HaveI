package org.teamhavei.havei.UI.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.teamhavei.havei.R;
import org.teamhavei.havei.UI.adapter.ProverbCardAdapter;
import org.teamhavei.havei.database.UtilDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivityProverbList extends BaseActivity {

    private static final String START_PARAM_SHOWN_ON_MAIN = "shown_on_main";

    RecyclerView proverbCardListRV;
    ProverbCardAdapter proverbCardAdapter;
    UtilDBHelper utilDBHelper;
    List<String> mProverbList;
    List<String> mRemoveList;
    String mShownOnMain;
    ExtendedFloatingActionButton fab;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityProverbList.class);
        context.startActivity(intent);
    }

    public static void startAction(Context context, String shownOnMain) {
        Intent intent = new Intent(context, ActivityProverbList.class);
        intent.putExtra(START_PARAM_SHOWN_ON_MAIN, shownOnMain);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShownOnMain = getIntent().getStringExtra(START_PARAM_SHOWN_ON_MAIN);


        setContentView(R.layout.activity_proverb_list);
        setSupportActionBar(findViewById(R.id.proverb_list_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utilDBHelper = new UtilDBHelper(ActivityProverbList.this, UtilDBHelper.DB_NAME, null, UtilDBHelper.DB_VERSION);

        proverbCardListRV = findViewById(R.id.proverb_list_card_list);
        fab = findViewById(R.id.proverb_list_add);
        ((NestedScrollView) findViewById(R.id.proverb_list_container)).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
                proverbCardAdapter.notifyDataSetChanged();
            }
        });

        mProverbList = utilDBHelper.getProverbs();
        mRemoveList = new ArrayList<>();
        proverbCardListRV.setLayoutManager(new LinearLayoutManager(this));
        proverbCardAdapter = new ProverbCardAdapter(mProverbList, mRemoveList, ActivityProverbList.this);
        proverbCardListRV.setAdapter(proverbCardAdapter);
        if (mShownOnMain != null) {
            int position = mProverbList.indexOf(mShownOnMain);
            proverbCardListRV.scrollToPosition(position - 1 >= 0 ? position : 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mRemoveList.size() > 0) {
            new AlertDialog.Builder(ActivityProverbList.this)
                    .setNeutralButton(R.string.back, (dialog, which) -> Log.d(TAG, "onBackPressed: back"))
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onBackPressed: cancel");
                            ActivityProverbList.super.onBackPressed();
                        }
                    })
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onBackPressed: confirm");
                            utilDBHelper.deleteProverbs(mRemoveList);
                            ActivityProverbList.super.onBackPressed();
                        }
                    })
                    .setTitle(R.string.proverb_remove_confirm_title)
                    .setMessage(R.string.proverb_remove_confirm_msg)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_text, null);
        EditText newProverbView = view.findViewById(R.id.edittext_dialog_edittext);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProverbList.this)
                .setTitle(getString(R.string.proverb_list_add))
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.save, null);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newProverbView.getText().toString().equals("")) {
                    Toast.makeText(ActivityProverbList.this, getString(R.string.proverb_add_empty_alert), Toast.LENGTH_SHORT).show();
                } else {
                    utilDBHelper.insertProverb(newProverbView.getText().toString());
                    mProverbList.add(newProverbView.getText().toString());
                    dialog.dismiss();
                }
            }
        });
    }
}
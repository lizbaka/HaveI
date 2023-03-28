package org.teamhavei.havei.UI.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.teamhavei.havei.UI.fragment.FragmentNumPad;
import org.teamhavei.havei.event.BookAccount;
import org.teamhavei.havei.event.Bookkeep;
import org.teamhavei.havei.event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.util.Util;
import org.teamhavei.havei.util.IconManager;
import org.teamhavei.havei.UI.adapter.TagListAdapter;
import org.teamhavei.havei.database.BookkeepDBHelper;

import java.util.ArrayList;
import java.util.List;

public class ActivityModifyAccount extends AppCompatActivity {

    private static final String START_PARAM_ACCOUNT_ID = "account_id";
    private static final int MODE_ADD = 0;
    private static final int MODE_MODIFY = 1;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityModifyAccount.class);
        context.startActivity(intent);
    }

    public static void startAction(Context context, int accountId) {
        Intent intent = new Intent(context, ActivityModifyAccount.class);
        intent.putExtra(START_PARAM_ACCOUNT_ID, accountId);
        context.startActivity(intent);
    }

    TextInputEditText nameET;
    TextInputEditText initET;
    ImageView iconIV;
    RecyclerView iconListRV;

    int mode;
    int selectedIconId;
    FragmentNumPad numPad;
    BookAccount account;
    BookkeepDBHelper dbHelper;
    IconManager iconManager;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_account);
        setSupportActionBar(findViewById(R.id.modify_account_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new BookkeepDBHelper(ActivityModifyAccount.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        iconManager = new IconManager(ActivityModifyAccount.this);

        initView();

        numPad = new FragmentNumPad(new FragmentNumPad.NumPadCallback() {
            @Override
            public void onConfirm(Double number) {
                account.setInit(number);
                initET.setText(String.format("%.2f", account.getInit()));
            }
        }, FragmentNumPad.MODE_BOTTOM_SHEET);

        if (getIntent().getIntExtra(START_PARAM_ACCOUNT_ID, -1) != -1) {
            mode = MODE_MODIFY;
            if (getIntent().getIntExtra(START_PARAM_ACCOUNT_ID, -1) == 0) {
                Toast.makeText(ActivityModifyAccount.this, R.string.modify_account_cannot_modify_default, Toast.LENGTH_SHORT).show();
                finish();
            }
            account = dbHelper.findBookAccountById(getIntent().getIntExtra(START_PARAM_ACCOUNT_ID, -1));
            selectedIconId = account.getIconId();
        } else {
            mode = MODE_ADD;
            selectedIconId = IconManager.DEFAULT_ACCOUNT_ICON_ID;
            account = new BookAccount();
        }
        nameET.setText(account.getName());
        initET.setText(String.format("%.2f", account.getInit()));
        iconIV.setImageDrawable(iconManager.getIcon(account.getIconId()));

        initIconList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_delete_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_delete_toolbar_save:
                if (checkValidate()) {
                    account.setName(nameET.getText().toString());
                    account.setIconId(selectedIconId);
                    account.setInit(Double.parseDouble(initET.getText().toString()));
                    if (mode == MODE_ADD) {
                        dbHelper.insertBookAccount(account);
                    } else {
                        dbHelper.updateBookAccount(account.getId(), account);
                    }
                }
                finish();
                return true;
            case R.id.save_delete_toolbar_delete:
                new AlertDialog.Builder(ActivityModifyAccount.this)
                        .setTitle(R.string.modify_account_delete_dialog_title)
                        .setMessage(R.string.modify_account_delete_dialog_content)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Bookkeep> bookkeepList = dbHelper.findBookkeepByAccount(account.getId());
                                for (Bookkeep bookkeep : bookkeepList) {
                                    bookkeep.setAccount(Util.DEFAULT_ACCOUNT_ID);
                                    dbHelper.updateBookkeep(bookkeep.getid(), bookkeep);
                                }
                                dbHelper.deleteBookAccount(account.getId());
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
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        nameET = findViewById(R.id.modify_account_name);
        initET = findViewById(R.id.modify_account_init);
        iconIV = findViewById(R.id.modify_account_icon);
        iconListRV = findViewById(R.id.modify_account_icon_list);

        initET.setInputType(InputType.TYPE_NULL);
        initET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    numPad.setOriginalValue(Double.parseDouble(initET.getText().toString()));
                    numPad.show(getSupportFragmentManager(), "TAG");
                    initET.clearFocus();
                }
            }
        });
    }

    private void initIconList() {
        List<Integer> iconIdList = iconManager.getAccountIconIDList();
        List<HaveITag> tagList = new ArrayList<>();

        for (int i : iconIdList) {
            HaveITag tag = new HaveITag();
            tag.setId(i);
            tag.setIconId(i);
            tagList.add(tag);
        }

        iconListRV.setLayoutManager(new GridLayoutManager(ActivityModifyAccount.this, 4, LinearLayoutManager.HORIZONTAL, false));
        iconListRV.setAdapter(new TagListAdapter(tagList, ActivityModifyAccount.this, account.getIconId(), TagListAdapter.ORIENTATION_HORIZONTAL, new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                selectedIconId = tag.getIconId();
                iconIV.setImageDrawable(iconManager.getIcon(selectedIconId));
            }
        }));
    }

    private boolean checkValidate() {
        boolean result = true;
        if (nameET.getText().toString().equals("")) {
            result = false;
            nameET.setError(getString(R.string.modify_account_empty_name));
        }
        return result;
    }
}
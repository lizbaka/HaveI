package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.teamhavei.havei.event.BookAccount;
import org.teamhavei.havei.R;
import org.teamhavei.havei.fragments.FragmentBookkeepAccountList;

public class ActivitySettingsAccount extends AppCompatActivity {

    public static void startAction(Context context){
        Intent intent = new Intent(context, ActivitySettingsAccount.class);
        context.startActivity(intent);
    }

    FragmentBookkeepAccountList accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_account);
        setSupportActionBar(findViewById(R.id.settings_account_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountList = new FragmentBookkeepAccountList(new FragmentBookkeepAccountList.Callback() {
            @Override
            public void onBookAccountSelected(BookAccount bookAccount) {
                ActivityModifyAccount.startAction(ActivitySettingsAccount.this,bookAccount.getId());
            }

            @Override
            public void operate() {
                ActivityModifyAccount.startAction(ActivitySettingsAccount.this);
            }
        },getString(R.string.account_add));

        getSupportFragmentManager().beginTransaction().replace(R.id.settings_account_mng_placeholder, accountList).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package org.teamhavei.havei.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import org.teamhavei.havei.R;
import org.teamhavei.havei.fragments.*;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends BaseActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;

    private int defaultPage = 1;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        initDrawer();
        initToolbar();
    }

    void initFragments(){
        fragmentList.add(new FragmentAccount());
        fragmentList.add(new FragmentHabit());
        fragmentList.add(new FragmentNote());
    }

    private void initDrawer(){

        mDrawerLayout = findViewById(R.id.main_drawer);

    }

    private void initToolbar(){
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setTitle("");
        mToolbar.inflateMenu(R.menu.toolbar);

        mTabLayout = findViewById(R.id.main_tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragment(fragmentList.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment_layout, fragment);
        transaction.commit();
    }
}
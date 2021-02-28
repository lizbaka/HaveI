package org.teamhavei.havei.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.MainPagerAdapter;
import org.teamhavei.havei.fragments.*;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    ViewPager mViewPager;
    Toolbar mToolbar;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
    TabLayout mTabLayout;

    private String[] tabTitles = new String[] {"记账","习惯","备忘"};

    private int defaultPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();
        initToolbar();
        initPager();
    }

    private void initDrawer(){

        mDrawerLayout = findViewById(R.id.main_drawer);

    }

    private void initPager(){

        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentAccount());
//        fragmentList.add(new FragmentDashBoard());
        fragmentList.add(new FragmentHabit());
        fragmentList.add(new FragmentNote());
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),fragmentList, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        mViewPager.setCurrentItem(defaultPage);
        mTabLayout = findViewById(R.id.main_tab);
        for(int i=0;i<tabTitles.length;i++){
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager,false);
        for(int i=0;i<tabTitles.length;i++){
            mTabLayout.getTabAt(i).setText(tabTitles[i]);
        }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }


//    private void initToolbarAndPager(){
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
//
//
//        List<Fragment> fragmentList = new ArrayList<>();
//        fragmentList.add(new FragmentAccount());
////        fragmentList.add(new FragmentDashBoard());
//        fragmentList.add(new FragmentHabit());
//        fragmentList.add(new FragmentNote());
//
//        for(int i=0;i<tabTitles.length;i++){
//            tabLayout.addTab(tabLayout.newTab());
//        }
//
//        tabLayout.setupWithViewPager(viewPager,false);
//        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),fragmentList, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
//
//        for(int i=0;i<tabTitles.length;i++){
//            tabLayout.getTabAt(i).setText(tabTitles[i]);
//        }
//        tabLayout.getTabAt(defaultSelectedTab).select();
//    }

}
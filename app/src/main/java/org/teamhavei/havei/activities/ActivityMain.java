package org.teamhavei.havei.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.MainPagerAdapter;
import org.teamhavei.havei.fragments.*;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends BaseActivity{

    private static final int ACCOUNT_INDEX = 0;
    private static final int DASHBOARD_INDEX = 1;
    private static final int HABIT_INDEX = 2;
    private static final int TODO_INDEX = 3;

    private String[] pageTitle;
    private int defaultFragment = 1;

    Toolbar mToolbar;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    DrawerLayout mDrawerLayout;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    ExtendedFloatingActionButton fab;
    NavigationView mNavView;

    View.OnClickListener addAccount, addAny, addHabit, addTodo;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFABOnClickListeners();
        fab = findViewById(R.id.main_fab);
        initFragments();
        initDrawer();
        initToolbar();
        initPager();
        initNavigationView();
    }

    void initFABOnClickListeners(){
        addAccount = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityMain.this,"功能：添加记账敬请期待",Toast.LENGTH_SHORT).show();
            }
        };
        addAny = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityMain.this,"添加任意项目功能：敬请期待",Toast.LENGTH_SHORT).show();
            }
        };
        addHabit = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentHabit fragmentHabit = (FragmentHabit)fragmentList.get(HABIT_INDEX);
                fragmentHabit.addHabit();
            }
        };
        addTodo = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityMain.this,"日程功能：敬请期待",Toast.LENGTH_SHORT).show();
            }
        };
    }

    void initFragments(){
        fragmentList.add(new FragmentAccount());
        fragmentList.add(new FragmentDashBoard());
        fragmentList.add(new FragmentHabit());
        fragmentList.add(new FragmentTodo());
    }

    private void initDrawer(){

        mDrawerLayout = findViewById(R.id.main_drawer);

    }

    private void initToolbar(){
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.main_drawer_open, R.string.main_drawer_close);
        mActionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setTitle("");
    }

    void initPager(){
        pageTitle = getResources().getStringArray(R.array.main_page_title);
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),fragmentList, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        for(int i=0;i<fragmentList.size();i++){
            mTabLayout.getTabAt(i).setText(pageTitle[i]);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0://account
                        fab.setOnClickListener(addAccount);
                        break;
                    case 1://dashboard
                        fab.setOnClickListener(addAny);
                        break;
                    case 2://habit
                        fab.setOnClickListener(addHabit);
                        break;
                    case 3://note
                        fab.setOnClickListener(addTodo);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(defaultFragment);
    }

    private void initNavigationView(){
        mNavView = findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_menu_theme:
                        Toast.makeText(ActivityMain.this,"主题切换功能：敬请期待",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_menu_settings:
                        Toast.makeText(ActivityMain.this,"设置：敬请期待",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_toolbar_analyze:
                Toast.makeText(this,"数据分析功能：敬请期待",Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mNavView)){
            mDrawerLayout.closeDrawer(mNavView);
        }
        else {
            super.onBackPressed();
        }
    }
}
package org.teamhavei.havei.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.MainPagerAdapter;
import org.teamhavei.havei.fragments.*;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    private String[] titles = new String[] {"记账","习惯","备忘"};

    private int defaultSelectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabAndPager();
    }


    private void initTabAndPager(){

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentAccount());
//        fragmentList.add(new FragmentDashBoard());
        fragmentList.add(new FragmentHabits());
        fragmentList.add(new FragmentNote());

        for(int i=0;i<titles.length;i++){
            tabLayout.addTab(tabLayout.newTab());
        }

        tabLayout.setupWithViewPager(viewPager,false);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),fragmentList, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));

        for(int i=0;i<titles.length;i++){
            tabLayout.getTabAt(i).setText(titles[i]);
        }
        tabLayout.getTabAt(defaultSelectedTab).select();
    }

}
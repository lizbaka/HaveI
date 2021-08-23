package org.teamhavei.havei.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.teamhavei.havei.fragments.FragmentYearCount;

import java.util.List;

public class YearCountPageAdapter extends FragmentPagerAdapter {

    List<FragmentYearCount> fragmentList;

    public YearCountPageAdapter(@NonNull FragmentManager fm, List<FragmentYearCount> fragmentList, int behavior) {
        super(fm, behavior);
        this.fragmentList = fragmentList;
    }


    @NonNull
    @Override
    public FragmentYearCount getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

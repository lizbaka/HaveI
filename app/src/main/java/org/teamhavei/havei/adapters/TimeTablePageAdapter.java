package org.teamhavei.havei.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.teamhavei.havei.fragments.FragmentTimeTable;

import java.util.List;

public class TimeTablePageAdapter extends FragmentPagerAdapter {

    List<FragmentTimeTable> fragmentList;

    public TimeTablePageAdapter(@NonNull FragmentManager fm, List<FragmentTimeTable> fragmentList, int behavior) {
        super(fm, behavior);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public FragmentTimeTable getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}

package org.teamhavei.havei.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.teamhavei.havei.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentYearCount extends BaseFragment {

    List<TextView> countTV = new ArrayList<>(12);
    TextView yearTV;

    List<Integer> countList = new ArrayList<>(12);
    int year;

    private final FragmentYearCountSocket socket;

    public interface FragmentYearCountSocket {
        void updateCountList(int year, List<Integer> count);
    }

    public void update() {
        socket.updateCountList(year, countList);
        for (int i = 0; i < countList.size(); i++) {
            countTV.get(i).setText(countList.get(i) + getString(R.string.times));
        }
    }

    public FragmentYearCount(int year, FragmentYearCountSocket socket) {
        this.year = year;
        this.socket = socket;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_yearly_count, container,false);
        initView(view);
        update();
        yearTV.setText(Integer.toString(year));
        return view;
    }

    private void initView(View view) {
        yearTV = view.findViewById(R.id.year_count_year);
        countTV.clear();
        countTV.add(0, view.findViewById(R.id.year_count_1));
        countTV.add(1, view.findViewById(R.id.year_count_2));
        countTV.add(2, view.findViewById(R.id.year_count_3));
        countTV.add(3, view.findViewById(R.id.year_count_4));
        countTV.add(4, view.findViewById(R.id.year_count_5));
        countTV.add(5, view.findViewById(R.id.year_count_6));
        countTV.add(6, view.findViewById(R.id.year_count_7));
        countTV.add(7, view.findViewById(R.id.year_count_8));
        countTV.add(8, view.findViewById(R.id.year_count_9));
        countTV.add(9, view.findViewById(R.id.year_count_10));
        countTV.add(10, view.findViewById(R.id.year_count_11));
        countTV.add(11, view.findViewById(R.id.year_count_12));
    }
}

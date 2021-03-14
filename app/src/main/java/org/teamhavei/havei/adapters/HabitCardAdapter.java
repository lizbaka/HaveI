package org.teamhavei.havei.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.habit.Habit;
import org.teamhavei.havei.R;

import java.util.List;

public class HabitCardAdapter extends RecyclerView.Adapter<HabitCardAdapter.ViewHolder> {

    private List<Habit> mHabitList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView tag;
        TextView frequency;
        TextView times;
        TextView last;

        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.card_habit_name);
            tag = (TextView) view.findViewById(R.id.card_habit_tag);
            frequency = (TextView) view.findViewById(R.id.card_habit_frequency);
            times = (TextView) view.findViewById(R.id.card_habit_times);
            last = (TextView) view.findViewById(R.id.card_habit_last);
        }

    }

    public HabitCardAdapter(List<Habit> habitList){
        mHabitList = habitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_habit_card,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = mHabitList.get(position);
        holder.name.setText(habit.getHabitName());
        holder.tag.setText(habit.getHabitTag());
        String FrequencyType = new String();
        switch(habit.getHabitFrequencyType()){
            case Habit.FREQUENCY_TYPE_DAY:
                FrequencyType = "天";
                break;
            case Habit.FREQUENCY_TYPE_WEEK:
                FrequencyType = "周";
                break;
            case Habit.FREQUENCY_TYPE_MONTH:
                FrequencyType = "月";
                break;
        }
        holder.frequency.setText(habit.getHabitFrequency() + "次每" + FrequencyType);
        holder.times.setText("已执行" + habit.getHabitExecTimes() + "次");
        holder.last.setText("上次执行时间：[未实现]");
    }

    @Override
    public int getItemCount() {
        return mHabitList.size();
    }
}

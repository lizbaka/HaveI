package org.teamhavei.havei.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.activities.ActivityHabitDetail;
import org.teamhavei.havei.habit.Habit;
import org.teamhavei.havei.R;

import java.util.List;

public class HabitCardAdapter extends RecyclerView.Adapter<HabitCardAdapter.ViewHolder> {

    private List<Habit> mHabitList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView tag;

        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.habit_card_name);
            tag = (TextView) view.findViewById(R.id.habit_card_tag);
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
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ActivityHabitDetail.startAction(parent.getContext(),holder.name.getText().toString());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //增加执行记录
                //动画
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = mHabitList.get(position);
        holder.name.setText(habit.getHabitName());
        holder.tag.setText(habit.getHabitTag());
    }

    @Override
    public int getItemCount() {
        return mHabitList.size();
    }
}

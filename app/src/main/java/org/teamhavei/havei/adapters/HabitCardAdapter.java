package org.teamhavei.havei.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.activities.ActivityHabitDetail;
import org.teamhavei.havei.habit.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.habit.HabitDBHelper;

import java.util.List;

public class HabitCardAdapter extends RecyclerView.Adapter<HabitCardAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";

    private List<Habit> mHabitList;
    private HabitDBHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView tag;
        TextView done;
        TextView count;
        Habit mHabit;

        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.habit_card_name);
            tag = (TextView) view.findViewById(R.id.habit_card_tag);
            done = (TextView) view.findViewById(R.id.habit_card_done);
            count = (TextView) view.findViewById(R.id.habit_card_count);
        }

    }

    public HabitCardAdapter(List<Habit> habitList,Context context){
        mHabitList = habitList;
        mContext = context;
        dbHelper = new HabitDBHelper(mContext,HabitDBHelper.DB_NAME,null,HabitDBHelper.DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_habit_card,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ActivityHabitDetail.startAction(parent.getContext(),holder.mHabit.getHabitName());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int habitID = holder.mHabit.getHabitID();
                if(dbHelper.switchHabitExec(habitID)){
                    holder.done.setText("太棒了！今天已经完成");
                }
                else{
                    holder.done.setText("今天尚未完成");
                }
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mHabit = mHabitList.get(position);
        String habitName = holder.mHabit.getHabitName();
        String habitTag = holder.mHabit.getHabitTag();
        int habitID = holder.mHabit.getHabitID();
        holder.name.setText(habitName);
        holder.tag.setText(habitTag);
        int count = dbHelper.getHabitExecCount(habitID);
        holder.count.setText("您已坚持完成习惯 " + count + " 次，持之以恒！");
        if(dbHelper.isHabitDoneToday(habitID)){
            holder.done.setText("太棒了！今天已经完成");
        }
        else{
            holder.done.setText("今天尚未完成");
        }
    }

    @Override
    public int getItemCount() {
        return mHabitList.size();
    }
}

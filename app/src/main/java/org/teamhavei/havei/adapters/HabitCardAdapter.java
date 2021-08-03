package org.teamhavei.havei.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.List;

public class HabitCardAdapter extends RecyclerView.Adapter<HabitCardAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";

    private List<Habit> mHabitList;
    private EventDBHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameView;
        TextView tagView;
        ImageView statusIcon;
        ImageView iconView;
        Habit mHabit;
        Boolean isHabitDoneToday;

        public ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.habit_card_name);
            tagView = (TextView) view.findViewById(R.id.habit_card_tag);
            statusIcon = (ImageView) view.findViewById(R.id.habit_card_status);
            iconView = (ImageView) view.findViewById(R.id.habit_card_icon);
        }

    }

    public HabitCardAdapter(List<Habit> habitList,Context context){
        mHabitList = habitList;
        mContext = context;
        dbHelper = new EventDBHelper(mContext, EventDBHelper.DB_NAME,null, EventDBHelper.DB_VERSION);
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
                // TODO: 2021/7/14 实现习惯详情界面后接入
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO: 2021/7/14 实现习惯标记功能
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mHabit = mHabitList.get(position);
        String name = holder.mHabit.getName();
        int tagId = holder.mHabit.getTagId();
        String tag = dbHelper.findEventTagById(tagId).getName();
        int habitID = holder.mHabit.getId();
        holder.nameView.setText(name);
        holder.tagView.setText(tag);
        holder.isHabitDoneToday = dbHelper.isHabitDoneToday(habitID);
        if(holder.isHabitDoneToday){
            holder.statusIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_check_24_color_pv));
        }
        else{
            holder.statusIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_close_24_white));
        }
    }

    @Override
    public int getItemCount() {
        return mHabitList.size();
    }
}

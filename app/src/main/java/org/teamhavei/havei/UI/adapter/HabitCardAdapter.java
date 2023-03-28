package org.teamhavei.havei.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.event.EventTag;
import org.teamhavei.havei.event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.util.IconManager;
import org.teamhavei.havei.util.Util;
import org.teamhavei.havei.UI.activity.ActivityHabitDetail;
import org.teamhavei.havei.database.EventDBHelper;

import java.util.Date;
import java.util.List;

public class HabitCardAdapter extends RecyclerView.Adapter<HabitCardAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";

    private List<Habit> mHabitList;
    private final EventDBHelper dbHelper;
    private final Context mContext;
    private final IconManager iconManager;

    public void setHabitList(List<Habit> mHabitList) {
        this.mHabitList = mHabitList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView tagView;
        ImageView statusIcon;
        ImageView iconView;
        Habit mHabit;
        Boolean isHabitDoneToday;

        public ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.habit_card_name);
            tagView = view.findViewById(R.id.habit_card_tag);
            statusIcon = view.findViewById(R.id.habit_card_status);
            iconView = view.findViewById(R.id.habit_card_icon);
        }

    }

    public HabitCardAdapter(List<Habit> habitList, Context context) {
        mHabitList = habitList;
        mContext = context;
        dbHelper = new EventDBHelper(mContext, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
        iconManager = new IconManager(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_habit_card, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHabitDetail.startAction(mContext, holder.mHabit);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.isHabitDoneToday = dbHelper.switchHabitExec(holder.mHabit.getId(), Util.eventDateFormatter(new Date()));
                if (holder.isHabitDoneToday) {
                    holder.statusIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_check_24_color_pv));
                    Toast.makeText(mContext,R.string.habit_done,Toast.LENGTH_SHORT).show();
                } else {
                    holder.statusIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_close_24_white));
                    Toast.makeText(mContext,R.string.habit_undone,Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mHabit = mHabitList.get(position);
        EventTag tag = dbHelper.findEventTagById(holder.mHabit.getTagId());
        holder.nameView.setText(holder.mHabit.getName());
        holder.tagView.setText(tag.getName());
        holder.iconView.setImageDrawable(iconManager.getIcon(tag.getIconId()));
        holder.isHabitDoneToday = dbHelper.isHabitDoneToday(holder.mHabit.getId());
        if (holder.isHabitDoneToday) {
            holder.statusIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_check_24_color_pv));
        } else {
            holder.statusIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_baseline_close_24_white));
        }
    }

    @Override
    public int getItemCount() {
        return mHabitList.size();
    }
}

package org.teamhavei.havei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.Event.EventTag;
import org.teamhavei.havei.R;

import java.util.List;

public class EventTagSelectorAdapter extends RecyclerView.Adapter<EventTagSelectorAdapter.ViewHolder> {

    public static final int VIEW_TYPE_TAGS = 1;
    public static final int VIEW_TYPE_SETTINGS = 2;

    List<EventTag> mEventTagList;
    Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tagNameView;
        ImageView tagIconView;
        EventTag mEventTag;

        public ViewHolder(View view){
            super(view);
            tagNameView = (TextView) view.findViewById(R.id.icon_title_title);
            tagIconView = (ImageView) view.findViewById(R.id.icon_title_icon);
        }

    }

    public EventTagSelectorAdapter(List<EventTag> eventTagList, Context context){
        mEventTagList = eventTagList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        switch(viewType) {
            case VIEW_TYPE_TAGS:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_icon_title, parent, false);
                holder = new ViewHolder(view);
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO: 2021.07.20 为标签选择器实现onClickListener
                    }
                });
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventTagSelectorAdapter.ViewHolder holder, int position) {
        holder.mEventTag = mEventTagList.get(position);
        holder.tagIconView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_help_24));// TODO: 2021.07.20 实现iconAdapter后接入
        holder.tagNameView.setText(holder.mEventTag.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

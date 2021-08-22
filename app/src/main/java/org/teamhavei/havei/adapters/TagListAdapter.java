package org.teamhavei.havei.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.R;

import java.util.List;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> {

    List<HaveITag> mTagList;
    Context mContext;
    IconAdapter iconAdapter;
    int selectedItem;

    private final OnTagClickListener onTagClickListener;

    public interface OnTagClickListener {
        void onClick(HaveITag tag);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tagNameView;
        ImageView tagIconView;
        HaveITag mTag;

        public ViewHolder(View view) {
            super(view);
            tagNameView = view.findViewById(R.id.icon_title_title);
            tagIconView = view.findViewById(R.id.icon_title_icon);
        }

    }

    /**
     * 构造tagListAdapter
     *
     * @param tagList            tag列表
     * @param context            recyclerview上下文
     * @param selectedID         调用时默认选中的item
     * @param onTagClickListener tag点击事件
     */
    public TagListAdapter(List<HaveITag> tagList, Context context) {
        this.mTagList = tagList;
        this.mContext = context;
        iconAdapter = new IconAdapter(context);
    }
    public TagListAdapter(List<HaveITag> tagList, Context context, int selectedID, OnTagClickListener onTagClickListener) {
        this.mTagList = tagList;
        this.mContext = context;
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getId() == selectedID) {
                selectedItem = i;
                break;
            }
        }
        this.onTagClickListener = onTagClickListener;
        iconAdapter = new IconAdapter(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_icon_title, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTagClickListener.onClick(holder.mTag);
                int oldSelectedItem = selectedItem;
                selectedItem = holder.getAdapterPosition();
                notifyItemChanged(oldSelectedItem);
                notifyItemChanged(selectedItem);
                holder.tagNameView.requestFocus();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagListAdapter.ViewHolder holder, int position) {
        holder.mTag=mTagList.get(position);
        holder.tagIconView.setImageDrawable(iconAdapter.getIcon(holder.mTag.getIconId()));
        holder.tagNameView.setText(holder.mTag.getName());
        if(position == selectedItem){
            holder.itemView.setBackgroundResource(R.color.amber_500);
        }else{
            holder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return mTagList.size();
    }
}

package org.teamhavei.havei.adapters;

import android.content.Context;
import android.util.Log;
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

    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;

    private static final String TAG = "DEBUG";

    private static final int MODE_SELECT = 0;
    private static final int MODE_CLICK = 1;

    List<HaveITag> mTagList;
    Context mContext;
    IconAdapter iconAdapter;

    int selectedItem;
    int mode = MODE_SELECT;
    int orientation;

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
     *
     * @param tagList 包含HaveITag的List
     * @param context 上下文
     * @param selectedID 默认选取的Tag的ID
     * @param orientation 滚动方向
     * @param onTagClickListener 项目点击事件
     */
    public TagListAdapter(List<HaveITag> tagList, Context context, int selectedID, int orientation, OnTagClickListener onTagClickListener) {
        mode = MODE_SELECT;
        this.mTagList = tagList;
        this.mContext = context;
        this.orientation = orientation;
        this.onTagClickListener = onTagClickListener;
        iconAdapter = new IconAdapter(context);
        Boolean found = false;
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getId() == selectedID) {
                selectedItem = i;
                found = true;
                break;
            }
        }
        if (!found && tagList.size() > 0) {
            selectedID = tagList.get(0).getId();
            selectedItem = 0;
            Log.d(TAG, "TagListAdapter: Initial tag ID not found or deleted");
            onTagClickListener.onClick(mTagList.get(0));
        }
    }

    /**
     *
     * @param tagList 包含HaveITag的List
     * @param context 上下文
     * @param orientation 滚动方向
     * @param onTagClickListener 项目点击事件
     */
    public TagListAdapter(List<HaveITag> tagList, Context context, int orientation, OnTagClickListener onTagClickListener) {
        mode = MODE_CLICK;
        this.mTagList = tagList;
        this.mContext = context;
        this.onTagClickListener = onTagClickListener;
        this.orientation = orientation;
        iconAdapter = new IconAdapter(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_icon_title, parent, false);
        ViewHolder holder = new ViewHolder(view);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if(orientation==ORIENTATION_HORIZONTAL){
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }else{
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        holder.itemView.setLayoutParams(params);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTagClickListener.onClick(holder.mTag);
                int oldSelectedItem = selectedItem;
                selectedItem = holder.getAdapterPosition();
                notifyItemChanged(oldSelectedItem);
                notifyItemChanged(selectedItem);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagListAdapter.ViewHolder holder, int position) {
        holder.mTag = mTagList.get(position);
        holder.tagIconView.setImageDrawable(iconAdapter.getIcon(holder.mTag.getIconId()));
        holder.tagNameView.setText(holder.mTag.getName());
        if (mode == MODE_SELECT) {
            holder.itemView.setSelected(position == selectedItem);
        }
    }

    @Override
    public int getItemCount() {
        return mTagList.size();
    }
}

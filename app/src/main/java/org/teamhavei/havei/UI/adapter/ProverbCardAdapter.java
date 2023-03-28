package org.teamhavei.havei.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.R;

import java.util.List;

public class ProverbCardAdapter extends RecyclerView.Adapter<ProverbCardAdapter.ViewHolder> {

    private static final String TAG = "DEBUG";

    public void setmProverbList(List<String> mProverbList) {
        this.mProverbList = mProverbList;
    }

    private List<String> mProverbList;
    private final List<String> mRemoveList;
    private final Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView proverbView;
        ImageView favoriteView;
        Boolean like;

        public ViewHolder(View view) {
            super(view);
            proverbView = view.findViewById(R.id.proverb_card_proverb);
            favoriteView = view.findViewById(R.id.proverb_card_favorite);
            like = true;
            view.setClickable(false);
        }
    }

    public ProverbCardAdapter(List<String> proverbList, List<String> removeList, Context context){
        mProverbList = proverbList;
        mRemoveList = removeList;
        mContext = context;
    }

    @NonNull
    @Override
    public ProverbCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_proverb_card,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.favoriteView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               if(holder.like) {
                   holder.like = false;
                   holder.favoriteView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_favorite_24_white));
                   mRemoveList.add(holder.proverbView.getText().toString());
               }else{
                   holder.like = true;
                   holder.favoriteView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_favorite_24_red));
                   mRemoveList.remove(holder.proverbView.getText().toString());
               }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ProverbCardAdapter.ViewHolder holder, int position) {
        holder.favoriteView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_favorite_24_red));
        holder.proverbView.setText(mProverbList.get(position));
    }

    @Override
    public int getItemCount() {
        return mProverbList.size();
    }
}

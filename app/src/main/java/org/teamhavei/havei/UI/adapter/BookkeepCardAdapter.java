package org.teamhavei.havei.UI.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.event.BookTag;
import org.teamhavei.havei.event.Bookkeep;
import org.teamhavei.havei.R;
import org.teamhavei.havei.util.IconManager;
import org.teamhavei.havei.util.Util;
import org.teamhavei.havei.database.BookkeepDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookkeepCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DATE_CARD_ID = -1;

    private static final int VIEW_TYPE_BOOKKEEP = 0;
    private static final int VIEW_TYPE_DATE = 1;

    private final List<Bookkeep> mBookList;
    private final BookkeepDBHelper dbHelper;
    private final IconManager iconManager;
    private final Context mContext;

    public interface BookkeepCardCallBack{
        void onLongClick(Bookkeep bookkeep);
        void onClick(Bookkeep bookkeep);
    }

    BookkeepCardCallBack mCallback;


    public BookkeepCardAdapter(List<Bookkeep> bookList, Context context, BookkeepCardCallBack callback) {
        mBookList = bookList;
        mContext = context;
        mCallback = callback;
        dbHelper = new BookkeepDBHelper(mContext, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        iconManager = new IconManager(mContext);
        adjustListForGrouping();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_BOOKKEEP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_bookkeep_record_card, parent, false);
            holder = new BookkeepHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onClick(((BookkeepHolder)holder).getmBookkeep());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCallback.onLongClick(((BookkeepHolder)holder).getmBookkeep());
                    return true;
                }
            });
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_bookkeep_date_card, parent, false);
            holder = new BookkeepCardAdapter.DateHolder(view);
        }
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Bookkeep bookkeep = mBookList.get(position);
        if (bookkeep.getid() == DATE_CARD_ID) {
            ((DateHolder) holder).bind(bookkeep);
        } else {
            ((BookkeepHolder) holder).bind(bookkeep);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mBookList.get(position).getid() == -1 ? VIEW_TYPE_DATE : VIEW_TYPE_BOOKKEEP;
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    private void adjustListForGrouping() {
        if (mBookList.isEmpty()) {
            return;
        }
        Bookkeep placeHolder = new Bookkeep();
        placeHolder.setId(DATE_CARD_ID);
        placeHolder.setTime(mBookList.get(0).gettime());
        placeHolder.setPM(0);
        for (int i = 0; i < mBookList.size() && mBookList.get(i).gettime().equals(placeHolder.gettime()); i++) {
            placeHolder.setPM(placeHolder.getPM() + mBookList.get(i).getPM());
        }
        mBookList.add(0, placeHolder);
        for (int i = 1; i < mBookList.size(); i++) {
            if (!mBookList.get(i).gettime().equals(mBookList.get(i - 1).gettime())) {
                placeHolder = new Bookkeep();
                placeHolder.setId(DATE_CARD_ID);
                placeHolder.setTime(mBookList.get(i).gettime());
                placeHolder.setPM(0);
                for (int j = i; j < mBookList.size() && mBookList.get(j).gettime().equals(placeHolder.gettime()); j++) {
                    placeHolder.setPM(placeHolder.getPM() + mBookList.get(j).getPM());
                }
                mBookList.add(i, placeHolder);
            }
        }
    }

    public void notifyBookListChanged() {
        adjustListForGrouping();
        notifyDataSetChanged();
    }

    class BookkeepHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView tagView;
        TextView bookView;
        TextView ioTV;
        ImageView iconView;
        Bookkeep mBookkeep;

        public Bookkeep getmBookkeep() {
            return mBookkeep;
        }

        public BookkeepHolder(@NonNull View view) {
            super(view);
            titleView = view.findViewById(R.id.bookkeep_record_card_title);
            tagView = view.findViewById(R.id.bookkeep_record_card_tag);
            bookView = view.findViewById(R.id.bookkeep_record_card_io);
            iconView = view.findViewById(R.id.bookkeep_record_card_icon);
            ioTV = view.findViewById(R.id.bookkeep_record_card_io);
        }

        public void bind(Bookkeep bookkeep) {
            this.mBookkeep = bookkeep;
            BookTag tag = dbHelper.findBookTagById(mBookkeep.gettag());
            if (mBookkeep.getname().equals("")) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(mBookkeep.getname());
            }
            if (mBookkeep.getPM() > 0) {
                ioTV.setText(String.format("+%.2f", mBookkeep.getPM()));
                ioTV.setTextColor(ContextCompat.getColor(mContext, R.color.green_500));
            } else {
                ioTV.setText(String.format("%.2f", mBookkeep.getPM()));
                ioTV.setTextColor(ContextCompat.getColor(mContext, R.color.red_500));
            }
            tagView.setText(tag.getName());
            iconView.setImageDrawable(iconManager.getIcon(tag.getIconId()));
        }
    }

    class DateHolder extends RecyclerView.ViewHolder {

        TextView dateTV;
        TextView weekdayTV;
        TextView ioTV;
        Bookkeep mBookkeep;

        public DateHolder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.bookkeep_date_card_date);
            weekdayTV = itemView.findViewById(R.id.bookkeep_date_card_weekday);
            ioTV = itemView.findViewById(R.id.bookkeep_date_card_io);
        }

        public void bind(Bookkeep bookkeep) {
            this.mBookkeep = bookkeep;
            if (mBookkeep.getPM() > 0) {
                ioTV.setText(String.format("+%.2f", mBookkeep.getPM()));
                ioTV.setTextColor(ContextCompat.getColor(mContext, R.color.green_500));
            } else {
                ioTV.setText(String.format("%.2f", mBookkeep.getPM()));
                ioTV.setTextColor(ContextCompat.getColor(mContext, R.color.red_500));
            }
            Date date = Util.eventDateParser(bookkeep.gettime());
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            SimpleDateFormat swdf = new SimpleDateFormat("EEEE");
            dateTV.setText(sdf.format(date));
            weekdayTV.setText(swdf.format(date));
        }
    }
}

package org.teamhavei.havei.adapters;
// TODO: 2021/8/10 做好适配

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.EventTag;
import org.teamhavei.havei.Event.Habit;
import org.teamhavei.havei.R;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.databases.EventDBHelper;

import java.util.List;

public class BookkeepCardAdapter extends RecyclerView.Adapter<BookkeepCardAdapter.ViewHolder> {


    private List<Bookkeep> mBookList;
    private BookkeepDBHelper dbHelper;
    private IconAdapter iconAdapter;
    private Context mContext;


    public BookkeepCardAdapter(List<Bookkeep> bookList,Context context)
    {
        mBookList = bookList;
        mContext = context;
        dbHelper = new BookkeepDBHelper(mContext, BookkeepDBHelper.DB_NAME,null, BookkeepDBHelper.DATABASE_VERSION);
        iconAdapter = new IconAdapter(mContext);

    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_bookkeep_record_card,parent,false);
        BookkeepCardAdapter.ViewHolder holder = new BookkeepCardAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });


        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mBookkeep = mBookList.get(position);
        BookTag tag = dbHelper.findBookTagById(holder.mBookkeep.gettag());
        holder.titleView.setText(holder.mBookkeep.getname());
        holder.titleView.setText(holder.mBookkeep.getPM().toString());
        holder.tagView.setText(tag.getName());
        holder.iconView.setImageDrawable(iconAdapter.getIcon(tag.getIconId()));


    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleView;
        TextView tagView;
        TextView bookView;
        ImageView iconView;
        Bookkeep mBookkeep;


        public ViewHolder(@NonNull View View) {
            super(View);
            titleView=(TextView) View.findViewById(R.id.todo_card_title);
            tagView=(TextView) View.findViewById(R.id.bookkeep_record_card_tag);
            bookView=(TextView) View.findViewById(R.id.bookkeep_record_card_io);
            iconView=(ImageView) View.findViewById(R.id.bookkeep_note_card_icon);
        }
    }
}

package org.teamhavei.havei.UI.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.teamhavei.havei.event.EventTag;
import org.teamhavei.havei.event.Todo;
import org.teamhavei.havei.R;
import org.teamhavei.havei.util.IconManager;
import org.teamhavei.havei.util.Util;
import org.teamhavei.havei.database.EventDBHelper;

import java.util.Calendar;
import java.util.List;

public class TodoCardAdapter extends RecyclerView.Adapter<TodoCardAdapter.ViewHolder> {

    public static final int TODO_CARD_TYPE_NORMAL = 0;
    public static final int TODO_CARD_TYPE_SIMPLE = 1;

    private static final String TAG = "DEBUG";

    private final int cardType;

    List<Todo> mTodoList;
    Context mContext;
    EventDBHelper dbHelper;

    public void setTodoList(List<Todo> mTodoList) {
        this.mTodoList = mTodoList;
    }

    private final OnCardClickListener onCardClickListener;
    public interface OnCardClickListener {
        void onClick(Todo todo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iconIV;
        TextView todoTitleTV;
        TextView todoTagTV;
        TextView todoTimeTV;
        TextView todoDateTV;
        Todo todo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconIV = itemView.findViewById(R.id.todo_card_icon);
            todoTitleTV = itemView.findViewById(R.id.todo_card_title);
            todoTagTV = itemView.findViewById(R.id.todo_card_tag);
            todoTimeTV = itemView.findViewById(R.id.todo_card_time);
            todoDateTV = itemView.findViewById(R.id.todo_card_date);
        }
    }

    public TodoCardAdapter(Context context, List<Todo> todoList, int cardType, OnCardClickListener listener) {
        this.mContext = context;
        this.mTodoList = todoList;
        if (cardType == 0 || cardType == 1) {
            this.cardType = cardType;
        } else {
            this.cardType = 0;
            Log.d(TAG, "TodoCardAdapter: no such card type, using normal type");
        }
        onCardClickListener = listener;
        dbHelper = new EventDBHelper(mContext, EventDBHelper.DB_NAME, null, EventDBHelper.DB_VERSION);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (cardType) {
            case TODO_CARD_TYPE_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_todo_card, parent, false);
            case TODO_CARD_TYPE_SIMPLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_todo_card_simple, parent, false);
        }
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClickListener.onClick(holder.todo);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.todo = mTodoList.get(position);
        EventTag tag = dbHelper.findEventTagById(holder.todo.getTagId());
        holder.todoTitleTV.setText(holder.todo.getName());
        holder.todoTagTV.setText(tag.getName());
        holder.iconIV.setImageDrawable(new IconManager(mContext).getIcon(tag.getIconId()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Util.eventDatetimeParser(holder.todo.getDateTime()));
        holder.todoDateTV.setText(Util.eventDateFormatter(calendar.getTime()));
        if(holder.todo.isDone()){
            holder.todoTimeTV.setText(R.string.finished);
        }else{
            holder.todoTimeTV.setText(Util.eventTimeFormatter(calendar.getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }
}

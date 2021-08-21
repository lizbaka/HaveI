package org.teamhavei.havei.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.Event.HaveIEvent;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentTimeTable extends BaseFragment {

    private static final long MILLISECONDS_PER_DAY = 86400000;
    private static final int CARD_MARGIN = 5;

    Calendar startOfWeek = Calendar.getInstance();

    TextView monthTV;
    FrameLayout FL;
    List<TextView> dateTVList = new ArrayList<>();
    List<TimeTableEvent> eventList = new ArrayList<>();

    int dayWidth;
    int hourHeight;
    int dayHeight;
    int labelOffset;
    boolean showTimeLine;

    private final TimetableSocket timetableSocket;

    public interface TimetableSocket {
        void onCardClick(HaveIEvent event);
        void updateEventList(Calendar startOfWeek, List<TimeTableEvent> eventList);
    }

    public FragmentTimeTable(Calendar startOfWeek, TimetableSocket socket, boolean showTimeLine) {
        this.startOfWeek.set(Calendar.YEAR, startOfWeek.get(Calendar.YEAR));
        this.startOfWeek.set(Calendar.MONTH, startOfWeek.get(Calendar.MONTH));
        this.startOfWeek.set(Calendar.DAY_OF_YEAR, startOfWeek.get(Calendar.DAY_OF_YEAR));
        this.timetableSocket = socket;
        this.showTimeLine = showTimeLine;
    }

    public static class TimeTableEvent {
        private Calendar startTime;
        private String text;
        private HaveIEvent event;

        public Calendar getStartTime() {
            return startTime;
        }

        public String getText() {
            return text;
        }

        public HaveIEvent getEvent() {
            return event;
        }

        public TimeTableEvent(Calendar startTime, String text, HaveIEvent event) {
            this.startTime = (Calendar) startTime.clone();
            this.text = text;
            this.event = event;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + UniToolKit.eventDateFormatter(startOfWeek.getTime()));
        View view = inflater.inflate(R.layout.fragment_event_timetable, container, false);
        initView(view);

        timetableSocket.updateEventList(startOfWeek, eventList);

        monthTV.setText(new SimpleDateFormat("MMM").format(startOfWeek.getTime()));
        for (int i = 0; i < 7; i++) {
            dateTVList.get(i).setText(Integer.toString(startOfWeek.get(Calendar.DAY_OF_MONTH)));
            startOfWeek.add(Calendar.DAY_OF_MONTH, 1);
        }
        startOfWeek.add(Calendar.DAY_OF_YEAR, -7);

        setCardSize();

        if (showTimeLine) {
            (view.findViewById(R.id.tt_timeline)).setVisibility(View.VISIBLE);
        } else {
            (view.findViewById(R.id.tt_timeline)).setVisibility(View.GONE);
        }
        view.findViewById(R.id.tt_tv_day1).getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        view.findViewById(R.id.tt_tv_day1).getViewTreeObserver().removeOnPreDrawListener(this);
                        dayWidth = view.findViewById(R.id.tt_tv_day1).getWidth(); // 获取宽度
                        if (eventList != null) {
                            dayWidth = view.findViewById(R.id.tt_tv_day1).getWidth();
                            FL.removeAllViews();
                            for (TimeTableEvent event : eventList) {
                                configCard(event);
                            }
                        }
                        return true;
                    }
                });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        timetableSocket.updateEventList(startOfWeek,eventList);
        if (eventList != null) {
            FL.removeAllViews();
            for (TimeTableEvent event : eventList) {
                configCard(event);
            }
        }
    }

    private void initView(View view) {
        monthTV = view.findViewById(R.id.tt_tv_month);
        dateTVList.clear();
        dateTVList.add(view.findViewById(R.id.tt_tv_day1));
        dateTVList.add(view.findViewById(R.id.tt_tv_day2));
        dateTVList.add(view.findViewById(R.id.tt_tv_day3));
        dateTVList.add(view.findViewById(R.id.tt_tv_day4));
        dateTVList.add(view.findViewById(R.id.tt_tv_day5));
        dateTVList.add(view.findViewById(R.id.tt_tv_day6));
        dateTVList.add(view.findViewById(R.id.tt_tv_day7));
        FL = view.findViewById(R.id.tt_FL);
    }

    private void configCard(TimeTableEvent event) {
        int dayOfWeek = event.getStartTime().get(Calendar.DAY_OF_WEEK);
        dayOfWeek = (dayOfWeek - 1 + 7) % 7;/*0~6, 周日为第一(0)天*/

        Calendar startOfDay = (Calendar) event.getStartTime().clone();
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 0);
        startOfDay.set(Calendar.SECOND, 0);
        long deltaMilli = event.getStartTime().getTimeInMillis() - startOfDay.getTimeInMillis();

        MaterialCardView card = (MaterialCardView) LayoutInflater.from(getContext()).inflate(R.layout.dynamic_timetable_card, null);
        TextView contentTV = card.findViewById(R.id.dynamic_tt_card_content);
        contentTV.setText(event.getText());
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timetableSocket.onCardClick(event.getEvent());
            }
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dayWidth - 2 * CARD_MARGIN, hourHeight - 2 * CARD_MARGIN);
        params.leftMargin = labelOffset + dayOfWeek * dayWidth + CARD_MARGIN;
        params.topMargin = (int) (1.0 * deltaMilli * dayHeight / MILLISECONDS_PER_DAY) + CARD_MARGIN;

        FL.addView(card, params);
    }

    private void setCardSize() {
        hourHeight = (int) getResources().getDimension(R.dimen.tt_time_spacing);
        dayHeight = hourHeight * 24;
        labelOffset = (int) getResources().getDimension(R.dimen.timetable_label_width);
    }
}

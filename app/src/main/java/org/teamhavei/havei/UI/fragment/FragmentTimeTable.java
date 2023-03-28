package org.teamhavei.havei.UI.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.event.HaveIEvent;
import org.teamhavei.havei.R;
import org.teamhavei.havei.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentTimeTable extends BaseFragment {

    private static final long MILLISECONDS_PER_DAY = 86400000;
    private static final int CARD_MARGIN = 5;

    NestedScrollView scrollView;
    TextView monthTV;
    FrameLayout FL;

    Calendar startOfWeek = Calendar.getInstance();
    List<TextView> dateTVList = new ArrayList<>();
    LinearLayout countTVContainer;
    List<TextView> countTVList = new ArrayList<>();
    List<TimeTableEvent> eventList = new ArrayList<>();
    List<MaterialCardView> cardList = new ArrayList<>();

    Integer[] count = new Integer[7];
    int dayWidth;
    int hourHeight;
    int dayHeight;
    int labelOffset;
    boolean isTodoMode;

    private final TimetableSocket timetableSocket;

    public interface TimetableSocket {
        void onCardClick(HaveIEvent event);
        void updateEventList(Calendar startOfWeek, List<TimeTableEvent> eventList);
        void onScrollBehavior(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    public FragmentTimeTable(Calendar startOfWeek, TimetableSocket socket, boolean isTodoMode) {
        this.startOfWeek.set(Calendar.YEAR, startOfWeek.get(Calendar.YEAR));
        this.startOfWeek.set(Calendar.MONTH, startOfWeek.get(Calendar.MONTH));
        this.startOfWeek.set(Calendar.DAY_OF_YEAR, startOfWeek.get(Calendar.DAY_OF_YEAR));
        this.timetableSocket = socket;
        this.isTodoMode = isTodoMode;
    }

    public Calendar getStartOfWeek() {
        return startOfWeek;
    }

    public static class TimeTableEvent {
        private final Calendar startTime;
        private final String text;
        private final HaveIEvent event;

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
        Log.d(TAG, "onCreateView: " + Util.eventDateFormatter(startOfWeek.getTime()));
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

        if (isTodoMode) {
            (view.findViewById(R.id.tt_timeline)).setVisibility(View.VISIBLE);
            (view.findViewById(R.id.tt_bg)).setVisibility(View.GONE);
            new Handler().postDelayed(configCurrentTime, 200);
        } else {
            (view.findViewById(R.id.tt_timeline)).setVisibility(View.GONE);
            (view.findViewById(R.id.tt_bg)).setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.tt_tv_day1).getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        view.findViewById(R.id.tt_tv_day1).getViewTreeObserver().removeOnPreDrawListener(this);
                        dayWidth = view.findViewById(R.id.tt_tv_day1).getWidth(); // 获取宽度
                        if (eventList != null) {
                            dayWidth = view.findViewById(R.id.tt_tv_day1).getWidth();
                            for (MaterialCardView card : cardList) {
                                FL.removeView(card);
                            }
                            for (int i = 0; i < 7; i++) {
                                count[i] = 0;
                                countTVList.get(i).setVisibility(View.GONE);
                            }
                            cardList.clear();
                            countTVContainer.setVisibility(View.GONE);
                            for (TimeTableEvent event : eventList) {
                                countTVContainer.setVisibility(View.VISIBLE);
                                configCard(event);
                            }
                        }
                        return true;
                    }
                });
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                timetableSocket.onScrollBehavior(scrollX,scrollY,oldScrollX,oldScrollY);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        timetableSocket.updateEventList(startOfWeek, eventList);
        if (eventList != null) {
            for (MaterialCardView card : cardList) {
                FL.removeView(card);
            }
            for (int i = 0; i < 7; i++) {
                count[i] = 0;
                countTVList.get(i).setVisibility(View.GONE);
            }
            cardList.clear();
            countTVContainer.setVisibility(View.GONE);
            for (TimeTableEvent event : eventList) {
                countTVContainer.setVisibility(View.VISIBLE);
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
        countTVContainer = view.findViewById(R.id.tt_count_layout);
        countTVList.clear();
        countTVList.add(view.findViewById(R.id.tt_count_text_1));
        countTVList.add(view.findViewById(R.id.tt_count_text_2));
        countTVList.add(view.findViewById(R.id.tt_count_text_3));
        countTVList.add(view.findViewById(R.id.tt_count_text_4));
        countTVList.add(view.findViewById(R.id.tt_count_text_5));
        countTVList.add(view.findViewById(R.id.tt_count_text_6));
        countTVList.add(view.findViewById(R.id.tt_count_text_7));
        FL = view.findViewById(R.id.tt_FL);
        scrollView = view.findViewById(R.id.tt_NSV);
    }

    private void configCard(TimeTableEvent event) {
        int dayOfWeek = event.getStartTime().get(Calendar.DAY_OF_WEEK);
        dayOfWeek = (dayOfWeek - 1 + 7) % 7;/*0~6, 周日为第一(0)天*/
        countTVList.get(dayOfWeek).setVisibility(View.VISIBLE);
        countTVList.get(dayOfWeek).setText(Integer.toString(++count[dayOfWeek]));

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
        cardList.add(card);

        FL.addView(card, params);
    }

    private void setCardSize() {
        hourHeight = (int) getResources().getDimension(R.dimen.tt_time_spacing);
        dayHeight = hourHeight * 24;
        labelOffset = (int) getResources().getDimension(R.dimen.timetable_label_width);
    }

    private final Runnable configCurrentTime = new Runnable() {
        @Override
        public void run() {
            Calendar startOfDay = Calendar.getInstance();
            startOfDay.set(Calendar.HOUR_OF_DAY, 0);
            startOfDay.set(Calendar.MINUTE, 0);
            startOfDay.set(Calendar.SECOND, 0);
            long deltaMilli = Calendar.getInstance().getTimeInMillis() - startOfDay.getTimeInMillis();
            int scrollToY = (int) (1.0 * deltaMilli * dayHeight / MILLISECONDS_PER_DAY) - scrollView.getHeight() / 2;
            scrollToY = Math.max(scrollToY, 0);
            scrollView.smoothScrollTo(0, scrollToY);
            View view = new View(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getContext().getResources().getDimension(R.dimen.dividing_line_view_height)*2);
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_blue));
            params.topMargin = (int) (1.0 * deltaMilli * dayHeight / MILLISECONDS_PER_DAY);
            FL.addView(view, params);
        }
    };
}

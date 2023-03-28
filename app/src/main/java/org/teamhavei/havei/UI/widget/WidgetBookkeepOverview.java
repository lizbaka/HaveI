package org.teamhavei.havei.UI.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import org.teamhavei.havei.R;
import org.teamhavei.havei.util.Util;
import org.teamhavei.havei.UI.activity.ActivityBookkeep;
import org.teamhavei.havei.UI.activity.ActivityBookkeepAdd;
import org.teamhavei.havei.database.BookkeepDBHelper;

import java.util.Date;


public class WidgetBookkeepOverview extends AppWidgetProvider {

    public static final String INTENT_ACTION_NAME = "org.teamhavei.havei.action.WIDGET_UPDATE";

    BookkeepDBHelper dbHelper;
    SharedPreferences pref;
    double expenditureToday;
    double expenditureMonth;
    double remainBudget;

    public static void updateWidgetAction(Context context){
        Intent intent = new Intent(INTENT_ACTION_NAME);
        intent.setComponent(new ComponentName(context,WidgetBookkeepOverview.class));
        context.sendBroadcast(intent);
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_bookkeep_overview);

        Intent intentAdd = new Intent(context, ActivityBookkeepAdd.class);
        PendingIntent PIAdd = PendingIntent.getActivity(context, 0, intentAdd, 0);
        Intent intentBookkeep = new Intent(context, ActivityBookkeep.class);
        PendingIntent PIBookkeep = PendingIntent.getActivity(context, 0, intentBookkeep, 0);
        views.setOnClickPendingIntent(R.id.widget_bookkeep_overview_add, PIAdd);
        views.setOnClickPendingIntent(R.id.bookkeep_three_container, PIBookkeep);

        views.setTextViewText(R.id.bookkeep_three_value1, String.format("%.2f", expenditureToday));
        views.setTextViewText(R.id.bookkeep_three_value2, String.format("%.2f", expenditureMonth));
        if (remainBudget == 0) {
            views.setTextViewText(R.id.bookkeep_three_value3, context.getString(R.string.unset));
        } else {
            views.setTextViewText(R.id.bookkeep_three_value3, String.format("%.2f", remainBudget - expenditureMonth));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        pref = context.getSharedPreferences(Util.PREF_SETTINGS, Context.MODE_PRIVATE);
        dbHelper = new BookkeepDBHelper(context, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        expenditureToday = dbHelper.getExpenditureByDay(new Date());
        expenditureMonth = dbHelper.getExpenditureByMonth(new Date());
        remainBudget = pref.getFloat(Util.PREF_BUDGET, 0);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(INTENT_ACTION_NAME)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,WidgetBookkeepOverview.class);
            onUpdate(context, manager,manager.getAppWidgetIds(componentName));
        }
    }
}
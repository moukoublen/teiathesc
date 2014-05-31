package org.teiath.teiesc.widget;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.teiath.teiesc.DataPack;
import org.teiath.teiesc.DataToDataPack;
import org.teiath.teiesc.LoginActivity;
import org.teiath.teiesc.R;
import org.teiath.teiesc.dataitems.LectureLessonPack;
import org.teiath.teiesc.dataitems.TimeUtil;
import org.teiath.teiesc.options.ViewOptions;
import org.teiath.teiesc.provider.dbmetadata.FullTablesPack;
import org.teiath.teiesc.provider.dbtransaction.FullDbTranPack;
import org.teiath.teiesc.utils.Selector;
import org.teiath.teiesc.utils.Selector.Condition;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ScheduleWidgetProvider extends AppWidgetProvider
{
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        DataPack       mItmsPack = new DataPack();
        FullTablesPack mTblsPack = new FullTablesPack();
        FullDbTranPack mTranPack = new FullDbTranPack(mTblsPack);
        
        DataToDataPack.fillAll(mItmsPack, mTranPack, context);
        
        List<LectureLessonPack> lst = getCurrentDay(mItmsPack);
        
        Collections.sort(lst);
        
        long cr_stamp = getCurStamp();
        
        List<LectureLessonPack> current  = getCurrent(cr_stamp, lst);
        
        List<LectureLessonPack> upcoming = getUpcoming(cr_stamp, lst);
        
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++)
        {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, LoginActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

            // To update a label
            //views.setTextViewText(R.id.widget_city_name, formattedDate );
            
            views.setTextViewText(R.id.wd_tx_current_title, formatLectures(context, current));
            views.setTextViewText(R.id.wd_tx_upcoming_title, formatLectures(context, upcoming));

            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
    
    private static List<LectureLessonPack> getCurrentDay(DataPack itemsPack)
    {
        return itemsPack.getLectureLessonPack(new ViewOptions(), 
                Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
    }
    
    @SuppressLint("DefaultLocale")
    private static long getCurStamp()
    {
        Calendar c = Calendar.getInstance();
        return TimeUtil.toUTCTime(String.format(Locale.getDefault() ,"%02d:%02d:00", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)) );
    }
    
    private List<LectureLessonPack> getCurrent(final long cur_time, List<LectureLessonPack> dayItems)
    {
        return Selector.where(dayItems, 
                new Condition<LectureLessonPack>()
        {
            @Override
            public boolean isAccepted(LectureLessonPack item)
            {
                long c = cur_time;
                return item.getLecture().getTimeBegin() <= c &&
                        item.getLecture().getTimeBegin() + item.getLecture().getDur() > c;
            }
        });
    }
    private List<LectureLessonPack> getUpcoming(final long cur_time, List<LectureLessonPack> dayItems)
    {
        return Selector.where(dayItems, 
                new Condition<LectureLessonPack>()
        {
            @Override
            public boolean isAccepted(LectureLessonPack item)
            {
                long c = cur_time;
                return item.getLecture().getTimeBegin() > c;
            }
        });
    }
    
    private String formatLectures(Context c, List<LectureLessonPack> lst)
    {
        if(lst.size() == 0)
        {
            return c.getString(R.string.widget_current_emtpy);
        }
        StringBuilder b = new StringBuilder();
        
        boolean first = true;
        for(LectureLessonPack p : lst)
        {
            if(first) first = false;
            else b.append("\n");
            
            b.append("• ");
            b.append(p.getLesson().getTitle());
            b.append(" [");
            b.append( TimeUtil.formatUTCTime(p.getLecture().getTimeBegin()) );
            b.append(" - ");
            b.append( TimeUtil.formatUTCTime(p.getLecture().getTimeBegin() + p.getLecture().getDur()) );
            b.append("]");
        }
        
        return b.toString();
    }
}

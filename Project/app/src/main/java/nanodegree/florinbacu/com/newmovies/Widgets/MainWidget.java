package nanodegree.florinbacu.com.newmovies.Widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.List;

import nanodegree.florinbacu.com.newmovies.Loaders.ContentLoader;
import nanodegree.florinbacu.com.newmovies.R;

/**
 * Implementation of App Widget functionality.
 */
public class MainWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget);
        views.removeAllViews(R.id.content_widget_items);
        RemoteViews itemView;
        int i;
        if(list!=null)
        {
        for(i=0;i<Math.min(5,list.size());i++) {
            itemView=new RemoteViews(context.getPackageName(),R.layout.item_list_content);
            itemView.setTextViewText(R.id.content,list.get(i).title);
            views.addView(R.id.content_widget_items, itemView);
        }
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    static List<ContentLoader.ItemRSS> list;
 public static void feedList(List<ContentLoader.ItemRSS> list)
    {
        if(list.size()>0) {
            MainWidget.list = list.subList(0, Math.min(4, list.size()));
        }
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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
}


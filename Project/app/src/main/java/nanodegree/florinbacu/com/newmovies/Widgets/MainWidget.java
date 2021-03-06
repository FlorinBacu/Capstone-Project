package nanodegree.florinbacu.com.newmovies.Widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.io.Serializable;
import java.util.List;

import nanodegree.florinbacu.com.newmovies.ItemListActivity;
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
        Intent intent = new Intent(context, RemoteWidgetMainService.class);
        intent.putExtra("list",(Serializable)list);
        views.setRemoteAdapter(R.id.content_widget_items, intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.content_widget_items);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static List<ContentLoader.ItemRSS> list;

    public static void feedList(List<ContentLoader.ItemRSS> list) {
        if (list.size() > 0) {

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


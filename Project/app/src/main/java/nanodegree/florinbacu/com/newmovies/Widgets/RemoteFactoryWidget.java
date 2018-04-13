package nanodegree.florinbacu.com.newmovies.Widgets;

import android.content.Context;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import nanodegree.florinbacu.com.newmovies.ItemListActivity;
import nanodegree.florinbacu.com.newmovies.Loaders.ContentLoader;
import nanodegree.florinbacu.com.newmovies.R;

/**
 * Created by Florin on 4/13/2018.
 */

public class RemoteFactoryWidget implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private final List<ContentLoader.ItemRSS> list;
    RemoteFactoryWidget(Context context, List<ContentLoader.ItemRSS> list)

    {
        this.list=list;
        this.context=context;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget);

        RemoteViews itemView;
        int i;
        for (i = 0; i < Math.min(5, list.size()); i++) {
            itemView = new RemoteViews(context.getPackageName(), R.layout.item_list_content_widget);
            itemView.setTextViewText(R.id.content, list.get(i).title.split("-")[1]);
            itemView.setImageViewUri(R.id.imageList, Uri.parse(ItemListActivity.aquaired_link.get(list.get(i).link)));
            views.addView(R.id.content_widget_items, itemView);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(list==null) return 0;
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
       ContentLoader.ItemRSS itemRSS=list.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_list_content_widget);
        views.setTextViewText(R.id.content,itemRSS.title.split("-")[1]);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

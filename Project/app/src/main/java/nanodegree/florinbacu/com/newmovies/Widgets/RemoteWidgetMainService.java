package nanodegree.florinbacu.com.newmovies.Widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

import java.util.List;

import nanodegree.florinbacu.com.newmovies.Loaders.ContentLoader;

/**
 * Created by Florin on 4/13/2018.
 */

public class RemoteWidgetMainService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteFactoryWidget(this.getApplicationContext(),(List<ContentLoader.ItemRSS>) intent.getSerializableExtra("list"));
    }
}

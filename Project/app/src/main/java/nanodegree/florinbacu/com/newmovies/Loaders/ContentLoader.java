package nanodegree.florinbacu.com.newmovies.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Xml;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import nanodegree.florinbacu.com.newmovies.ItemListActivity;
import nanodegree.florinbacu.com.newmovies.R;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ContentLoader extends AsyncTaskLoader<List<ContentLoader.ItemRSS>> {

    /**
     * An array of sample (dummy) items.
     */

    public static List<ItemRSS> ITEMS = new ArrayList<ItemRSS>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ItemRSS> ITEM_MAP = new HashMap<String, ItemRSS>();

    public static  int COUNT;


    Context context;
    public ContentLoader(Context context) {
        super(context);
        this.context=context;
    }

    private static void addItem(ItemRSS item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ItemRSS createDummyItem(int position) {
        return new ItemRSS(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
ArrayList<ItemRSS> listLoaded=new ArrayList<ItemRSS>();

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ItemRSS> loadInBackground() {
        String rss = null;
        ItemRSS item;
        URL url= null;

        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://www.moma.org/feeds/events_films_30_days.rss");
            urlConnection = (HttpURLConnection)url.openConnection() ;
            InputStream in = urlConnection.getInputStream();
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser pullParser = factory.newPullParser();
            pullParser.setInput(in,null);
            int eventType = pullParser.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT)
            {
                if(pullParser.getName()!=null && pullParser.getName().equals("item"))
                {

                    item=processItem(pullParser);
                    listLoaded.add(item);
                }
                eventType=pullParser.next();
            }


        } catch (IOException e) {
            urlConnection.disconnect();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return listLoaded;

    }

    @Override
    public void deliverResult(List<ItemRSS> items)
    {
        super.deliverResult(items);
        ContentLoader.ITEMS=items;

    }
    private ItemRSS processItem(XmlPullParser pullParser) throws XmlPullParserException, IOException {


        int eventType=pullParser.getEventType();
        String name;
        String title = null;
        String description=null;
        ItemRSS item = null;

        while(pullParser.getName()!=null && !pullParser.getName().equals("item") || eventType!=XmlPullParser.END_TAG)
        {
            name=pullParser.getName();
            if(name==null) {
                eventType = pullParser.next();
                continue;
            }
            switch (name)
            {
                case "title":
                    if(title==null) {
                        while (eventType != XmlPullParser.CDSECT) {
                            eventType = pullParser.nextToken();
                        }
                        title = pullParser.getText();
                    }
                    break;
                case "description":
                    if(description==null) {
                        while (eventType != XmlPullParser.CDSECT) {
                            eventType = pullParser.nextToken();
                        }
                        description = pullParser.getText();
                    }
                    break;
            }
            eventType=pullParser.next();
        }
        item=new ItemRSS(String.valueOf(listLoaded.size()),title,description);

        return  item;
    }



    /**
     * A dummy item representing a piece of content.
     */
    public static class ItemRSS {
        public final String id;
        public final String content;
        public final String details;

        public ItemRSS(String id, String title, String description) {
            this.id = id;
            this.content = title;
            this.details = description;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}

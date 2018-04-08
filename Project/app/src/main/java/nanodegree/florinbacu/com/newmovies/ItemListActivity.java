package nanodegree.florinbacu.com.newmovies;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import nanodegree.florinbacu.com.newmovies.Database.MovieContract;
import nanodegree.florinbacu.com.newmovies.Loaders.ContentLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ContentLoader.ItemRSS>>{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static boolean first_load=false;
    private boolean mTwoPane;
    AdView mAdView;
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ContentResolver resolver = getContentResolver();
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resolver.delete(MovieContract.CONTENT_URI,null,null);
                int i;
                int count=ContentLoader.ITEMS.size();
                ContentValues []cvs=new ContentValues[count];
                ContentValues cv;
                for(i=0;i<count;i++)
                {
                    cv=new ContentValues();

                    cv.put(MovieContract.MovieEntry.COLUMN_ID,ContentLoader.ITEMS.get(i).id);
                    cv.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE,ContentLoader.ITEMS.get(i).title);
                    cv.put(MovieContract.MovieEntry.COLUMN_NAME_DETAIL,ContentLoader.ITEMS.get(i).details);
                    cvs[i]=cv;
                }
                resolver.bulkInsert(MovieContract.CONTENT_URI,cvs);
                Snackbar.make(view, "data saved in database", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    if(isOnline()) {
        getLoaderManager().initLoader(0, null, this);
    }
    else
    {
        Toast.makeText(this,"No internet available",Toast.LENGTH_LONG).show();


        new AsyncTask<Cursor, Void,List<ContentLoader.ItemRSS>>() {


            private Context context;

            public AsyncTask setContext(Context context)
            {
                this.context=context;
                return this;
            }
            @Override
            protected List<ContentLoader.ItemRSS> doInBackground(Cursor ...cursors) {


                ArrayList<ContentLoader.ItemRSS> list=new ArrayList<ContentLoader.ItemRSS>();
                    if(!first_load)
                    {

                        first_load=true;
                        Cursor cursor = resolver.query(MovieContract.CONTENT_URI, null, null, null, null);

                    int index_title=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_TITLE);
                    int index_detail=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_DETAIL);
                    int index_id=cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
                    String row_title,row_detail,row_id;

                        while(cursor.moveToNext()) {
                            row_detail=cursor.getColumnName(index_detail);
                            row_title=cursor.getColumnName(index_title);
                            row_id=cursor.getColumnName(index_id);
                            list.add(new ContentLoader.ItemRSS(row_id,row_title,row_detail));
                            cursor.moveToNext();
                        }

                        return list;
                    }
                else
                    {
                       return ContentLoader.ITEMS;
                    }




            }

            @Override
            protected void onPostExecute(List<ContentLoader.ItemRSS> listout) {

                if(listout.size()==0) {
                    Toast.makeText(context, "database is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(context,"data loaded from database",Toast.LENGTH_LONG).show();
                super.onPostExecute(listout);
                ContentLoader.ITEMS=listout;
                ContentLoader.COUNT=listout.size();
                View recyclerView =findViewById(R.id.item_list);
                assert recyclerView != null;
                setupRecyclerView((RecyclerView) recyclerView);
            }
        }.setContext(this).execute(new Cursor[]{null});

    }
        MobileAds.initialize(this,    "ca-app-pub-3940256099942544~3347511713");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ContentLoader.ITEMS, mTwoPane));
    }


    @Override
    public Loader<List<ContentLoader.ItemRSS>> onCreateLoader(int id, Bundle args) {
        return new ContentLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<ContentLoader.ItemRSS>> loader, List<ContentLoader.ItemRSS> data) {
        ContentLoader.COUNT=ContentLoader.ITEMS.size();
        View recyclerView =findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }




    @Override
    public void onLoaderReset(Loader<List<ContentLoader.ItemRSS>> loader) {

    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<ContentLoader.ItemRSS> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentLoader.ItemRSS item = (ContentLoader.ItemRSS) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<ContentLoader.ItemRSS> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mContentView.setText(mValues.get(position).title.split("-")[1]);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mContentView;

            ViewHolder(View view) {
                super(view);

                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}

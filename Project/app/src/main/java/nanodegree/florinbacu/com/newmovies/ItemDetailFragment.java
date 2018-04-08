package nanodegree.florinbacu.com.newmovies;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nanodegree.florinbacu.com.newmovies.Loaders.ContentLoader;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ContentLoader.ItemRSS mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            mItem=ContentLoader.ITEMS.get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title.split("-")[1]);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        mItem=ContentLoader.ITEMS.get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView)rootView.findViewById(R.id.date_detail)).setText(mItem.title.split("-")[0]);
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(Html.fromHtml(mItem.details));
        }
        FloatingActionButton fab = (FloatingActionButton)  rootView.findViewById(R.id.fabf);
        if(fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.setType("text/html");
                    String shareBody = mItem.details;
                    String shareSubject = mItem.title;
                    intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                    intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(intent, "Share using"));
                }
            });
        }
        return rootView;
    }
}

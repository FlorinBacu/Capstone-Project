package nanodegree.florinbacu.com.newmovies.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieContract {
    public static final String CONTENT_AUTHORITY = "nanodegree.florinbacu.com.newmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH = "Movies";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

    private MovieContract() {

    }

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DETAIL = "detail";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_ID = "id_movie";
    }
}

package nanodegree.florinbacu.com.newmovies.Database;

import android.provider.BaseColumns;

public final class MovieContract {
    private MovieContract()
    {

    }
    public static class MovieEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DETAIL = "detail";
        public static final String COLUMN_ID="id_movie";
    }
}

package nanodegree.florinbacu.com.newmovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MOMA";
    private static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME +
                "(" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_ID + " INTEGER NOT NULL," +
                MovieContract.MovieEntry.COLUMN_NAME_DETAIL + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUMN_NAME_LINK + " TEXT NOT NULL);";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}

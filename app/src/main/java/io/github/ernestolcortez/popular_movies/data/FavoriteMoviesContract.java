package io.github.ernestolcortez.popular_movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class FavoriteMoviesContract {

    public static final String AUTHORITY = "io.github.ernestolcortez.popular_movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "MOVIES";


    public static final class Movies implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final int COL_INDEX_MOVIE_ID = 1;


    }

}

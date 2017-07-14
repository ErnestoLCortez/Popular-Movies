package io.github.ernestolcortez.popular_movies.data;

import android.provider.BaseColumns;

public final class FavoriteMoviesContract {

    public static final class Movies implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final int COL_INDEX_MOVIE_ID = 1;


    }

}

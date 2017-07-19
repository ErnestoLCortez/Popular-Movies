package io.github.ernestolcortez.popular_movies.data;

import android.database.Cursor;

public interface FetchMovieFromDBListener {
    void onTaskComplete(Cursor cursor);
}

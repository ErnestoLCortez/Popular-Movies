package io.github.ernestolcortez.popular_movies;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import io.github.ernestolcortez.popular_movies.data.FetchMovieFromDBListener;
import static io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract.Movies.CONTENT_URI;


public class FetchMovieFromDBTask extends AsyncTask<String, Void, Cursor> {

    private FetchMovieFromDBListener mListener;
    private ContentResolver contentResolver;

    public FetchMovieFromDBTask(FetchMovieFromDBListener listener, Context context) {
        mListener = listener;
        contentResolver = context.getContentResolver();
    }


    @Override
    protected Cursor doInBackground(String... strings) {
        try {
            return contentResolver.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        mListener.onTaskComplete(cursor);
    }
}

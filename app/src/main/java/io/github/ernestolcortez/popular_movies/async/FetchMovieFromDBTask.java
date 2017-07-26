package io.github.ernestolcortez.popular_movies.async;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import io.github.ernestolcortez.popular_movies.data.FetchMovieFromDBListener;


public class FetchMovieFromDBTask extends AsyncTask<Uri, Void, Cursor> {

    private FetchMovieFromDBListener mListener;
    private ContentResolver contentResolver;

    public FetchMovieFromDBTask(FetchMovieFromDBListener listener, Context context) {
        mListener = listener;
        contentResolver = context.getContentResolver();
    }


    @Override
    protected Cursor doInBackground(Uri... uri) {

        try {
            return contentResolver.query(
                    uri[0],
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

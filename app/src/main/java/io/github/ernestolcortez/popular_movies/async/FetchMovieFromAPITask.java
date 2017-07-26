package io.github.ernestolcortez.popular_movies.async;

import android.database.Cursor;
import android.os.AsyncTask;
import java.net.URL;

import io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract;
import io.github.ernestolcortez.popular_movies.utilities.AsyncTaskListener;
import io.github.ernestolcortez.popular_movies.utilities.MovieJsonUtils;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;
import io.github.ernestolcortez.popular_movies.utilities.NetworkUtils;

public class FetchMovieFromAPITask extends AsyncTask<Object, Void, MovieObject[]> {


    private AsyncTaskListener mListener;

    public FetchMovieFromAPITask(AsyncTaskListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onPreExecute();
    }

    @Override
    protected MovieObject[] doInBackground(Object... params) {

        if (params.length == 0)
            return null;

        String sortQuery = (String)params[0];

        if(sortQuery.equals("favorites")){
            Cursor cursor = (Cursor)params[1];
            return fetchMoviesById(cursor);
        }

        URL movieRequestUrl = NetworkUtils.buildMovieListUrl(sortQuery);

        try {
            String jsonResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);

            return MovieJsonUtils
                    .getMovieObjectsFromJson(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private MovieObject[] fetchMoviesById(Cursor cursor) {
        MovieObject[] movieObjects = new MovieObject[cursor.getCount()];

        try {
            for (int index = 0; cursor.moveToNext(); ++index){
                URL movieRequestUrl = NetworkUtils.buildSingleMovieUrl(cursor.getInt(FavoriteMoviesContract.Movies.COL_INDEX_MOVIE_ID));
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                movieObjects[index] = MovieJsonUtils
                        .getMovieObjectFromJson(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return movieObjects;
    }

    @Override
    protected void onPostExecute(MovieObject[] movieObjects) {
        super.onPostExecute(movieObjects);
        mListener.onTaskComplete(movieObjects);
    }
}
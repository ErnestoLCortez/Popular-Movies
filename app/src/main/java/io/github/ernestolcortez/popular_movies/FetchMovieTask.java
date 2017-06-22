package io.github.ernestolcortez.popular_movies;

import android.os.AsyncTask;
import java.net.URL;

import io.github.ernestolcortez.popular_movies.utilities.AsyncTaskListener;
import io.github.ernestolcortez.popular_movies.utilities.MovieJsonUtils;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;
import io.github.ernestolcortez.popular_movies.utilities.NetworkUtils;

public class FetchMovieTask extends AsyncTask<String, Void, MovieObject[]> {


    private AsyncTaskListener mListener;

    public FetchMovieTask(AsyncTaskListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onPreExecute();
    }

    @Override
    protected MovieObject[] doInBackground(String... params) {

        if (params.length == 0)
            return null;

        String sortQuery = params[0];
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

    @Override
    protected void onPostExecute(MovieObject[] movieObjects) {
        super.onPostExecute(movieObjects);
        mListener.onTaskComplete(movieObjects);
    }
}
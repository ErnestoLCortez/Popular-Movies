package io.github.ernestolcortez.popular_movies.async;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import io.github.ernestolcortez.popular_movies.utilities.FetchMovieReviewsListener;
import io.github.ernestolcortez.popular_movies.utilities.MovieJsonUtils;
import io.github.ernestolcortez.popular_movies.utilities.NetworkUtils;
import io.github.ernestolcortez.popular_movies.utilities.ReviewObject;

public class FetchMovieReviewsFromAPITask extends AsyncTask<Integer, Void, ReviewObject[]> {

    private FetchMovieReviewsListener mListener;

    public FetchMovieReviewsFromAPITask(FetchMovieReviewsListener listener) { mListener = listener;}

    @Override
    protected void onPostExecute(ReviewObject[] reviews) {
        super.onPostExecute(reviews);
        mListener.onTaskComplete(reviews);
    }

    @Override
    protected ReviewObject[] doInBackground(Integer... params) {
        if (params.length == 0)
            return null;

        int key = params[0];

        URL reviewsUrl = NetworkUtils.buildMovieReviewsUrl(key);
        Log.d("REVIEW URL BUILT: ", reviewsUrl.toString());

        try {
            String jsonResponse = NetworkUtils
                    .getResponseFromHttpUrl(reviewsUrl);

            Log.d("JSON RESPONSE: ", jsonResponse);

            return MovieJsonUtils
                    .getReviewObjectsFromJson(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

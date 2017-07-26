package io.github.ernestolcortez.popular_movies.async;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;

import io.github.ernestolcortez.popular_movies.utilities.FetchRelatedVideosListener;
import io.github.ernestolcortez.popular_movies.utilities.MovieJsonUtils;
import io.github.ernestolcortez.popular_movies.utilities.NetworkUtils;
import io.github.ernestolcortez.popular_movies.utilities.RelatedVideo;

public class FetchRelatedVideosFromAPITask extends AsyncTask<Integer, Void, ArrayList<RelatedVideo>> {

    private FetchRelatedVideosListener mListener;

    public FetchRelatedVideosFromAPITask(FetchRelatedVideosListener listener) { mListener = listener; }

    @Override
    protected void onPostExecute(ArrayList<RelatedVideo> relatedVideos) {
        mListener.onTaskComplete(relatedVideos);
    }

    @Override
    protected ArrayList<RelatedVideo> doInBackground(Integer... params) {
        if (params.length == 0)
            return null;

        int key = params[0];

        URL videoUrl = NetworkUtils.buildRelatedVideosUrl(key);
        Log.d("VIDEO URL BUILT: ", videoUrl.toString());

        try {
            String jsonResponse = NetworkUtils
                    .getResponseFromHttpUrl(videoUrl);

            Log.d("JSON RESPONSE: ", jsonResponse);

            return MovieJsonUtils
                    .getTrailerVideosFromJson(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

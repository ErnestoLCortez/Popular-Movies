package io.github.ernestolcortez.popular_movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by louie on 6/20/17.
 */

public final class MovieJsonUtils {

    public static MovieObject[] getMovieObjectsFromJson(String movieJsonStr) throws JSONException{

        final String MOVIE_LIST = "results";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_POSTER = "poster_path";
        final String MOVIE_VOTE_AVG = "vote_average";
        final String MOVIE_OVERVIEW = "overview";
        MovieObject[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);

        parsedMovieData = new MovieObject[movieArray.length()];

        for(int i = 0; i < movieArray.length(); ++i){

            JSONObject movie = movieArray.getJSONObject(i);

            String title = movie.getString(MOVIE_TITLE);
            String releaseDate = movie.getString(MOVIE_RELEASE_DATE);
            String moviePosterPath = movie.getString(MOVIE_POSTER);
            double voteAverage = movie.getDouble(MOVIE_VOTE_AVG);
            String plotSynopsis = movie.getString(MOVIE_OVERVIEW);

            parsedMovieData[i] = new MovieObject(
                    title,
                    releaseDate,
                    moviePosterPath,
                    voteAverage,
                    plotSynopsis
            );
        }

        return parsedMovieData;
    }
}

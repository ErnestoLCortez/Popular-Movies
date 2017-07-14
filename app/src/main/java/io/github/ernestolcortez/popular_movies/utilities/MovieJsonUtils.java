package io.github.ernestolcortez.popular_movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class MovieJsonUtils {

    private static final String MOVIE_LIST = "results";
    private static final String MOVIE_TITLE = "original_title";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String MOVIE_VOTE_AVG = "vote_average";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_ID = "id";

    public static MovieObject[] getMovieObjectsFromJson(String movieJsonStr) throws JSONException{


        MovieObject[] parsedMovieData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);

        parsedMovieData = new MovieObject[movieArray.length()];

        for(int i = 0; i < movieArray.length(); ++i){

            JSONObject movie = movieArray.getJSONObject(i);

            parsedMovieData[i] = getMovieObjectFromJson(movie);
        }

        return parsedMovieData;
    }

    public static MovieObject getMovieObjectFromJson(String movieJsonStr) throws JSONException{
        JSONObject movieJson = new JSONObject(movieJsonStr);
        return getMovieObjectFromJson(movieJson);
    }

    private static MovieObject getMovieObjectFromJson(JSONObject movie) throws JSONException{

        String title = movie.getString(MOVIE_TITLE);
        String releaseDate = movie.getString(MOVIE_RELEASE_DATE);
        String moviePosterPath = NetworkUtils.buildMovieImageURL(movie.getString(MOVIE_POSTER)).toString();
        double voteAverage = movie.getDouble(MOVIE_VOTE_AVG);
        String plotSynopsis = movie.getString(MOVIE_OVERVIEW);
        int movieId = movie.getInt(MOVIE_ID);

        return new MovieObject(
                title,
                releaseDate,
                moviePosterPath,
                voteAverage,
                plotSynopsis,
                movieId
        );
    }

}

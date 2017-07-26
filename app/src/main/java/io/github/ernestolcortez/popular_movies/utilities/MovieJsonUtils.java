package io.github.ernestolcortez.popular_movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class MovieJsonUtils {

    private static final String RESULTS = "results";
    private static final String MOVIE_TITLE = "original_title";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_POSTER = "poster_path";
    private static final String MOVIE_VOTE_AVG = "vote_average";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_ID = "id";
    private static final String MOVIE_BACKDROP = "backdrop_path";

    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_URL = "url";

    private static final String VIDEO_KEY = "key";
    private static final String VIDEO_NAME = "name";
    private static final String VIDEO_SITE = "site";
    private static final String VIDEO_TYPE = "type";
    private static final String VIDEO_SITE_VALUE = "YouTube";
    private static final String VIDEO_TYPE_VALUE = "Trailer";

    public static MovieObject[] getMovieObjectsFromJson(String movieJsonStr) throws JSONException{


        MovieObject[] parsedMovieData;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(RESULTS);

        parsedMovieData = new MovieObject[movieArray.length()];

        for(int i = 0; i < movieArray.length(); ++i){

            JSONObject movie = movieArray.getJSONObject(i);

            parsedMovieData[i] = getMovieObjectFromJson(movie);
        }

        return parsedMovieData;
    }

    public static MovieObject getMovieObjectFromJson(String movieJsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(movieJsonStr);
        return getMovieObjectFromJson(movieJson);
    }

    private static MovieObject getMovieObjectFromJson(JSONObject movie) throws JSONException {

        String title = movie.getString(MOVIE_TITLE);
        String releaseDate = movie.getString(MOVIE_RELEASE_DATE);
        String moviePosterPath = NetworkUtils.buildMovieImageURL(movie.getString(MOVIE_POSTER)).toString();
        double voteAverage = movie.getDouble(MOVIE_VOTE_AVG);
        String plotSynopsis = movie.getString(MOVIE_OVERVIEW);
        int movieId = movie.getInt(MOVIE_ID);
        String backDrop = NetworkUtils.buildMovieImageURL(movie.getString(MOVIE_BACKDROP)).toString();

        return new MovieObject(
                title,
                releaseDate,
                moviePosterPath,
                voteAverage,
                plotSynopsis,
                movieId,
                backDrop
        );
    }

    public static ReviewObject[] getReviewObjectsFromJson(String reviewJsonStr) throws JSONException {

        ReviewObject[] parsedReviewData;
        JSONObject reviewJson = new JSONObject(reviewJsonStr);
        JSONArray resultArray = reviewJson.getJSONArray(RESULTS);
        parsedReviewData = new ReviewObject[resultArray.length()];

        for(int i = 0; i < resultArray.length(); ++i){

            JSONObject review = resultArray.getJSONObject(i);

            parsedReviewData[i] = new ReviewObject(
                    review.getString(REVIEW_AUTHOR),
                    review.getString(REVIEW_CONTENT),
                    review.getString(REVIEW_URL)
            );
        }

        return parsedReviewData;
    }

    public static ArrayList<RelatedVideo> getTrailerVideosFromJson(String videoJsonStr) throws JSONException {
        ArrayList<RelatedVideo> parsedVideoData;
        JSONObject videoJson = new JSONObject(videoJsonStr);
        JSONArray resultArray = videoJson.getJSONArray(RESULTS);
        parsedVideoData = new ArrayList<>(resultArray.length());

        for(int i = 0; i < resultArray.length(); ++i){

            JSONObject video = resultArray.getJSONObject(i);

            if(isYouTubeTrailer(video)) {
                parsedVideoData.add(
                        new RelatedVideo(
                            video.getString(VIDEO_KEY),
                            video.getString(VIDEO_NAME)
                        )
                );

            }
        }
        return parsedVideoData;
    }

    private static boolean isYouTubeTrailer(JSONObject video) {

        boolean isTrailer;
        boolean isYouTube;

        try {
            isTrailer = video.getString(VIDEO_TYPE).equals(VIDEO_TYPE_VALUE);
            isYouTube = video.getString(VIDEO_SITE).equals(VIDEO_SITE_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return ( isTrailer && isYouTube );
    }


}

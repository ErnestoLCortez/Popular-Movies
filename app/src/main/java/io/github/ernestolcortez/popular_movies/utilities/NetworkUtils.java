package io.github.ernestolcortez.popular_movies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import io.github.ernestolcortez.popular_movies.BuildConfig;


public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/";
    private static final String MOVIEDB_IMAGE_URL = "https://image.tmdb.org/t/p/";
    private static final String YOUTUBE_BASE_URL = "http://www.youtube.com/";
    // Paths
    private final static String AUTH_VERSION = "3";
    private final static String MEDIA_CATEGORY = "movie";
    private final static String IMAGE_SIZE = "w500";
    private final static String RELATED_VIDEOS = "videos";
    private final static String MOVIE_REVIEWS = "reviews";
    private final static String YOUTUBE_WATCH = "watch";
    // Values
    private final static String MOVIEDB_API_KEY = BuildConfig.MOVIEDB_API_KEY;
    private final static String ENGLISH_US = "en-US";
    // Query Keys
    private final static String API_KEY = "api_key";
    private final static String LANG = "language";
    private final static String PAGE = "page";
    private final static String YOUTUBE_VIDEO = "v";

    public static URL buildMovieListUrl(String sortQuery) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(AUTH_VERSION)
                .appendPath(MEDIA_CATEGORY)
                .appendPath(sortQuery)
                .appendQueryParameter(API_KEY, MOVIEDB_API_KEY)
                .appendQueryParameter(LANG, ENGLISH_US)
                .build();

        return buildUrlHelper(builtUri);
    }

    public static URL buildMovieImageURL(String filePath) {
        Uri builtUri = Uri.parse(MOVIEDB_IMAGE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(filePath)
                .build();

        return buildUrlHelper(builtUri);
    }

    public static URL buildSingleMovieUrl(int movieId){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(AUTH_VERSION)
                .appendPath(MEDIA_CATEGORY)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(API_KEY, MOVIEDB_API_KEY)
                .appendQueryParameter(LANG, ENGLISH_US)
                .build();

        return buildUrlHelper(builtUri);
    }

    public static URL buildRelatedVideosUrl(int movieId){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(AUTH_VERSION)
                .appendPath(MEDIA_CATEGORY)
                .appendPath(Integer.toString(movieId))
                .appendPath(RELATED_VIDEOS)
                .appendQueryParameter(API_KEY, MOVIEDB_API_KEY)
                .appendQueryParameter(LANG, ENGLISH_US)
                .build();

        return buildUrlHelper(builtUri);
    }

    public static URL buildMovieReviewsUrl(int movieId){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(AUTH_VERSION)
                .appendPath(MEDIA_CATEGORY)
                .appendPath(Integer.toString(movieId))
                .appendPath(MOVIE_REVIEWS)
                .appendQueryParameter(API_KEY, MOVIEDB_API_KEY)
                .appendQueryParameter(LANG, ENGLISH_US)
                .build();

        return buildUrlHelper(builtUri);
    }

    public static Uri buildYoutubeUrl(String key) {
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(YOUTUBE_WATCH)
                .appendQueryParameter(YOUTUBE_VIDEO, key)
                .build();

        return builtUri;
    }

    private static URL buildUrlHelper(Uri builtUri){
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

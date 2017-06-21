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

    // Paths
    private final static String AUTH_VERSION = "3";
    private final static String MEDIA_CATEGORY = "movie";
    public final static String SORT_POPULAR = "popular";
    public final static String SORT_TOP_RATED = "top_rated";
    private final static String IMAGE_SIZE = "w500";
    // Values
    private final static String MOVIEDB_API_KEY = BuildConfig.MOVIEDB_API_KEY;
    private final static String ENGLISH_US = "en-US";
    // Query Keys
    private final static String API_KEY = "api_key";
    private final static String LANG = "language";

    public static URL buildMovieListUrl(String sortQuery) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(AUTH_VERSION)
                .appendPath(MEDIA_CATEGORY)
                .appendPath(sortQuery)
                .appendQueryParameter(API_KEY, MOVIEDB_API_KEY)
                .appendQueryParameter(LANG, ENGLISH_US)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildMovieImageURL(String filePath) {
        Uri buildUri = Uri.parse(MOVIEDB_IMAGE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(filePath)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Image URI " + url);

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

package io.github.ernestolcortez.popular_movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import static io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract.Movies.*;

public class FavoriteMoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_BY_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavoriteMoviesDbHelper dbHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        dbHelper = new FavoriteMoviesDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor returnCursor = null;

        switch(sUriMatcher.match(uri)) {
            case MOVIES:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_BY_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = COLUMN_MOVIE_ID + " = ?";
                String[] mSelectionArgs = new String[]{id};

                returnCursor = db.query(
                        TABLE_NAME,
                        null,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        null
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case MOVIES:
                long id = db.insert(FavoriteMoviesContract.Movies.TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.Movies.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int deletedRows;

        switch(sUriMatcher.match(uri)){
            case MOVIE_BY_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = COLUMN_MOVIE_ID + " = ?";
                String[] mSelectionArgs = new String[]{id};

                deletedRows = db.delete(
                        TABLE_NAME,
                        mSelection,
                        mSelectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_MOVIES + "/#", MOVIE_BY_ID);

        return uriMatcher;
    }
}

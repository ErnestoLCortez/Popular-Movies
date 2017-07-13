package io.github.ernestolcortez.popular_movies.data;

import io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favoritemovies.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITEMOVIES_TABLE = "CREATE TABLE " +
                Movies.TABLE_NAME + " (" +
                Movies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Movies.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE " +
        ");";

        db.execSQL(SQL_CREATE_FAVORITEMOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Movies.TABLE_NAME);
    }
}

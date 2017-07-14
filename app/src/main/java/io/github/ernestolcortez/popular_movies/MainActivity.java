package io.github.ernestolcortez.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract;
import io.github.ernestolcortez.popular_movies.data.FavoriteMoviesDbHelper;
import io.github.ernestolcortez.popular_movies.utilities.AsyncTaskListener;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private String sortQuery;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sortQuery = sharedPreferences.getString(getString(R.string.pref_sort_key), getResources().getString(R.string.pref_sort_key_popular));

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int spanCount = isPortrait ? 2 : 4;

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(this);
        mDb = dbHelper.getReadableDatabase();

        loadMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(MovieObject selectedMovie) {
        Intent intent = new Intent(getBaseContext(), MovieDetailActivity.class);
        intent.putExtra(MovieObject.MOVIE_KEY, selectedMovie);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sort_key))) {
            sortQuery = sharedPreferences.getString(key, getResources().getString(R.string.pref_sort_key_popular));
            loadMovieData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void loadMovieData() {
        showMovieDataView();
        if(sortQuery.equals(getString(R.string.pref_sort_key_favorites))){
            new FetchMovieTask(new FetchMovieListener()).execute(sortQuery, getFavorites());
        } else {
            new FetchMovieTask(new FetchMovieListener()).execute(sortQuery);
        }
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private Cursor getFavorites() {
        return mDb.query(
                FavoriteMoviesContract.Movies.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteMoviesContract.Movies.COLUMN_MOVIE_ID
        );
    }

    public class FetchMovieListener implements AsyncTaskListener {

        @Override
        public void onPreExecute() {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(MovieObject[] movieObjects) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieObjects != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieObjects);
            } else {
                showErrorMessage();
            }
        }
    }
}

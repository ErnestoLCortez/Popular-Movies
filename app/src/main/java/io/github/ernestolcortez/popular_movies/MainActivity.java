package io.github.ernestolcortez.popular_movies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.github.ernestolcortez.popular_movies.adapters.MovieAdapter;
import io.github.ernestolcortez.popular_movies.async.FetchMovieFromAPITask;
import io.github.ernestolcortez.popular_movies.async.FetchMovieFromDBTask;
import io.github.ernestolcortez.popular_movies.data.FetchMovieFromDBListener;
import io.github.ernestolcortez.popular_movies.utilities.AsyncTaskListener;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;

import static io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract.Movies.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private String sortQuery;
    private ContentObserver favoritesObserver;
    GridLayoutManager gridLayoutManager;
    private static String DATA_STATE = "dataState";

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
        gridLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieAdapter(this);

        if(savedInstanceState != null) {
            MovieObject[] data = (MovieObject[]) savedInstanceState.getParcelableArray(DATA_STATE);
            mMovieAdapter.setMovieData(data);
        } else {
            loadMovieData();
        }
        mRecyclerView.setAdapter(mMovieAdapter);

        favoritesObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                loadMovieData();
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange);
                loadMovieData();
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(CONTENT_URI,true, favoritesObserver);
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
        getContentResolver().unregisterContentObserver(favoritesObserver);
    }

    private void loadMovieData() {
        showMovieDataView();
        if(sortQuery.equals(getString(R.string.pref_sort_key_favorites))){
            new FetchMovieFromDBTask(new FetchMovieListener(), this).execute(CONTENT_URI);
        } else {
            new FetchMovieFromAPITask(new FetchMovieListener()).execute(sortQuery);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(DATA_STATE, mMovieAdapter.getMovieData());
    }

    public class FetchMovieListener implements AsyncTaskListener, FetchMovieFromDBListener {

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

        @Override
        public void onTaskComplete(Cursor cursor) {
            new FetchMovieFromAPITask(new FetchMovieListener()).execute(sortQuery, cursor);
        }
    }

}

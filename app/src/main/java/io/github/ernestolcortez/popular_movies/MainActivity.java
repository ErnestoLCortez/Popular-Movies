package io.github.ernestolcortez.popular_movies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.net.URL;

import io.github.ernestolcortez.popular_movies.utilities.MovieJsonUtils;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;
import io.github.ernestolcortez.popular_movies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private Menu toolbarMenu;
    private String sortQuery;
    private int checkedMenuItemId;

    private final String SORT_STATE_KEY = "sort_state";
    private final String MENU_STATE_KEY = "menu_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState != null){
            sortQuery = savedInstanceState.getString(SORT_STATE_KEY);
            checkedMenuItemId = savedInstanceState.getInt(MENU_STATE_KEY);
        } else {
            sortQuery = NetworkUtils.SORT_POPULAR;
            checkedMenuItemId = R.id.sort_popular;
        }

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadWeatherData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        toolbarMenu = menu;
        toolbarMenu.findItem(checkedMenuItemId).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sort_popular:
                if(item.isChecked())
                    break;
                item.setChecked(true);
                sortQuery = NetworkUtils.SORT_POPULAR;
                loadWeatherData();
                break;
            case R.id.sort_rating:
                if(item.isChecked())
                    break;
                item.setChecked(true);
                sortQuery = NetworkUtils.SORT_TOP_RATED;
                loadWeatherData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSaveInstanceState(Bundle outState) {

        outState.putString(SORT_STATE_KEY, sortQuery);
        outState.putInt(MENU_STATE_KEY, toolbarMenu.findItem(R.id.sort_popular).isChecked() ? R.id.sort_popular : R.id.sort_rating);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(MovieObject selectedMovie) {
        Intent intent = new Intent(getBaseContext(), MovieDetailActivity.class);
        intent.putExtra(MovieObject.MOVIE_KEY, selectedMovie);
        startActivity(intent);
    }

    private void loadWeatherData() {
        showMovieDataView();
        new FetchMovieTask().execute(sortQuery);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @SuppressLint("StaticFieldLeak")
    public class FetchMovieTask extends AsyncTask<String, Void, MovieObject[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieObject[] doInBackground(String... params) {

            if(params.length == 0)
                return null;

            String sortQuery = params[0];
            URL movieRequestUrl = NetworkUtils.buildMovieListUrl(sortQuery);

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);

                return MovieJsonUtils
                        .getMovieObjectsFromJson(jsonResponse);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieObject[] movieObjects) {
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

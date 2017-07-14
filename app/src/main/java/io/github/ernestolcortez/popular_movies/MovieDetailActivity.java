package io.github.ernestolcortez.popular_movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract;
import io.github.ernestolcortez.popular_movies.data.FavoriteMoviesDbHelper;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;

import static io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract.Movies.COLUMN_MOVIE_ID;
import static io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract.Movies.TABLE_NAME;

public class MovieDetailActivity extends AppCompatActivity {
    private MovieObject currentMovie;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;
    private ImageView mPoster;
    private MovieDetailActivity mDetailBinding;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();

        mTitle = (TextView) findViewById(R.id.detail_view_title);
        mReleaseDate = (TextView) findViewById(R.id.detail_view_release);
        mRating = (TextView) findViewById(R.id.detail_view_rating);
        mSynopsis = (TextView) findViewById(R.id.detail_view_synopsis);
        mPoster = (ImageView) findViewById(R.id.detail_view_poster);

        if(savedInstanceState != null){
            currentMovie = savedInstanceState.getParcelable(MovieObject.MOVIE_KEY);
        } else if (intent != null && intent.hasExtra(MovieObject.MOVIE_KEY)){
            currentMovie = getIntent().getParcelableExtra(MovieObject.MOVIE_KEY);
        }

        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        setFavoritesButton();
        fillMovieViews();

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MovieObject.MOVIE_KEY, currentMovie);
        super.onSaveInstanceState(outState);
    }

    private void fillMovieViews() {
        String rating = currentMovie.getVoteAverage() + "/10";

        mTitle.setText(currentMovie.getTitle());
        mReleaseDate.setText(currentMovie.getReleaseDate());
        mRating.setText(rating);
        mSynopsis.setText(currentMovie.getPlotSynopsis());
        Picasso.with(this).load(currentMovie.getMoviePosterPath()).placeholder(R.drawable.posterplaceholder).into(mPoster);
        mPoster.setContentDescription("Poster for the Movie: " + currentMovie.getTitle());
        mPoster.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
    }

    public void addFavorite(View v) {

        mDb.insertOrThrow(
                TABLE_NAME,
                null,
                currentMovie.toContentValue()
        );
    }

    private void setFavoritesButton() {
        String buttonText = isFavorite() ? "Remove" : "Add";
        ((Button) findViewById(R.id.fav_button)).setText(buttonText);
    }

    private boolean isFavorite() {

        String movieId = Integer.toString(currentMovie.getMovieId());
        Log.d("Stuff", movieId);

        Cursor cursor = mDb.query(
                TABLE_NAME,
                null,
                COLUMN_MOVIE_ID + " = ?",
                new String[] { movieId},
                null,
                null,
                null
        );

        return cursor.getCount() > 0;
    }
}

package io.github.ernestolcortez.popular_movies;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.github.ernestolcortez.popular_movies.data.FavoriteMoviesDbHelper;
import io.github.ernestolcortez.popular_movies.data.FetchMovieFromDBListener;
import io.github.ernestolcortez.popular_movies.databinding.ActivityMovieDetailBinding;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;

import static io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract.Movies.*;


public class MovieDetailActivity extends AppCompatActivity {
    private MovieObject currentMovie;
    private ActivityMovieDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        Intent intent = getIntent();

        if(savedInstanceState != null){
            currentMovie = savedInstanceState.getParcelable(MovieObject.MOVIE_KEY);
        } else if (intent != null && intent.hasExtra(MovieObject.MOVIE_KEY)){
            currentMovie = getIntent().getParcelableExtra(MovieObject.MOVIE_KEY);
        }

        new FetchMovieFromDBTask(new FetchMovieListener(), this)
                .execute(contentUriWithId(currentMovie.getMovieId()));
        fillMovieViews();

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MovieObject.MOVIE_KEY, currentMovie);
        super.onSaveInstanceState(outState);
    }

    private void fillMovieViews() {
        String rating = currentMovie.getVoteAverage() + "/10";

        mDetailBinding.detailViewTitle.setText(currentMovie.getTitle());
        mDetailBinding.detailViewRelease.setText(currentMovie.getReleaseDate());
        mDetailBinding.detailViewRating.setText(rating);
        mDetailBinding.detailViewSynopsis.setText(currentMovie.getPlotSynopsis());
        Picasso.with(this).load(currentMovie.getMoviePosterPath()).placeholder(R.drawable.posterplaceholder).into(mDetailBinding.detailViewPoster);
        mDetailBinding.detailViewSynopsis.setContentDescription("Poster for the Movie: " + currentMovie.getTitle());
        mDetailBinding.detailViewSynopsis.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
    }

    public void addFavorite(View v) {
        getContentResolver().insert(CONTENT_URI, currentMovie.toContentValue());
    }

    public void removeFavorite(View v) {
        getContentResolver().delete(contentUriWithId(currentMovie.getMovieId()), null, null);
    }

    private void setFavoritesButton(boolean isFavorite) {
        String buttonText = isFavorite ? "Remove" : "Add";
        ((Button) findViewById(R.id.fav_button)).setText(buttonText);
    }

    public class FetchMovieListener implements FetchMovieFromDBListener {

        @Override
        public void onTaskComplete(Cursor cursor) {
            setFavoritesButton(cursor.getCount() > 0);
        }
    }
}

package io.github.ernestolcortez.popular_movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.github.ernestolcortez.popular_movies.utilities.MovieObject;

public class MovieDetailActivity extends AppCompatActivity {
    private MovieObject currentMovie;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;
    private ImageView mPoster;

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
}

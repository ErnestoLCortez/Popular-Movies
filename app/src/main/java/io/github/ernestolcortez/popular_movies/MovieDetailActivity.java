package io.github.ernestolcortez.popular_movies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import io.github.ernestolcortez.popular_movies.adapters.ReviewAdapter;
import io.github.ernestolcortez.popular_movies.adapters.VideoAdapter;
import io.github.ernestolcortez.popular_movies.async.FetchMovieFromDBTask;
import io.github.ernestolcortez.popular_movies.async.FetchMovieReviewsFromAPITask;
import io.github.ernestolcortez.popular_movies.async.FetchRelatedVideosFromAPITask;
import io.github.ernestolcortez.popular_movies.data.FetchMovieFromDBListener;
import io.github.ernestolcortez.popular_movies.utilities.FetchMovieReviewsListener;
import io.github.ernestolcortez.popular_movies.utilities.FetchRelatedVideosListener;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;
import io.github.ernestolcortez.popular_movies.utilities.RelatedVideo;
import io.github.ernestolcortez.popular_movies.utilities.ReviewObject;

import static io.github.ernestolcortez.popular_movies.data.FavoriteMoviesContract.Movies.*;

public class MovieDetailActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickListener, ReviewAdapter.ReviewAdapterOnClickListener {
    private static final String DESCRIPTION_ADD = "Add movie to favorites";
    private static final String DESCRIPTION_REMOVE = "Remove movie from favorites";
    private static final String TRAILER_DATA = "trailerData";
    private static final String REVIEW_DATA = "reviewData";
    private MovieObject currentMovie;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mSynopsis;
    private ImageView mPoster;
    private ImageView mBackDrop;
    private CardView videosCardView;
    private CardView reviewsCardView;
    private FloatingActionButton mFAB;
    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;

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
        mBackDrop = (ImageView) findViewById(R.id.backdrop);
        mFAB = (FloatingActionButton) findViewById(R.id.fab_button);
        videosCardView = (CardView) findViewById(R.id.videos_cardview);
        reviewsCardView = (CardView) findViewById(R.id.reviews_cardview);

        videoAdapter = new VideoAdapter(this);
        reviewAdapter = new ReviewAdapter(this);

        if (savedInstanceState != null) {
            currentMovie = savedInstanceState.getParcelable(MovieObject.MOVIE_KEY);
            videoAdapter.setVideoData(
                    (ArrayList<RelatedVideo>)savedInstanceState.getSerializable(TRAILER_DATA)
            );
            reviewAdapter.setReviewData(
                    (ReviewObject[])savedInstanceState.getParcelableArray(REVIEW_DATA)
            );
        } else if (intent != null && intent.hasExtra(MovieObject.MOVIE_KEY)) {
            currentMovie = getIntent().getParcelableExtra(MovieObject.MOVIE_KEY);
            new FetchRelatedVideosFromAPITask(new FetchMovieListener()).execute(currentMovie.getMovieId());
            new FetchMovieReviewsFromAPITask(new FetchMovieListener()).execute(currentMovie.getMovieId());
        }

        initializeToolbar();
        initializeRecylerView((RecyclerView) findViewById(R.id.recyclerview_reviews), reviewAdapter);
        initializeRecylerView((RecyclerView) findViewById(R.id.recyclerview_videos), videoAdapter);

        new FetchMovieFromDBTask(new FetchMovieListener(), this)
                .execute(contentUriWithId(currentMovie.getMovieId()));

        fillMovieViews();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MovieObject.MOVIE_KEY, currentMovie);
        outState.putSerializable(TRAILER_DATA, videoAdapter.getVideoData());
        outState.putParcelableArray(REVIEW_DATA, reviewAdapter.getReviewData());
        super.onSaveInstanceState(outState);
    }

    public void initializeToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentMovie.getTitle());
    }

    public void initializeRecylerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void fillMovieViews() {
        String rating = currentMovie.getVoteAverage() + "/10";

        mTitle.setText(currentMovie.getTitle());
        mReleaseDate.setText(currentMovie.getReleaseDate());
        mRating.setText(rating);
        mSynopsis.setText(currentMovie.getPlotSynopsis());
        Picasso.with(this).load(currentMovie.getMoviePosterPath()).placeholder(R.drawable.posterplaceholder).into(mPoster);
        mPoster.setContentDescription("Poster for the Movie: " + currentMovie.getTitle());
        Picasso.with(this).load(currentMovie.getMovieBackdropPath()).placeholder(R.drawable.posterplaceholder).into(mBackDrop);
    }

    public void fabAction(View v) {
        if (v.getContentDescription().equals(DESCRIPTION_ADD)) {
            addFavorite(v);
        } else if (v.getContentDescription().equals(DESCRIPTION_REMOVE)) {
            removeFavorite(v);
        }
    }

    public void addFavorite(View v) {
        Uri uri = getContentResolver().insert(CONTENT_URI, currentMovie.toContentValue());
        setFavoritesButton(uri != null);
    }

    public void removeFavorite(View v) {
        int deletedRow = getContentResolver().delete(contentUriWithId(currentMovie.getMovieId()), null, null);
        setFavoritesButton(deletedRow == 0);
    }

    private void setFavoritesButton(boolean isFavorite) {
        String contentDescription = isFavorite ? DESCRIPTION_REMOVE : DESCRIPTION_ADD;
        int imageId = isFavorite ? R.drawable.ic_clear_white_24px : R.drawable.ic_add_white_24px;
        mFAB.setImageResource(imageId);
        mFAB.setContentDescription(contentDescription);
    }

    @Override
    public void onClick(RelatedVideo video) {
        Intent intent = new Intent(Intent.ACTION_VIEW, video.getVideoUri());
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onClick(ReviewObject review) {
        Intent intent = new Intent(Intent.ACTION_VIEW, review.getUri());
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public class FetchMovieListener implements FetchMovieFromDBListener, FetchMovieReviewsListener, FetchRelatedVideosListener {

        @Override
        public void onTaskComplete(Cursor cursor) {
            setFavoritesButton(cursor.getCount() > 0);
        }

        @Override
        public void onTaskComplete(ReviewObject[] reviews) {
            if (reviews.length > 0) {
                reviewAdapter.setReviewData(reviews);
                reviewsCardView.setVisibility(View.VISIBLE);
            } else {
                reviewsCardView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onTaskComplete(ArrayList<RelatedVideo> videos) {
            if (videos.size() > 0) {
                videoAdapter.setVideoData(videos);
                videosCardView.setVisibility(View.VISIBLE);
            } else {
                videosCardView.setVisibility(View.INVISIBLE);
            }
        }
    }

}

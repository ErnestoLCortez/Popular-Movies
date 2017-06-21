package io.github.ernestolcortez.popular_movies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.ernestolcortez.popular_movies.utilities.MovieObject;

public class MovieDetailActivity extends AppCompatActivity {
    MovieObject currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if(savedInstanceState != null){
            currentMovie = savedInstanceState.getParcelable("movie");
        }
    }
}

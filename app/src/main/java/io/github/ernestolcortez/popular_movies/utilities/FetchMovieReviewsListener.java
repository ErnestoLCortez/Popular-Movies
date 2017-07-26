package io.github.ernestolcortez.popular_movies.utilities;

public interface FetchMovieReviewsListener {
    void onTaskComplete(ReviewObject[] reviews);
}

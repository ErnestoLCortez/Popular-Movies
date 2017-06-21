package io.github.ernestolcortez.popular_movies.utilities;

/**
 * Created by louie on 6/19/17.
 */

public final class MovieObject {
    private String title;
    private String releaseDate;
    private String moviePosterPath;
    private double voteAverage;
    private String plotSynopsis;

    public MovieObject(String title, String releaseDate, String moviePosterPath, double voteAverage, String plotSynopsis) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePosterPath = moviePosterPath;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }
}

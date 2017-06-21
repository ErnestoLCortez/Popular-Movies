package io.github.ernestolcortez.popular_movies.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public final class MovieObject implements Parcelable {

    public final static String MOVIE_KEY = "movie";

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

    public MovieObject(Parcel in){
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.moviePosterPath = in.readString();
        this.voteAverage = in.readDouble();
        this.plotSynopsis = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(moviePosterPath);
        dest.writeDouble(voteAverage);
        dest.writeString(plotSynopsis);
    }

    public static final Creator<MovieObject> CREATOR = new Creator<MovieObject>() {
        @Override
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };

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

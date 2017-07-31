package io.github.ernestolcortez.popular_movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.github.ernestolcortez.popular_movies.R;
import io.github.ernestolcortez.popular_movies.utilities.MovieObject;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private final MovieAdapterOnClickHandler mClickHandler;
    private MovieObject[] mMovieData;
    private Context mContext;
    
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder MovieAdapterViewHolder, int position) {
        MovieObject movie = mMovieData[position];
        String moviePosterURL = movie.getMoviePosterPath();
        Picasso.with(mContext).load(moviePosterURL).placeholder(R.drawable.posterplaceholder).into(MovieAdapterViewHolder.mMoviePoster);
        MovieAdapterViewHolder.mMoviePoster.setContentDescription("Poster for movie: " + movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }

    public void setMovieData(MovieObject[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public MovieObject[] getMovieData() {
        return mMovieData;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieObject selectedMovie);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mMoviePoster;

        public MovieAdapterViewHolder(View view) {
            super(view);

            mMoviePoster = (ImageView) view.findViewById(R.id.poster_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieObject selectedMovie = mMovieData[adapterPosition];
            mClickHandler.onClick(selectedMovie);
        }
    }
}

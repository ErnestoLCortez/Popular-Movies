package io.github.ernestolcortez.popular_movies.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.ernestolcortez.popular_movies.R;
import io.github.ernestolcortez.popular_movies.utilities.ReviewObject;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ReviewObject[] reviews;
    private ReviewAdapterOnClickListener mClickHandler;

    public ReviewAdapter(ReviewAdapterOnClickListener clickHandler) { mClickHandler = clickHandler; }

    public interface ReviewAdapterOnClickListener {
        void onClick(ReviewObject review);
    }

    public void setReviewData(ReviewObject[] reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public ReviewObject[] getReviewData() {
        return reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new ReviewAdapter.ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.author.setText(reviews[position].getAuthor());
        holder.content.setText(reviews[position].getContent());
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.length;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView author;
        TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.review_cardview);
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ReviewObject selectedReview = reviews[adapterPosition];
            mClickHandler.onClick(selectedReview);
        }
    }
}

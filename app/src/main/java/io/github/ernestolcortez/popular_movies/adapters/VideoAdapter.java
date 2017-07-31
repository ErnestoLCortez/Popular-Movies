package io.github.ernestolcortez.popular_movies.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.ernestolcortez.popular_movies.R;
import io.github.ernestolcortez.popular_movies.utilities.RelatedVideo;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private ArrayList<RelatedVideo> videos;
    private VideoAdapterOnClickListener mClickHandler;

    public VideoAdapter(VideoAdapterOnClickListener clickHandler) {
        mClickHandler = clickHandler;
    }

    public void setVideoData(ArrayList<RelatedVideo> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public ArrayList<RelatedVideo> getVideoData() {
        return videos;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_card, parent, false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.title.setText(videos.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface VideoAdapterOnClickListener {
        void onClick(RelatedVideo video);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView title;

        VideoViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.related_videos_cardview);
            title = (TextView) view.findViewById(R.id.video_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            RelatedVideo selectedVideo = videos.get(adapterPosition);
            mClickHandler.onClick(selectedVideo);
        }
    }
}

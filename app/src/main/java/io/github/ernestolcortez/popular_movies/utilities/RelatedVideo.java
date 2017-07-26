package io.github.ernestolcortez.popular_movies.utilities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class RelatedVideo implements Parcelable {

    private Uri videoUri;
    private String title;

    public RelatedVideo(String videoKey, String title) {
        this.videoUri = NetworkUtils.buildYoutubeUrl(videoKey);
        this.title = title;
    }

    public RelatedVideo(Parcel in) {
      this.videoUri = Uri.parse(in.readString());
      this.title = in.readString();
    }

    public static final Creator<RelatedVideo> CREATOR = new Creator<RelatedVideo>() {
        @Override
        public RelatedVideo createFromParcel(Parcel in) {
            return new RelatedVideo(in);
        }

        @Override
        public RelatedVideo[] newArray(int size) {
            return new RelatedVideo[size];
        }
    };

    public Uri getVideoUri() {
        return videoUri;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoUri.toString());
        dest.writeString(title);
    }
}

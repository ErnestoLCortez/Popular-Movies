package io.github.ernestolcortez.popular_movies.utilities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ReviewObject implements Parcelable {

    private String author;
    private String content;
    private Uri uri;

    public ReviewObject(String author, String content, String uri) {
        this.author = author;
        this.content = content;
        this.uri = Uri.parse(uri);
    }

    public ReviewObject(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.uri = Uri.parse(in.readString());
    }

    public static final Creator<ReviewObject> CREATOR = new Creator<ReviewObject>() {
        @Override
        public ReviewObject createFromParcel(Parcel in) {
            return new ReviewObject(in);
        }

        @Override
        public ReviewObject[] newArray(int size) {
            return new ReviewObject[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Uri getUri() {
        return uri;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(uri.toString());
    }
}

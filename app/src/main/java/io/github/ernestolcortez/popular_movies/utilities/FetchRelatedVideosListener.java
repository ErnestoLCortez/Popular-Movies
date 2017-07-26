package io.github.ernestolcortez.popular_movies.utilities;

import java.util.ArrayList;

public interface FetchRelatedVideosListener {

    void onTaskComplete(ArrayList<RelatedVideo> videos);
}

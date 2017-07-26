package io.github.ernestolcortez.popular_movies.utilities;

public interface AsyncTaskListener{

    void onPreExecute();
    void onTaskComplete(MovieObject[] movieObjects);
}

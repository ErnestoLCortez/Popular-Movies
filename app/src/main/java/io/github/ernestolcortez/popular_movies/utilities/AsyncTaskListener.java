package io.github.ernestolcortez.popular_movies.utilities;


import android.database.Cursor;

public interface AsyncTaskListener{

    void onPreExecute();
    void onTaskComplete(MovieObject[] movieObjects);
}

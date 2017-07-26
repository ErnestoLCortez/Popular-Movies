# Popular-Movies

Developed in fulfillment of Udacity Android Developer Nanodegree, Project 1 Stage 1.

* Presents the user with a grid arrangement of movie posters upon launch.
* Allows user to change sort order via a setting:
* The sort order can be by most popular or by highest-rated
* Allows the user to tap on a movie poster and transition to a details screen with additional information such as:
  - original title
  - movie poster image thumbnail
  - A plot synopsis (called overview in the api)
  - user rating (called vote_average in the api)
  - release date
  - Trailers
  - User reviews

## Getting Started

The app requires an API key from theMovieDB.org.  Add the following to your gradle.properties file in the root directory:
```
MOVIEDB_API_KEY="YOUR API KEY HERE"
```

## Built With

* [Android Studio](https://developer.android.com/studio/index.html) - IDE
* [MovieDB API](https://themoviedb.org/) - Movie data API

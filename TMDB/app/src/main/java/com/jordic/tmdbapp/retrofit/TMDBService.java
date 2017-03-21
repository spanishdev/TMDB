package com.jordic.tmdbapp.retrofit;

import com.jordic.tmdbapp.pojo.movies.MovieResult;
import com.jordic.tmdbapp.pojo.configuration.ConfigurationResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by J on 16/03/2017.
 */

public interface TMDBService {

    @GET("/3/configuration")
    Observable<ConfigurationResult> getConfiguration(@Query("api_key") String apiKey);

    @GET("/3/movie/popular")
    Observable<MovieResult> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("/3/search/movie")
    Observable<MovieResult> searchMovies(@Query("api_key") String apiKey, @Query("query") String keyword, @Query("page") int page);
}

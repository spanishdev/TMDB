package com.jordic.tmdbapp.retrofit;

import com.jordic.tmdbapp.pojo.movies.MovieResult;
import com.jordic.tmdbapp.pojo.configuration.ConfigurationResult;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by J on 16/03/2017.
 *
 * This class manages all the Retrofit calls, returning responses as Observables
 */

public class RetrofitManager {

    public static final String BASE_URL = "https://api.themoviedb.org";

    /**
     * Request the configuration in order to build image urls
     *
     * @param apiKey Api Key, necessary in order to use the Webservice
     * @return An Observable with the results
     */
    public Observable getConfiguration (String apiKey)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBService service = retrofit.create(TMDBService.class);

        return service.getConfiguration(apiKey);
    }

    /**
     * It gets the Most popular movies in TMDB
     *
     * @param apiKey Api Key, necessary in order to use the Webservice
     * @param page Number of page which we want to take the data
     * @return An Observable with the results
     */
    public Observable getPopularMovies (String apiKey, int page)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBService service = retrofit.create(TMDBService.class);

        return service.getPopularMovies(apiKey,page);
    }

    /**
     * It returns a list of movies according to the word  wanted to search
     *
     * @param apiKey Api Key, necessary in order to use the Webservice
     * @param keyword Word to search
     * @param page Number of page to take the data
     * @return An Observable with the results
     */
    public Observable searchMovies (String apiKey, String keyword, int page)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDBService service = retrofit.create(TMDBService.class);

        return service.searchMovies(apiKey,keyword,page);
    }
}

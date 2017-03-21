package com.jordic.tmdbapp.ui;

import android.content.SharedPreferences;

import com.jordic.tmdbapp.pojo.movies.Movie;
import com.jordic.tmdbapp.preferences.PreferenceOperations;

import java.util.List;

/**
 * Created by J on 16/03/2017.
 */

public interface MainContract {

    interface View
    {
        void showLoading(boolean show);

        void addMovies(List<Movie> movies);

        void clearMovies();

        void showError(int resId);
    }

    interface Presenter
    {
        void getPopularMovies(String apiKey,int page);

        void searchMovies(String apiKey,String keyword, int page);

        void cancelRequest();

        void getConfiguration(String apiKey, PreferenceOperations preferences);
    }
}

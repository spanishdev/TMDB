package com.jordic.tmdbapp.ui;

import com.jordic.tmdbapp.R;
import com.jordic.tmdbapp.pojo.configuration.ConfigurationResult;
import com.jordic.tmdbapp.pojo.configuration.Image;
import com.jordic.tmdbapp.pojo.movies.MovieResult;
import com.jordic.tmdbapp.preferences.PreferenceOperations;
import com.jordic.tmdbapp.retrofit.RetrofitManager;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by J on 16/03/2017.
 */

public class MainPresenter implements MainContract.Presenter {

    //FIELDS
    MainContract.View mainView;
    RetrofitManager retrofitManager;
    Subscription requestSubscription;

    @Inject
    public MainPresenter( MainContract.View mainView, RetrofitManager retrofitManager)
    {
        this.mainView = mainView;
        this.retrofitManager=retrofitManager;
    }

    @Override
    public void getConfiguration(final String apiKey, final PreferenceOperations preferences) {
        mainView.showLoading(true);

        Observable observableResult = retrofitManager.getConfiguration(apiKey);

        requestSubscription = observableResult.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ConfigurationResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        mainView.showLoading(false);
                        mainView.showError(R.string.error_no_connection);

                    }

                    @Override
                    public void onNext(ConfigurationResult result) {
                        //From all the sizes, the 4th is selected because is the Medium size
                        Image images = result.getImages();
                        StringBuilder imageUrlBuilder = new StringBuilder(images.getBaseUrl());
                        imageUrlBuilder.append(images.getPosterSizes().get(3));

                        preferences.putString(PreferenceOperations.PREF_IMAGE_URL,imageUrlBuilder.toString());

                        //Once stored the image url, proceed to get the first popular movies
                        mainView.showLoading(false);
                        getPopularMovies(apiKey,1);
                    }
                });
    }

    @Override
    public void getPopularMovies(String apiKey, int page) {

        mainView.showLoading(true);

        Observable observableResult = retrofitManager.getPopularMovies(apiKey,page);

        requestSubscription = observableResult.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        mainView.showLoading(false);
                        mainView.showError(R.string.error_no_connection);

                    }

                    @Override
                    public void onNext(MovieResult movieResult) {

                        mainView.showLoading(false);
                        if(movieResult!=null) mainView.addMovies(movieResult.getMovies());

                    }
                });
    }

    @Override
    public void searchMovies(String apiKey,String keyword, int page) {

        mainView.showLoading(true);

        Observable observableResult = retrofitManager.searchMovies(apiKey,keyword,page);

        requestSubscription = observableResult.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        mainView.showLoading(false);
                        mainView.showError(R.string.error_no_connection);

                    }

                    @Override
                    public void onNext(MovieResult movieResult) {
                        mainView.showLoading(false);
                        mainView.addMovies(movieResult.getMovies());
                    }
                });
    }

    @Override
    public void cancelRequest() {
        if(requestSubscription!=null)
        {
            requestSubscription.unsubscribe();
            mainView.showLoading(false);
        }

    }


}


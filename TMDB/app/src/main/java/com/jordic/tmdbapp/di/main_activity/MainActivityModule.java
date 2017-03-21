package com.jordic.tmdbapp.di.main_activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jordic.tmdbapp.di.PerActivity;
import com.jordic.tmdbapp.retrofit.RetrofitManager;
import com.jordic.tmdbapp.ui.MainActivity;
import com.jordic.tmdbapp.ui.MainContract;
import com.jordic.tmdbapp.ui.MainPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by J on 17/03/2017.
 */

@Module
public class MainActivityModule {

    MainContract.View mainView;

    public MainActivityModule( MainContract.View mainView)
    {
        this.mainView=mainView;
    }

    @Provides
    @PerActivity
    public MainContract.Presenter providePresenter()
    {
        return new MainPresenter(mainView,new RetrofitManager());
    }

}

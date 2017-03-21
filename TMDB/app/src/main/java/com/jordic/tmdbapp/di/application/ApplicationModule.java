package com.jordic.tmdbapp.di.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jordic.tmdbapp.di.PerActivity;
import com.jordic.tmdbapp.preferences.PreferenceOperations;
import com.jordic.tmdbapp.retrofit.RetrofitManager;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * Created by J on 17/03/2017.
 */
@Singleton
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application application() {
        return application;
    }

    @Provides
    public PreferenceOperations providePreferences() {
        return new PreferenceOperations(application);
    }

}

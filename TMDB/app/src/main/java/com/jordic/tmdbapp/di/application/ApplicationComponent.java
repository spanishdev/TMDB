package com.jordic.tmdbapp.di.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.jordic.tmdbapp.di.main_activity.MainActivityModule;
import com.jordic.tmdbapp.preferences.PreferenceOperations;
import com.jordic.tmdbapp.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by J on 17/03/2017.
 */
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void injectApplication(Application application);

    PreferenceOperations preferenceOperations();
}

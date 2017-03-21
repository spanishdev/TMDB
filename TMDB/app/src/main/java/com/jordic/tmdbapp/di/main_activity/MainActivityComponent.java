package com.jordic.tmdbapp.di.main_activity;

import com.jordic.tmdbapp.di.PerActivity;
import com.jordic.tmdbapp.di.application.ApplicationComponent;
import com.jordic.tmdbapp.di.application.ApplicationModule;
import com.jordic.tmdbapp.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;

/**
 * Created by J on 17/03/2017.
 */
@PerActivity
@Component( dependencies = ApplicationComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}

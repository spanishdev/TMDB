package com.jordic.tmdbapp;

import android.app.Application;

import com.jordic.tmdbapp.di.application.ApplicationComponent;
import com.jordic.tmdbapp.di.application.ApplicationModule;
import com.jordic.tmdbapp.di.application.DaggerApplicationComponent;

/**
 * Created by J on 17/03/2017.
 */

public class TMDBApplication extends Application {

    ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        component.injectApplication(this);
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}

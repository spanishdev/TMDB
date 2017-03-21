package com.jordic.tmdbapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by J on 18/03/2017.
 *
 * This class Manages the Operations of SharedPrefences, such as Read, Insert or Update its values.
 *
 * I will use only String values, so that's why there are only these functions. In case that Integers, Floats or other data
 * were needed, I would insert functions with these data as well
 */

public class PreferenceOperations {

    public static final String PREF_IMAGE_URL="IMAGE_URL";

    private SharedPreferences preferences;

    public PreferenceOperations(Context context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //GETTERS

    public String getString(String key, @Nullable String defaultValue)
    {
        if(key==null)
            throw new IllegalArgumentException("The key passed to SharedPreferences cannot be null");

        return preferences.getString(key,defaultValue);
    }

    //SETTERS

    public void putString(String key, String value)
    {
        if(key==null)
            throw new IllegalArgumentException("The key passed to SharedPreferences cannot be null");

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(key,value);
        preferencesEditor.commit();
    }
}

package com.jordic.tmdbapp;

import android.content.res.Resources;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jordic.tmdbapp.ui.adapters.MovieAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by J on 20/03/2017.
 */

public class RecyclerViewMatchers {

    public static Matcher<RecyclerView.ViewHolder> withViewHolderTitle(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, MovieAdapter.MovieViewHolder>( MovieAdapter.MovieViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text: " + text);
            }

            @Override
            protected boolean matchesSafely(MovieAdapter.MovieViewHolder item) {
                if(item.getItemViewType()!=MovieAdapter.VIEW_TYPE_MOVIE) return false;

                TextView titleTextView = (TextView) item.itemView.findViewById(R.id.movieTitle);
                if (titleTextView == null) {
                    return false;
                }

                return titleTextView.getText().toString().contains(text);
            }
        };
    }

}

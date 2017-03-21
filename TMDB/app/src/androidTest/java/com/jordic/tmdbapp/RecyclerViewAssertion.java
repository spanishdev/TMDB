package com.jordic.tmdbapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by J on 19/03/2017.
 */

public class RecyclerViewAssertion {

    public ViewAssertion matchCount(final int count) {

        return new ViewAssertion() {

            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {

                if (noViewFoundException!=null) {
                    throw noViewFoundException;
                }

                RecyclerView recyclerView = (RecyclerView) view;

                assertEquals(count,recyclerView.getAdapter().getItemCount());
            }
        };
    }


}

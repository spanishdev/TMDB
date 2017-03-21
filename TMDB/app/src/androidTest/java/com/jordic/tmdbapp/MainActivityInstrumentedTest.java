package com.jordic.tmdbapp;

import android.preference.Preference;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import com.jordic.tmdbapp.preferences.PreferenceOperations;
import com.jordic.tmdbapp.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.Build.VERSION_CODES.M;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jordic.tmdbapp.RecyclerViewMatchers.withViewHolderTitle;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    //FIELDS
    RecyclerViewAssertion recyclerViewAssertion;

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, true,
                    true);   // launchActivity

    @Before
    public void setUp()
    {
        recyclerViewAssertion = new RecyclerViewAssertion();
    }

    /**
     * This tests when there are no connectivity.
     * NOTE: Disabling the device's internet is required to test it properly
     */
    @Test
    public void noConnectionTest()
    {
        //Check that the error view is not displayed
        onView(withId(R.id.errorTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.errorTextView)).check(matches(withText(R.string.error_no_connection)));
    }

    /**
     * Tests that popular Movies are shown
     */
    @Test
    public void popularMoviesTest()
    {
        //Check that the error view is not displayed
        onView(withId(R.id.errorTextView)).check(matches(not(isDisplayed())));

        //Check that the adapter count is equal to 20 (the movie list added)
        int movieCount = 20;
        onView(withId(R.id.movieRecyclerView)).check(recyclerViewAssertion.matchCount(movieCount));

        //Check that the first element title equals to "Logan"
        //NOTE: This may vary depending on the popularity of the TMDB
        onView(withId(R.id.movieRecyclerView)).perform(RecyclerViewActions.scrollToHolder(withViewHolderTitle("Logan")));
    }

    /**
     * Tests taht the Searching function works correctly
     */
    @Test
    public void searchMoviesTest()
    {
        //Check that the error view is not displayed
        onView(withId(R.id.errorTextView)).check(matches(not(isDisplayed())));

        onView(withId(R.id.searchEditText)).perform(typeText("ir"));

        //Check that the adapter count is equal to 20 (the movie list added)
        int movieCount = 20;
        onView(withId(R.id.movieRecyclerView)).check(recyclerViewAssertion.matchCount(movieCount));

        //Check that the element with title "Tent City" exists
        onView(withId(R.id.movieRecyclerView)).perform(RecyclerViewActions.scrollToHolder(withViewHolderTitle("Tent City")));

        //Now we test a search with no results
        onView(withId(R.id.searchEditText)).perform(typeText("x"));

        //The Search Field now have the value "irx", which has no results, so the RecyclerView is empty
        movieCount = 0;
        onView(withId(R.id.movieRecyclerView)).check(recyclerViewAssertion.matchCount(movieCount));

        //Error view is displayed because the list is empty
        onView(withId(R.id.errorTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.errorTextView)).check(matches(withText(R.string.message_no_elements)));

        //And then, we return to previous search
        onView(withId(R.id.searchEditText)).perform(replaceText("ir"));

        //We repeat the previous checks
        movieCount = 20;
        onView(withId(R.id.movieRecyclerView)).check(recyclerViewAssertion.matchCount(movieCount));
        onView(withId(R.id.movieRecyclerView)).perform(RecyclerViewActions.scrollToHolder(withViewHolderTitle("Tent City")));

    }

}

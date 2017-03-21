package com.jordic.tmdbapp.ui;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jordic.tmdbapp.R;
import com.jordic.tmdbapp.TMDBApplication;
import com.jordic.tmdbapp.di.application.ApplicationComponent;
import com.jordic.tmdbapp.di.main_activity.DaggerMainActivityComponent;
import com.jordic.tmdbapp.di.main_activity.MainActivityModule;
import com.jordic.tmdbapp.preferences.PreferenceOperations;
import com.jordic.tmdbapp.pojo.movies.Movie;
import com.jordic.tmdbapp.ui.adapters.MovieAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jordic.tmdbapp.R.id.emptyProgressBar;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    //WIDGETS
    @BindView(R.id.searchEditText)
    EditText searchEditText;

    @BindView(R.id.movieRecyclerView)
    RecyclerView movieRecyclerView;

    @BindView(R.id.errorTextView)
    TextView errorTextView;

    //INJECTED FIELDS
    @Inject
    MainContract.Presenter presenter;

    @Inject
    PreferenceOperations preferences;

    //FIELDS
    private int currentPage = 1;
    LinearLayoutManager layoutManager;
    MovieAdapter movieAdapter;

    //This is used to avoid calling multiple times the loading when the scroll is at the bottom
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initializeDagger();

        initializeViews();

        //Check if have already an image base url into Preferences. If not, get it from TMDB.
        if (preferences.getString(PreferenceOperations.PREF_IMAGE_URL, "").isEmpty()) {
            presenter.getConfiguration(getString(R.string.apiKey), preferences);
        } else presenter.getPopularMovies(getString(R.string.apiKey), currentPage);
    }

    /**
     * Initializes Dagger 2 and the Injections
     */
    private void initializeDagger() {

        ApplicationComponent component = ((TMDBApplication) getApplicationContext()).getComponent();

        DaggerMainActivityComponent.builder()
                .applicationComponent(component)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    /**
     * Initialize the views and widgets of this activity
     */
    private void initializeViews() {

        //First create and set the RecyclerView properties (adapter, layoutmanager and ScrollListener)
        movieAdapter = new MovieAdapter(getApplicationContext(), preferences);
        movieRecyclerView.setAdapter(movieAdapter);
        layoutManager = new LinearLayoutManager(this);
        movieRecyclerView.setLayoutManager(layoutManager);

        //Here the RecyclerView scroll listener is set, it will check when the scroll reached the bottom
        movieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!isLoading) {
                    int itemCount = movieAdapter.getItemCount();

                    if (itemCount>0 && layoutManager.findLastCompletelyVisibleItemPosition() == itemCount - 1) {
                        isLoading = true;

                        //Here check if the Search Edit Text has any string or not.
                        //If it is not empty, load more pages of the search, otherwise
                        //load more popular movies
                        if (!searchEditText.getText().toString().isEmpty()) {
                            presenter.searchMovies(getString(R.string.apiKey), searchEditText.getText().toString(), currentPage);
                        } else {
                            presenter.getPopularMovies(getString(R.string.apiKey), currentPage);
                        }
                    }
                }
            }
        });


        //Set the Search Edit Text Listener when a key is pressed
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Get the string written in the EditText
                final String keyword = editable.toString();

                //First, clear the movies, there will be a new list
                clearMovies();
                //Then,  search for the movies. If the keybord written is not empty, search for movies.
                //Otherwise, load the popular movies again
                if(!keyword.isEmpty())
                {
                    presenter.searchMovies(getString(R.string.apiKey),keyword,currentPage);
                }
                else presenter.getPopularMovies(getString(R.string.apiKey),currentPage);
            }
        });

    }

    /**
     * Shows the ProgressBar when the List is Empty
     *
     * @param show True to show, False to hide
     */
    @Override
    public void showLoading(boolean show) {

        //Hide the Error Message in case the list is loaded again
        if(show) errorTextView.setVisibility(View.GONE);
        isLoading = show;
        movieAdapter.showLoading(show);
    }

    /**
     * Add a list of movies to the Recyclerview if it is not empty. Otherwise it will show an error message.
     * @param movies List of movies to add
     */
    @Override
    public void addMovies(List<Movie> movies) {
        if (movies != null && !movies.isEmpty()) {
            movieAdapter.addMovies(movies);
            currentPage++;
        }
        else
        {
            showError(R.string.message_no_elements);
        }
    }

    /**
     * Clear all the movies from the RecyclerView
     */
    @Override
    public void clearMovies() {
        presenter.cancelRequest();
        movieAdapter.clearMovies();
        currentPage = 1;
    }

    /**
     * Shows an error from the String resources
     * @param resId Id of the String resource
     */
    @Override
    public void showError(int resId) {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(resId);
    }
}

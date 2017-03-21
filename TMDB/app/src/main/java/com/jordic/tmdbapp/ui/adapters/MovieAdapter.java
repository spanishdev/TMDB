package com.jordic.tmdbapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jordic.tmdbapp.R;
import com.jordic.tmdbapp.pojo.movies.Movie;
import com.jordic.tmdbapp.preferences.PreferenceOperations;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by J on 16/03/2017.
 *
 * This is the adapter of the RecyclerView which shows the movies
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //CONSTANTS
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_MOVIE = 1;

    //FIELDS
    private Context context;
    private List<Movie> movieList;
    private PreferenceOperations preferences;

    /**
     * ViewHolder which is used with the movies
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        //WIDGETS
        @BindView(R.id.movieTitle)
        TextView movieTitle;

        @BindView(R.id.movieYear)
        TextView movieYear;

        @BindView(R.id.movieOverview)
        TextView movieOverview;

        @BindView(R.id.movieImageView)
        ImageView movieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Loads the ViewHolder with the data which the Movie has
         *
         * @param movie Movie containing the data
         */
        public void setData(Movie movie) {
            movieTitle.setText(movie.getTitle());
            movieOverview.setText(movie.getOverview());

            //Get the release date. If it has the format of YYYY-MM-dd, proceed to get the Year.
            String releaseDate = movie.getReleaseDate();
            if (releaseDate != null && releaseDate.indexOf("-") > 0) {
                //The year will be the value before the first '-', so I take all the substring until this character is found
                movieYear.setText(releaseDate.split("-")[0]);
            } else movieYear.setText("Unknown");

            //Finally,  load the Image
            if (!preferences.getString(PreferenceOperations.PREF_IMAGE_URL, "").isEmpty()) {
                String url = preferences.getString(PreferenceOperations.PREF_IMAGE_URL, "") + movie.getPosterPath();
                Glide.with(context).load(url).into(movieImageView);
            }
        }

    }

    /**
     * ViewHolder which is used to show the ProgressBar
     */
    class LoadingViewHolder extends RecyclerView.ViewHolder {

        //WIDGETS
        @BindView(R.id.bottomProgressBar)
        ProgressBar bottomProgressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * Creates a new Movie Adapter
     * @param context Context
     * @param preferences Preferences were the base url of the image is stored
     */
    public MovieAdapter(Context context, PreferenceOperations preferences) {
        this.context = context;
        movieList = new ArrayList<>();
        this.preferences = preferences;
    }

    /**
     * Returns a new ViewHolder. It depends of the type of View. If it is a Movie Type, it will return a MovieViewHolder.
     * Otherwise it will return a LoadingViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_MOVIE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_adapter_layout, parent, false);
            return new MovieViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_adapter_layout, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    /**
     * If the Item Type is a MOVIE TYPE, then set the viewholder at the specified position, the data of the movie at that position
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_MOVIE)
            ((MovieViewHolder) holder).setData(getMovie(position));
    }

    /**
     * If the Movie at position is null, then it is a Loading View. Otherwise, the Type will be a Movie Type
     *
     * @param position Position of the adapter
     * @return The type of view
     */
    @Override
    public int getItemViewType(int position) {
        if (movieList.get(position) == null) return VIEW_TYPE_LOADING;
        else return VIEW_TYPE_MOVIE;
    }

    /**
     * Return the number of movies in the list plus 1, which represents the loading view
     *
     * @return The number of movies in the list plus 1, which represents the loading view
     */
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * Shows or hides the ProgressBar
     * @param show True if want to show the ProgressBar, False otherwise
     */
    public void showLoading(boolean show) {

        //In order to show the progressBar, I add a NULL movie, this will add a new view to the adapter
        if (show) {
            movieList.add(null);
            notifyItemInserted(getItemCount());
        }
        //In other case, remove the last view, which represents the loading view
        //I check before that the Item count is greather than 0, to avoid exceptions
        else if(getItemCount()>0) {
            movieList.remove(getItemCount() - 1);
            notifyItemRemoved(getItemCount());
        }
    }

    //List Operations

    /**
     * Adds a new Movie List to the current list of the adapter
     * @param movies List of movies to add
     */
    public void addMovies(List<Movie> movies) {
        movieList.addAll(movies);
        notifyDataSetChanged();
    }

    /**
     * Removes all the movies of the dapter
     */
    public void clearMovies() {
        movieList.clear();
        notifyDataSetChanged();
    }

    /**
     * Returns the movie at the specified position
     * @param position Index of the movie to get
     * @return The movie at index "position"
     */
    public Movie getMovie(int position) {
        return movieList.get(position);
    }
}

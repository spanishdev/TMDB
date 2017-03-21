package com.jordic.tmdbapp;

import com.jordic.tmdbapp.pojo.movies.Movie;
import com.jordic.tmdbapp.pojo.movies.MovieResult;
import com.jordic.tmdbapp.preferences.PreferenceOperations;
import com.jordic.tmdbapp.responses.ConfigurationMockResults;
import com.jordic.tmdbapp.responses.MoviesMockResults;
import com.jordic.tmdbapp.retrofit.RetrofitManager;
import com.jordic.tmdbapp.ui.MainActivity;
import com.jordic.tmdbapp.ui.MainPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    MainActivity mainActivity;

    @Mock
    RetrofitManager retrofitManager;

    @Mock
    PreferenceOperations preferenceOperations;

    //FIELDS
    MainPresenter presenter;
    String apiKey="";

    //Mock server results
    ConfigurationMockResults configurationResults;
    MoviesMockResults movieResults;

    @Before
    public void setUp() {

        //We set both Schedulers to inmediate while testing
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });

        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook(){
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });

        //Initialize the necessary objects
        presenter = new MainPresenter(mainActivity, retrofitManager);
        configurationResults=new ConfigurationMockResults();
        movieResults = new MoviesMockResults();
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

    /**
     * I test that the proper image url is stored when the webservice to get the Configuration is called
     */
    @Test
    public void getConfigurationOKTest() {

        //First I mock the Observables (getConfig and getPopularMovies, the latter is called after getting the result)
        when(retrofitManager.getConfiguration(apiKey))
                .thenReturn(Observable.just(configurationResults.getConfigurationOK()));

        when(retrofitManager.getPopularMovies(apiKey,1))
                .thenReturn(Observable.just(any(MovieResult.class)));

        //Perform the getConfiguration call
        presenter.getConfiguration(apiKey, preferenceOperations);

        //I check that showloading is called, at least once (it can be called twice, one in GetConfiguration
        //and other time in getPopularMovies)
        verify(mainActivity, VerificationModeFactory.atLeastOnce()).showLoading(true);
        verify(mainActivity, VerificationModeFactory.atLeastOnce()).showLoading(false);

        //I check that the proper url is stored to the Preferences
        verify(preferenceOperations).putString(PreferenceOperations.PREF_IMAGE_URL,"http://image.tmdb.org/t/p/w342");
    }

    /**
     * Tests when an error occurred while calling getConfiguration
     */
    @Test
    public void getConfigurationErrorTest() {

        when(retrofitManager.getConfiguration(apiKey))
                .thenReturn(Observable.error(new HttpException(configurationResults.configurationErrorResponse())));

        //Perform the getConfiguration call
        presenter.getConfiguration(apiKey, preferenceOperations);

        verify(mainActivity).showLoading(true);
        verify(mainActivity, VerificationModeFactory.atLeastOnce()).showLoading(false);
        verify(mainActivity).showError(anyInt());

        //Checks that the PutString was not called this time
        verify(preferenceOperations,VerificationModeFactory.times(0)).putString(PreferenceOperations.PREF_IMAGE_URL,"http://image.tmdb.org/t/p/w342");

    }

    /**
     * Test when the Popular Movies call is executed successfully
     */
    @Test
    public void getPopularMoviesOKTest() {

        int page = 1;

        //First we build the observable built from popularResults
        Observable moviesObservable = Observable.just(movieResults.getMoviesOK(page));
        when(retrofitManager.getPopularMovies(apiKey,page))
                .thenReturn(moviesObservable);

        //Perform the getPopularMovies call
        presenter.getPopularMovies(apiKey, page);

        //I check that showloading is called
        verify(mainActivity).showLoading(true);
        verify(mainActivity).showLoading(false);

        //ArgumentCaptor captures the list of movies were are called from the Presenter
        ArgumentCaptor<List<Movie>> movieList = ArgumentCaptor.forClass(ArrayList.class);
        verify(mainActivity).addMovies(movieList.capture());

        //Then, we check that the movie count is the expected, and one of the results is the same as the fake json
        assertEquals(20,movieList.getValue().size());
        assertEquals("Logan",movieList.getValue().get(0).getTitle());

    }

    /**
     * Test the Popular Movies when there were an error
     */
    @Test
    public void getPopularMoviesErrorTest() {

        int page = 1;

        when(retrofitManager.getPopularMovies(apiKey,page))
                .thenReturn(Observable.error(new HttpException(movieResults.movieErrorResult())));

        //Perform the popularmovies call
        presenter.getPopularMovies(apiKey, page);

        verify(mainActivity).showLoading(true);
        verify(mainActivity, VerificationModeFactory.atLeastOnce()).showLoading(false);
        verify(mainActivity).showError(anyInt());

        //We check that the OnNext is not called
        verify(mainActivity,VerificationModeFactory.times(0)).addMovies(ArgumentMatchers.<Movie>anyList());
    }


    /**
     * Test the Search Movies call
     */
    @Test
    public void getSearchMoviesOKTest() {

        int page = 1;
        String keyword = "ir";

        when(retrofitManager.searchMovies(anyString(),anyString(),anyInt()))
        .thenAnswer(new Answer<Observable>(){
            @Override
            public Observable answer(InvocationOnMock invocation){
                String keywordArg = invocation.getArgument(1).toString();
                return Observable.just(movieResults.getSearchMoviesOK(keywordArg));

            }});

        presenter.searchMovies(apiKey,keyword ,page);

        //I check that showloading is called
        verify(mainActivity).showLoading(true);
        verify(mainActivity).showLoading(false);

        //ArgumentCaptor captures the list of movies were are called from the Presenter
        ArgumentCaptor<List<Movie>> movieList = ArgumentCaptor.forClass(ArrayList.class);
        verify(mainActivity).addMovies(movieList.capture());

        //Then, we check that the movie count is the expected, and one of the results is the same as the fake json
        assertEquals(20,movieList.getValue().size());
        assertEquals("Tent City",movieList.getValue().get(0).getTitle());

        //Now we verify with a Search without results
        presenter.searchMovies(apiKey, "irx",page);

        verify(mainActivity,VerificationModeFactory.atLeastOnce()).addMovies(movieList.capture());

        //This time, the movie list is empty
        assertEquals(0,movieList.getValue().size());
    }

    /**
     * Test the Search Movies call with an error response
     */
    @Test
    public void getSearchMoviesErrorTest() {

        int page = 1;

        when(retrofitManager.searchMovies(apiKey,"keyword",page))
                .thenReturn(Observable.error(new HttpException(movieResults.movieErrorResult())));

        //Perform the popularmovies call
        presenter.searchMovies(apiKey,"keyword", page);

        verify(mainActivity).showLoading(true);
        verify(mainActivity, VerificationModeFactory.atLeastOnce()).showLoading(false);
        verify(mainActivity).showError(anyInt());

        //We check that the OnNext is not called
        verify(mainActivity,VerificationModeFactory.times(0)).addMovies(ArgumentMatchers.<Movie>anyList());
    }

    /**
     * We test that the request is cancelled successfully
     */
    @Test
    public void cancelRequestTest() {
        //Check the cancelRequest is called when the subscription is null
        presenter.cancelRequest();
        verify(mainActivity,times(0)).showLoading(true);

        //Check the subscription is cancelled when a Service is called (this case, getPopularMovies)
        int page=1;

        Observable moviesObservable = Observable.just(movieResults.getMoviesOK(page));
        when(retrofitManager.getPopularMovies(apiKey,page))
                .thenReturn(moviesObservable);

        presenter.getPopularMovies(apiKey,page);

        presenter.cancelRequest();

        //If this is true, the method CancelRequest has been executed
        verify(mainActivity).showLoading(true);

    }


}
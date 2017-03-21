package com.jordic.tmdbapp.responses;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jordic.tmdbapp.pojo.configuration.ConfigurationResult;
import com.jordic.tmdbapp.pojo.movies.MovieResult;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by J on 18/03/2017.
 * <p>
 * This class generates Mock Server Responses from the Webservice "getConfiguration". It loads the response files from the
 * resources directory.
 */

public class MoviesMockResults {

    /**
     * Read a text file from the path selected
     *
     * @param path Path to the file to read
     * @return A string containing the content of the file
     */
    public String readFile(String path) {
        InputStream inputStream = getClass().getResourceAsStream(path);
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");

        return s.hasNext() ? s.next() : "";
    }

    /**
     * Reads the Movies Result depending on the Page number
     * @param page Page number
     * @return The mock response JSON as String
     */
    private String getJSONFromPage(int page) {
        if (page == 1) return readFile("MoviesJSON.json");
        else if (page > 1) return readFile("MoviesNextPageJSON.json");
        else return readFile("MoviesPageErrorJSON.json");
    }

    /**
     * Reads the Movies Result depending on the keyword
     * @param keyword Keyword to use
     * @return The mock response JSON as String
     */
    private String getJSONFromKeyword(String keyword) {
        if (keyword.equalsIgnoreCase("ir")) return readFile("MoviesSearch_IR_JSON.json");
        else return readFile("MoviesSearch_NoResults_JSON.json");
    }

    /**
     * Returns a Mock popular movies result in case that it is OK
     *
     * @param page Depending on the page Number, we return one or other mock result
     * @return The MovieResult object from the json string
     */
    public MovieResult getMoviesOK(int page) {
        String json = getJSONFromPage(page);
        Type type = new TypeToken<MovieResult>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }


    /**
     * Returns an error response
     *
     * @return
     */
    public Response movieErrorResult() {
        String json = readFile("ErrorJSON.json");

        Response response = Response.error(404, ResponseBody.create(MediaType.parse("application/json"), json));
        return response;
    }

    /**
     * Returns a Mock Search Movies results with the given Keyword
     *
     * @param keyword Keyword that we want to search
     * @return The MovieResult object from the json string
     */
    public MovieResult getSearchMoviesOK(String keyword) {
        String json = getJSONFromKeyword(keyword);
        Type type = new TypeToken<MovieResult>() {
        }.getType();

        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }


}

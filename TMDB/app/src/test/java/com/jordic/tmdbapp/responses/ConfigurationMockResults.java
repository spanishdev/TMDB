package com.jordic.tmdbapp.responses;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jordic.tmdbapp.pojo.configuration.ConfigurationResult;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by J on 18/03/2017.
 *
 * This class generates Mock Server Responses from the Webservice "getConfiguration". It loads the response files from the
 * resources directory.
 */

public class ConfigurationMockResults {

    /**
     * Read a text file from the path selected
     * @param path Path to the file to read
     * @return A string containing the content of the file
     */
    public String readFile(String path)
    {
        InputStream inputStream =  getClass().getResourceAsStream(path);
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");

       return s.hasNext() ? s.next() : "";
    }

    /**
     * Returns a Mock Configuration result in case that it is OK
     * @return A ConfigResult with an OK response
     */
    public ConfigurationResult getConfigurationOK()
    {
        String json = readFile("ConfigurationJSON.json");
        Type type = new TypeToken<ConfigurationResult>(){}.getType();

        Gson gson = new Gson();
        return gson.fromJson(json,type);
    }

    /**
     * Returns a Mock Configuration result in case that the Server returns an error
     * @return A ConfigResult with an error response
     */
    public Response configurationErrorResponse()
    {
        String json = readFile("ErrorJSON.json");

        Response response = Response.error(401, ResponseBody.create(MediaType.parse("application/json") ,json));
        return response;
    }
}

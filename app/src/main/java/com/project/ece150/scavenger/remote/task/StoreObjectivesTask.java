package com.project.ece150.scavenger.remote.task;

import android.os.AsyncTask;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.remote.ObjectivesParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * This Task is used to store data to the backend via a REST POST request in
 * asynchronous fire-and-forget fashion.
 */
public class StoreObjectivesTask extends AsyncTask<String, String, Integer> {

    private ObjectivesParser _parser;
    private IObjective _objective;

    public StoreObjectivesTask(ObjectivesParser parser, IObjective objective) {
        _parser = parser;
        _objective = objective;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // Parse into Request
        JSONObject jObjective = _parser.parseObjectiveToJSON(_objective);

        // Execute Request
        URL url;
        HttpURLConnection urlConnection = null;
        JSONArray response = new JSONArray();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            int responseCode = urlConnection.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return 0;
    }
}
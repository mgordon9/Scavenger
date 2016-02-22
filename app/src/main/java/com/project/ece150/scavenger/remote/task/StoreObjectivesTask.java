package com.project.ece150.scavenger.remote.task;

import android.accounts.NetworkErrorException;
import android.net.Uri;
import android.os.AsyncTask;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.remote.ObjectivesParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
        // Execute Request
        URL url;
        HttpURLConnection urlConnection = null;
        JSONArray response = new JSONArray();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Accept", "application/json");

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("keyname", _objective.getOwner())
                    .appendQueryParameter("title", _objective.getTitle())
                    .appendQueryParameter("info", _objective.getInfo())
                    .appendQueryParameter("latitude", String.valueOf(_objective.getLatitude()))
                    .appendQueryParameter("longitude", String.valueOf(_objective.getLongitude()))
                    .appendQueryParameter("owner", _objective.getOwner())
                    .appendQueryParameter("otherConfirmedUsers", "<tbd>")
                    .appendQueryParameter("activity", "tutoring");
            String query = builder.build().getEncodedQuery();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            int responseCode=urlConnection.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                throw new NetworkErrorException();
            }

            urlConnection.connect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return 0;
    }
}
package com.project.ece150.scavenger.remote.task;

import android.accounts.NetworkErrorException;
import android.net.Uri;
import android.os.AsyncTask;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.remote.parser.ObjectiveParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This Task is used to store data to the backend via a REST POST request in
 * asynchronous fire-and-forget fashion.
 */
public class CreateObjectiveTask extends AsyncTask<String, String, Integer> {

    private ObjectiveParser _parser;
    private IObjective _objective;

    public CreateObjectiveTask(ObjectiveParser parser, IObjective objective) {
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
            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jObjective = _parser.parseObjectiveToJSON(_objective);

            String sObjective = jObjective.toString();
            byte[] postData = sObjective.getBytes( StandardCharsets.UTF_8 );

            try(DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                wr.write(postData);
            }


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
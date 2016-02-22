package com.project.ece150.scavenger.remote.task;

import android.os.AsyncTask;
import android.util.Log;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.remote.ObjectivesParser;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Observer;

/**
 * This Task Object is used to request data from the backend via a REST GET in
 * asynchronous fashion. Then, the data will be parsed into a List of IObjective objects.
 */
public class GetObjectivesTask extends AsyncTask<String, String, List<IObjective>> {

    private Observer _observer;
    private ObjectivesParser _parser;

    public GetObjectivesTask(Observer observer, ObjectivesParser parser) {
        _observer = observer;
        _parser = parser;
    }

    @Override
    protected List<IObjective> doInBackground(String... params) {
        // Fetch Data
        URL url;
        HttpURLConnection urlConnection = null;
        JSONArray response = new JSONArray();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == 200){
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("GetObjectivesTask", responseString);
                response = new JSONArray(responseString);
            }else{
                Log.v("GetObjectivesTask", "Response code:"+ responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        // Parse Objectives
        List<IObjective> objectives = _parser.parseJSONToObjectives(response);
        return objectives;
    }

    @Override
    protected void onPostExecute(List<IObjective> result) {
        // Notify Observer of updated data set.
        _observer.update(null, result);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}

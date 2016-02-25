package com.project.ece150.scavenger.remote.task;

import android.os.AsyncTask;
import android.util.Log;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.remote.parser.ObjectiveParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * This Task Object is used to request data from the backend via a REST GET in
 * asynchronous fashion. Then, the data will be parsed into a List of IObjective objects.
 */
public class GetObjectiveTask extends AsyncTask<String, String, IObjective> {

    private IGetObjectiveTaskObserver _observer;
    private ObjectiveParser _parser;

    public GetObjectiveTask(IGetObjectiveTaskObserver observer, ObjectiveParser parser) {
        _observer = observer;
        _parser = parser;
    }

    @Override
    protected IObjective doInBackground(String... params) {
        // Fetch Data
        URL url;
        HttpURLConnection urlConnection = null;
        JSONObject response = new JSONObject();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == 200){
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("GetObjectiveTask", responseString);
                response = new JSONObject(responseString);
            }else{
                Log.v("GetObjectiveTask", "Response code:"+ responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        // Parse Objectives
        IObjective objective = _parser.parseJSONToObjective(response);
        return objective;
    }

    @Override
    protected void onPostExecute(IObjective result) {
        // Notify Observer of updated data set.
        _observer.onObjectiveGetReceived(result);
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

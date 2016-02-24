package com.project.ece150.scavenger.remote.task;

import android.os.AsyncTask;
import android.util.Log;

import com.project.ece150.scavenger.IUser;
import com.project.ece150.scavenger.remote.parser.UserParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This Task Object is used to request data from the backend via a REST GET in
 * asynchronous fashion. Then, the data will be parsed into a IUser object.
 */
public class GetUserTask extends AsyncTask<String, String, IUser> {

    private IGetUserTaskObserver _observer;
    private UserParser _parser;

    public GetUserTask(IGetUserTaskObserver observer, UserParser parser) {
        _observer = observer;
        _parser = parser;
    }

    @Override
    protected IUser doInBackground(String... params) {
        // Fetch Data
        URL url;
        HttpURLConnection urlConnection = null;
        JSONObject response = new JSONObject();

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == 200){
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("GetObjectivesTask", responseString);
                response = new JSONObject(responseString);
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
        IUser user = _parser.parseJSONToUser(response);
        return user;
    }

    @Override
    protected void onPostExecute(IUser result) {
        // Notify Observer of updated data set.
        _observer.onUserGetReceived(result);
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

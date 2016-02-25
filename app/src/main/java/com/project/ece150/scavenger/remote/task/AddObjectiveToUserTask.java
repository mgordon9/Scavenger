package com.project.ece150.scavenger.remote.task;

import android.accounts.NetworkErrorException;
import android.net.Uri;
import android.os.AsyncTask;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.IUser;
import com.project.ece150.scavenger.remote.EObjectiveConfirmedType;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This Task is used to store data to the backend via a REST POST request in
 * asynchronous fire-and-forget fashion.
 */
public class AddObjectiveToUserTask extends AsyncTask<String, String, Integer> {

    private String _objectiveid;
    private EObjectiveConfirmedType _objectiveConfirmedType;

    public AddObjectiveToUserTask(String objectiveid, EObjectiveConfirmedType type) {
        _objectiveid = objectiveid;
        _objectiveConfirmedType = type;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // Execute Request
        URL url;
        HttpURLConnection urlConnection = null;
        JSONArray response = new JSONArray();

        String confirmedTypeStr = new String();
        if (_objectiveConfirmedType ==  EObjectiveConfirmedType.VISUALLYCONFIRMED) {
            confirmedTypeStr = "visualObjective";
        } else {
            confirmedTypeStr = "locationObjective";
        }

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
                    .appendQueryParameter("objectiveType", confirmedTypeStr)
                    .appendQueryParameter("objectiveId", _objectiveid);
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
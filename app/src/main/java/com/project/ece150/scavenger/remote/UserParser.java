package com.project.ece150.scavenger.remote;

import com.project.ece150.scavenger.IUser;
import com.project.ece150.scavenger.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Helper object for parsing JSON to IUser objects
 */
public class UserParser {
    ObjectivesParser _objectivesParser;

    public UserParser() {
        _objectivesParser = new ObjectivesParser();
    }

    public IUser parseJSONToUser(JSONArray json) {

        User user = new User();

        try {
            JSONObject jUser = json.getJSONObject(0);
            user.setScore(jUser.getDouble("score"));
            user.setLocationObjectives(_objectivesParser.parseJSONToObjectives(jUser.getJSONArray("locationObjectives")));
            user.setLocationObjectives(_objectivesParser.parseJSONToObjectives(jUser.getJSONArray("locationObjectives")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}

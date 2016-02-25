package com.project.ece150.scavenger.remote.parser;

import com.project.ece150.scavenger.IUser;
import com.project.ece150.scavenger.User;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Helper object for parsing JSON to IUser objects
 */
public class UserParser {
    ObjectiveParser _objectiveParser;

    public UserParser() {
        _objectiveParser = new ObjectiveParser();
    }

    public IUser parseJSONToUser(JSONObject json) {

        User user = new User();

        try {
            user.setUserid(json.getString("userid"));
            if(json.getString("score") != "null")
                user.setScore(json.getDouble("score"));
            user.setLocationObjectives(_objectiveParser.parseJSONToObjectives(json.getJSONArray("locationObjectives")));
            user.setVisualObjectives(_objectiveParser.parseJSONToObjectives(json.getJSONArray("visualObjectives")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}

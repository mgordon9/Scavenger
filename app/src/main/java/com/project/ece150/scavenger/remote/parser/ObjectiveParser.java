package com.project.ece150.scavenger.remote.parser;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.Objective;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Helper object for parsing JSON to IObjective objects
 */
public class ObjectiveParser {
    public ObjectiveParser() {
    }

    public List<IObjective> parseJSONToObjectives(JSONArray json) {

        LinkedList<IObjective> list = new LinkedList<IObjective>();

        for(int i=0; i<json.length(); i++) {
            Objective objective = new Objective();

            try {
                JSONObject jObjective = json.getJSONObject(i);

                objective.setObjectiveid(jObjective.getString("keyname"));
                objective.setTitle(jObjective.getString("title"));
                objective.setInfo(jObjective.getString("info"));
                objective.setLatitude(Double.parseDouble(jObjective.getString("latitude")));
                objective.setLongitude(Double.parseDouble(jObjective.getString("longitude")));
                objective.setOwner(jObjective.getString("owner"));

//              TODO: Parse OtherConfirmedUsers
//                objective.setOtherConfirmedUsers(jCoords.getString("otherConfirmedUsers"));

                list.push((IObjective) objective);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public JSONObject parseObjectiveToJSON(IObjective objective) {
        JSONObject jObjective = null;

        try {
            jObjective = new JSONObject();
            jObjective.put("keyname", objective.getTitle());

            JSONObject jCoords = new JSONObject();
            jCoords.put("latitude", objective.getLatitude());
            jCoords.put("longitude", objective.getLongitude());
            jObjective.put("coords", jCoords);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObjective;
    }
}

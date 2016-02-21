package com.project.ece150.scavenger;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.project.ece150.scavenger.remote.ObjectivesParser;

import org.json.JSONArray;

import java.util.List;


public class ObjectivesParserTest extends ApplicationTestCase<Application> {
    public ObjectivesParserTest() {
        super(Application.class);
    }

    public void UserMockTest() throws Exception {

        String responseString = "[{\"keyname\":\"location2841455999479258\",\"latitude\":40.1046562621,\"longitude\":-121.955652198,\"activity\":\"tutoring\",\"date\":1455999479258,\"completelyPopulated\":true},{\"keyname\":\"location2641455999478771\",\"latitude\":37.6859059268,\"longitude\":-120.909530144,\"activity\":\"grocery\",\"date\":1455999478771,\"completelyPopulated\":true},{\"keyname\":\"location3091455999479869\",\"latitude\":33.8748477124,\"longitude\":-120.50927338,\"activity\":\"takeoutdog\",\"date\":1455999479869,\"completelyPopulated\":true},{\"keyname\":\"location2161455999477564\",\"latitude\":40.5565947417,\"longitude\":-120.213225841,\"activity\":\"drink\",\"date\":1455999477564,\"completelyPopulated\":true},{\"keyname\":\"location2931455999479467\",\"latitude\":35.457793678,\"longitude\":-120.155429955,\"activity\":\"player\",\"date\":1455999479467,\"completelyPopulated\":true}]";
        JSONArray response = new JSONArray(responseString);
        ObjectivesParser parser = new ObjectivesParser();
        List<IObjective> objectives = parser.parseJSONToObjectives(response);

        // Test for length
        assertEquals(5, objectives.size());
    }
}
package com.project.ece150.scavenger.remote;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.remote.task.GetObjectivesTask;
import com.project.ece150.scavenger.remote.task.StoreObjectivesTask;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This Object holds the state of the remote objectives.
 * Update must be performed by client polling.
 */
public class ObjectivesClient implements Observer {

    private ObjectivesParser _objectivesParser;
    private String _objectiveResourceUrl;
    private List<IObjective> _objectives;

    public ObjectivesClient(String objectiveResourceUrl) {
        _objectivesParser = new ObjectivesParser();
        _objectiveResourceUrl = objectiveResourceUrl;
    }

    public ObjectivesParser getObjectiveParser() {
        return _objectivesParser;
    }

    public void initDataRequest() {
        GetObjectivesTask task = new GetObjectivesTask(this, _objectivesParser);
        task.execute(_objectiveResourceUrl);
    }

    public void initStoreRequest(IObjective objective) {
        StoreObjectivesTask task = new StoreObjectivesTask(_objectivesParser, objective);
        task.execute(_objectiveResourceUrl);
    }

    @Override
    public void update(Observable observable, Object data) {
        _objectives = (List<IObjective>) data;
    }
}
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
    private Observer _observer;
    List<IObjective> _objectives;

    public ObjectivesClient(Observer observer, String objectiveResourceUrl) {
        _objectivesParser = new ObjectivesParser();
        _objectiveResourceUrl = objectiveResourceUrl;
        _observer = observer;
    }

    public List<IObjective> getObjectives() {
        return _objectives;
    }

    public void initDataRequest() {
        GetObjectivesTask task = new GetObjectivesTask(this, _objectivesParser);
        task.execute(_objectiveResourceUrl);
    }

    // called when List<IObjective> object is available.
    @Override
    public void update(Observable observable, Object data) {
        _objectives = (List<IObjective>) data;

        if(_observer != null) {
            _observer.update(null, _objectives);
        }
    }

    public void initStoreRequest(IObjective objective) {
        StoreObjectivesTask task = new StoreObjectivesTask(_objectivesParser, objective);
        task.execute(_objectiveResourceUrl);
    }
}
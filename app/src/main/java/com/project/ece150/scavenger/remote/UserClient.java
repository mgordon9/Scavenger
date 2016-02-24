package com.project.ece150.scavenger.remote;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.IUser;
import com.project.ece150.scavenger.remote.task.GetUserTask;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * This Object retrieves a user entity.
 */
public class UserClient implements Observer {

    private Observer _observer;
    private UserParser _userParser;
    private String _userResourceUrl;

    public UserClient(Observer observer, String objectiveResourceUrl) {
        _observer = observer;
        _userParser = new UserParser();
        _userResourceUrl = objectiveResourceUrl;
    }

    public void initDataRequest() {
        GetUserTask task = new GetUserTask(this, _userParser);
        task.execute(_userResourceUrl);
    }

    // called when User object is available.
    @Override
    public void update(Observable observable, Object data) {
        IUser user = (IUser) data;

        if(_observer != null) {
            _observer.update(null, user);
        }
    }


}

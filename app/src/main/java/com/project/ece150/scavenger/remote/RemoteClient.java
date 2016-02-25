package com.project.ece150.scavenger.remote;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.IUser;
import com.project.ece150.scavenger.User;
import com.project.ece150.scavenger.remote.parser.ObjectiveParser;
import com.project.ece150.scavenger.remote.parser.UserParser;
import com.project.ece150.scavenger.remote.task.AddObjectiveToUserTask;
import com.project.ece150.scavenger.remote.task.CreateUserTask;
import com.project.ece150.scavenger.remote.task.GetObjectiveTask;
import com.project.ece150.scavenger.remote.task.GetObjectivesTask;
import com.project.ece150.scavenger.remote.task.GetUserTask;
import com.project.ece150.scavenger.remote.task.IGetObjectiveTaskObserver;
import com.project.ece150.scavenger.remote.task.IGetObjectivesTaskObserver;
import com.project.ece150.scavenger.remote.task.IGetUserTaskObserver;
import com.project.ece150.scavenger.remote.task.CreateObjectiveTask;

import java.util.List;

/**
 * This Object stores/receives entities.
 */
public class RemoteClient implements IGetUserTaskObserver, IGetObjectivesTaskObserver, IGetObjectiveTaskObserver {

    private IRemoteClientObserver _observer;
    private UserParser _userParser;
    private ObjectiveParser _objectiveParser;
    private String _resourceURI;

    public RemoteClient(IRemoteClientObserver observer, String resourceUrl) {
        _observer = observer;
        _userParser = new UserParser();
        _objectiveParser = new ObjectiveParser();
        _resourceURI = resourceUrl;
    }

    /**
     * User Requests
     */
    public void initUserGetRequest(String userid) {
        GetUserTask task = new GetUserTask(this, _userParser);
        task.execute(_resourceURI + "/rest/users/" + userid);
    }

    public void initUserCreateRequest(String userid) {
        User user = new User();
        user.setUserid(userid);
        CreateUserTask task = new CreateUserTask(user);
        task.execute(_resourceURI + "/rest/users");
    }

    public void initUserAddObjectiveRequest(IUser user, IObjective objective, EObjectiveConfirmedType type) {
        AddObjectiveToUserTask task = new AddObjectiveToUserTask(user, objective, type);
        task.execute(_resourceURI + "/rest/users/" + user.getUserid());
    }

    /**
     * Objective Requests
     */
    public void initObjectivesGetRequest() {
        GetObjectivesTask task = new GetObjectivesTask(this, _objectiveParser);
        task.execute(_resourceURI + "/rest/objectives");
    }

    public void initObjectiveGetRequest(IObjective objective) {
        GetObjectiveTask task = new GetObjectiveTask(this, _objectiveParser);
        task.execute(_resourceURI + "/rest/objectives/" + objective.getObjectiveid());
    }

    public void initObjectivesCreateRequest(IObjective objective) {
        CreateObjectiveTask task = new CreateObjectiveTask(_objectiveParser, objective);
        task.execute(_resourceURI + "/rest/objectives");
    }

    /**
     * Callback methods implementation
     */
    @Override
    public void onUserGetReceived(IUser user) {
        _observer.onUserGetReceived(user);
    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {
        _observer.onObjectivesGetReceived(objectives);
    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {
        _observer.onObjectiveGetReceived(objective);
    }
}

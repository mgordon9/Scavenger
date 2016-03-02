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

import java.util.LinkedList;
import java.util.List;

/**
 * This Object stores/receives entities.
 */
public class RemoteClient implements IGetUserTaskObserver, IGetObjectivesTaskObserver, IGetObjectiveTaskObserver {

    private List<IRemoteClientObserver> _observers;
    private UserParser _userParser;
    private ObjectiveParser _objectiveParser;
    private String _resourceURI;

    public RemoteClient(String resourceUrl) {
        _userParser = new UserParser();
        _objectiveParser = new ObjectiveParser();
        _resourceURI = resourceUrl;

        _observers = new LinkedList<IRemoteClientObserver>();
    }

    public void registerObserver(IRemoteClientObserver observer) {
        _observers.add(observer);
    }

    /**
     * User Requests
     */

    /**
     * GET Requests that returns a "IUser" via the callback "onUserGetReceived".
     * @param userid        The User ID
     */
    public void initUserGetRequest(String userid) {
        GetUserTask task = new GetUserTask(this, _userParser);
        task.execute(_resourceURI + "/rest/users/" + userid);
    }

    /**
     * POST Request that creates a User entity in the Backend.
     * @param userid        The User ID, serves as key in the database representation
     */
    public void initUserCreateRequest(String userid) {
        User user = new User();
        user.setUserid(userid);
        CreateUserTask task = new CreateUserTask(user);
        task.execute(_resourceURI + "/rest/users");
    }

    /**
     * POST Request that adds an Objective id to a User entity in the Backend.
     * @param userid        The User
     * @param objectiveid   The Objective
     * @param type          The EObjectiveConfirmedType
     */
    public void initUserAddObjectiveRequest(String userid, String objectiveid, EObjectiveConfirmedType type) {
        AddObjectiveToUserTask task = new AddObjectiveToUserTask(objectiveid, type);
        task.execute(_resourceURI + "/rest/users/" + userid);
    }

    /**
     * Objective Requests
     */

    /**
     * GET Request that returns a list of "IObjective" via the callback "onObjectivesGetReceived".
     */
    public void initObjectivesGetRequest() {
        GetObjectivesTask task = new GetObjectivesTask(this, _objectiveParser);
        task.execute(_resourceURI + "/rest/objectives");
    }

    /**
     * POST Request that creates an Objective entity in the Backend.
     * @param objective     The objective to store. All fields will be in the database representation
     */
    public void initObjectivesCreateRequest(IObjective objective) {
        CreateObjectiveTask task = new CreateObjectiveTask(_objectiveParser, objective);
        task.execute(_resourceURI + "/rest/objectives");
    }

    /**
     * GET Request that returns a Objective, including the full-sized image.
     * @param objectiveid   The objective
     */
    public void initObjectiveGetRequest(String objectiveid) {
        GetObjectiveTask task = new GetObjectiveTask(this, _objectiveParser);
        task.execute(_resourceURI + "/rest/objectives/" + objectiveid);
    }

    /**
     * Callback methods implementation
     */
    @Override
    public void onUserGetReceived(IUser user) {
        for(IRemoteClientObserver observer : _observers) {
            observer.onUserGetReceived(user);
        }
    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {
        for(IRemoteClientObserver observer : _observers) {
            observer.onObjectivesGetReceived(objectives);
        }
    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {
        for(IRemoteClientObserver observer : _observers) {
            observer.onObjectiveGetReceived(objective);
        }
    }
}

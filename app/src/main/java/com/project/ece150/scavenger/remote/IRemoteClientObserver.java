package com.project.ece150.scavenger.remote;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.IUser;

import java.util.List;

public interface IRemoteClientObserver {

    void onUserGetReceived(IUser user);
    void onObjectivesGetReceived(List<IObjective> objectives);
    void onObjectiveGetReceived(IObjective objective);
    void onObjectiveCreated();
}

package com.project.ece150.scavenger.remote.task;

import com.project.ece150.scavenger.IObjective;

public interface IGetObjectiveTaskObserver {

    void onObjectiveGetReceived(IObjective objective);
}
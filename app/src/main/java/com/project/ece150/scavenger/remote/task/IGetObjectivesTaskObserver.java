package com.project.ece150.scavenger.remote.task;

import com.project.ece150.scavenger.IObjective;
import java.util.List;

public interface IGetObjectivesTaskObserver {

    void onObjectivesGetReceived(List<IObjective> objectives);
}

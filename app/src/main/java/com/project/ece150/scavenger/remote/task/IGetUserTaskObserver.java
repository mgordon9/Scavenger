package com.project.ece150.scavenger.remote.task;

import com.project.ece150.scavenger.IUser;

public interface IGetUserTaskObserver {

    void onUserGetReceived(IUser user);
}

package com.project.ece150.scavenger.mocks;

import com.project.ece150.scavenger.IObjective;
import com.project.ece150.scavenger.IUser;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class UserMock implements IUser {

    private LinkedList<IObjective> _objectives;

    public UserMock() {
        _objectives = new LinkedList<IObjective>();

        for(int i=0; i<10; i++) {
            ObjectiveMock objective = new ObjectiveMock();
            _objectives.push(objective);
        }
    }

    @Override
    public String getUserid() { return "<userid>"; }

    @Override
    public Double getScore() {
        return 999.9;
    }

    @Override
    public List<IObjective> getLocationObjectives() {

        return _objectives;
    }

    @Override
    public List<IObjective> getVisualObjectives() {
        return _objectives;
    }

    public String toString() {
        String ret = "";

        boolean first = true;
        for(IObjective objective : _objectives) {
            if(first) {
                first = false;
            } else {
                ret += "; ";
            }

            ret += objective.toString();
        }

        return ret;
    }
}
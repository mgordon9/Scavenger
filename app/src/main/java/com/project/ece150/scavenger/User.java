package com.project.ece150.scavenger;

import java.util.List;

public class User implements IUser {

    private String _userid;
    private Double _score;
    private List<IObjective> _locationObjectives;
    private List<IObjective> _visualObjectives;

    @Override
    public String getUserid() { return _userid; }

    public void setUserid(String userid) { _userid = userid; }

    @Override
    public Double getScore() {
        return _score;
    }

    public void setScore(Double score) {
        _score = score;
    }

    @Override
    public List<IObjective> getLocationObjectives() {
        return _locationObjectives;
    }

    public void setLocationObjectives(List<IObjective> locationObjectives) {
        _locationObjectives = locationObjectives;
    }

    @Override
    public List<IObjective> getVisualObjectives() {
        return _visualObjectives;
    }

    public void setVisualObjectives(List<IObjective> visualObjectives) {
        _visualObjectives = visualObjectives;
    }
}

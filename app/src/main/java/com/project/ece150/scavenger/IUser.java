package com.project.ece150.scavenger;

import com.project.ece150.scavenger.IObjective;
import java.util.List;

public interface IUser {
    /* Current Score */
    Double getScore();

    /* Visited objectives, confirmed on location data */
    List<IObjective> getLocationObjectives();

    /* Visited objectives, confirmed on matched image */
    List<IObjective> getVisualObjectives();
}
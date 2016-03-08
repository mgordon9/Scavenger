package com.project.ece150.scavenger;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;

import java.util.LinkedList;
import java.util.List;


public class CompletedObjectivesFragment extends Fragment implements IRemoteClientObserver {

    RemoteClient mRemoteClient;
    private String _username;
    private static int[] rowColors;
    private static Bitmap[] objectiveThumbnails;
    private static String[] objectiveDetails;
    private static String[] objectiveNames;
    private static ListView completedList;



    public CompletedObjectivesFragment() {
        // Required empty public constructor
    }

    public void initialize(RemoteClient remoteClient, String userId) {
        mRemoteClient = remoteClient;
        _username = userId;
        mRemoteClient.registerObserver(this); //register observer
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_completedobjectives, container, false);
        completedList = (ListView) rootView.findViewById(R.id.listView);
        mRemoteClient.initUserGetRequest(_username);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onUserGetReceived(IUser user) {
        List<IObjective> completedLocationObjectives = user.getLocationObjectives();
        List<IObjective> completedImageObjectives = user.getVisualObjectives();
        List<IObjective> completedObjectives= parseObjectives(completedLocationObjectives,completedImageObjectives);

        if (completedObjectives != null) {
            int size = completedObjectives.size();
            objectiveNames = new String[size];
            objectiveThumbnails = new Bitmap[size];
            objectiveDetails = new String[size];
            rowColors = new int[size];
            int i = 0;
            for (IObjective o : completedObjectives) {
                String s = o.getTitle();
                Bitmap b = o.getThumbnail();
                objectiveNames[i] = s;
                objectiveThumbnails[i] = b;
                if((o.isVisitedGPS() && o.isVisitedVisual())){
                    objectiveDetails[i] = "Objective Complete! Good Job";
                    rowColors[i] = Color.argb(90,19,119,22);
                }
                else if(o.isVisitedVisual()){
                    objectiveDetails[i] = "Objective Visually Confirmed, Location Confirmation Incomplete";
                    rowColors[i] = Color.argb(90,235,247,56);
                }
                else if(o.isVisitedGPS()){
                    objectiveDetails[i] = "Objective Positionally Confirmed, Visual Confirmation Incomplete";
                    rowColors[i] = Color.argb(90,235,247,56);
                }
                else{
                    objectiveDetails[i] = "Objective Incomplete! Get to Work!!!";
                    rowColors[i] = Color.argb(90,249,25,25);
                }
                i++;
            }

        }
        else{
            objectiveNames = new String[1];
            objectiveNames[0] = "No objectives completed ";
            rowColors = new int[1];
            rowColors[0] = Color.argb(90,249,25,25);

        }

        CustomListAdapter adapter = new CustomListAdapter(getActivity(), objectiveNames,objectiveDetails,objectiveThumbnails,rowColors);
        completedList.setAdapter(adapter);
    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {

    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {

    }

    @Override
    public void onObjectiveCreated() {

    }

    private List<IObjective> parseObjectives(List<IObjective> locationObjectives, List<IObjective> visualObjectives) {
        List<IObjective> objectives = new LinkedList<IObjective>();

        if(locationObjectives == null && visualObjectives == null) {
            return null;
        }

        if(locationObjectives != null && visualObjectives == null) {
            for(IObjective o : locationObjectives) {
                Objective obj = (Objective) o;
                obj.setVisitedGPS(true);
                obj.setVisitedVisual(false);
                objectives.add(obj);
            }
        }

        if(locationObjectives == null && visualObjectives != null) {
            for(IObjective o : visualObjectives) {
                Objective obj = (Objective) o;
                obj.setVisitedGPS(false);
                obj.setVisitedVisual(true);
                objectives.add(obj);
            }
        }

        if(locationObjectives != null && visualObjectives != null) {
            List<IObjective> remainingVisualObjectives = visualObjectives;

            for(IObjective o : locationObjectives) {
                Objective obj = (Objective) o;
                if(containsObjectiveById(visualObjectives, o)) {
                    remainingVisualObjectives = removeObjectiveById(remainingVisualObjectives, o);

                    obj.setVisitedGPS(true);
                    obj.setVisitedVisual(true);
                } else {
                    obj.setVisitedGPS(true);
                    obj.setVisitedVisual(false);
                }
                objectives.add(obj);
            }

            for(IObjective o : remainingVisualObjectives) {
                Objective obj = (Objective) o;

                obj.setVisitedGPS(false);
                obj.setVisitedVisual(true);

                objectives.add(obj);
            }
        }

        return objectives;
    }

    private boolean containsObjectiveById(List<IObjective> objectives, IObjective objective) {
        if(objectives == null || objective == null) {
            return false;
        }

        for(IObjective o : objectives) {
            if(o.getObjectiveid().equals(objective.getObjectiveid()))
                return true;
        }

        return false;
    }

    private List<IObjective> removeObjectiveById(List<IObjective> objectives, IObjective objective) {
        if(objectives == null || objective == null) {
            return null;
        }

        List<IObjective> retObjectives = new LinkedList<IObjective>();

        for(IObjective o : objectives) {
            if(o.getObjectiveid().equals(objective.getObjectiveid()) == false)
                retObjectives.add(o);
        }

        return retObjectives;
    }
}



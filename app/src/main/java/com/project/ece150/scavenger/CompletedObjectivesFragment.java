package com.project.ece150.scavenger;

import android.app.Activity;
import android.app.ListFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.ece150.scavenger.remote.IRemoteClientObserver;
import com.project.ece150.scavenger.remote.RemoteClient;

import java.util.ArrayList;
import java.util.List;


public class CompletedObjectivesFragment extends Fragment implements IRemoteClientObserver {

    RemoteClient mRemoteClient;
    private String _username;
    Bitmap[] objectiveThumbnails;
    String[] objectiveDetails;
    String[] objectiveNames;// = {"obj1", "obj2", "obj3", "obj4", "obj5","obj1", "obj2", "obj3", "obj4", "obj5","obj1", "obj2", "obj3", "obj4", "obj5","obj1", "obj2", "obj3", "obj4", "obj5"};
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

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_layout,objectiveNames);
        //completedList.setAdapter(adapter);

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
        List<IObjective> completedObjectives = user.getLocationObjectives();
        if (completedObjectives != null) {
            int size = completedObjectives.size();
            objectiveNames = new String[size];
            objectiveThumbnails = new Bitmap[size];
            objectiveDetails = new String[size];
            int i = 0;
            for (IObjective o : completedObjectives) {
                String s = o.getTitle();
                Bitmap b = o.getThumbnail();
                objectiveNames[i] = s;
                objectiveThumbnails[i] = b;
                if(o.isVisitedGPS() && o.isVisitedVisual()){
                    objectiveDetails[i] = "Objective Complete! Good Job";
                }
                else if(o.isVisitedVisual()){
                    objectiveDetails[i] = "Objective Visually Confirmed, Location Confirmation Incomplete";
                }
                else if(o.isVisitedGPS()){
                    objectiveDetails[i] = "Objective Positionally Confirmed, Visual Confirmation Incomplete";
                }
                else{
                    objectiveDetails[i] = "Objective Incomplete! Get to Work!!!";
                }

                i++;
            }
        }
        else{
            Log.e("poop", " is null af");
            objectiveNames = new String[1];
            objectiveNames[0] = "No objectives completed :(";
        }

        CustomListAdapter adapter = new CustomListAdapter(getActivity(), objectiveNames,objectiveDetails,objectiveThumbnails);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_layout,objectiveNames);
        completedList.setAdapter(adapter);
    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {

    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {

    }
}



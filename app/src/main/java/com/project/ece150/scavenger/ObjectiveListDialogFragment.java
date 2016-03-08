package com.project.ece150.scavenger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.project.ece150.scavenger.remote.IRemoteClientObserver;

import java.util.List;

/**
 *
 */
public class ObjectiveListDialogFragment extends DialogFragment
        implements IRemoteClientObserver {

    private ObjectivesFragment.OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private String mTitle;
    private Button mCompareButton;

    public ObjectiveListDialogFragment() {
        // Required empty public constructor
    }

    public void setListener(ObjectivesFragment.OnListFragmentInteractionListener listener)
    {
        mListener = listener;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onUserGetReceived(IUser user) {
    }

    @Override
    public void onObjectivesGetReceived(List<IObjective> objectives) {
        PickObjectiveRecyclerViewAdapter adapter = new PickObjectiveRecyclerViewAdapter(objectives, mListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.invalidate();
    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {

    }

    @Override
    public void onObjectiveCreated() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b =  new  AlertDialog.Builder(getActivity())
                .setTitle(mTitle)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        LayoutInflater i = getActivity().getLayoutInflater();
        View view = (LinearLayout) i.inflate(R.layout.fragment_objective_list_dialog, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        b.setView(view);
        return b.create();
    }

}

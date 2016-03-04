package com.project.ece150.scavenger;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.project.ece150.scavenger.remote.IRemoteClientObserver;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ObjectiveListDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ObjectiveListDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObjectiveListDialogFragment extends DialogFragment
        implements IRemoteClientObserver{

    private ObjectivesFragment.OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private String mTitle;

    public ObjectiveListDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ObjectiveListDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ObjectiveListDialogFragment newInstance() {
        ObjectiveListDialogFragment fragment = new ObjectiveListDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = (LinearLayout) inflater.inflate(R.layout.fragment_objective_list_dialog, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        getDialog().setTitle(mTitle);
        return view;
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
        ActiveObjectiveRecyclerViewAdapter adapter = new ActiveObjectiveRecyclerViewAdapter(objective);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.invalidate();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

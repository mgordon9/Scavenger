package com.project.ece150.scavenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.project.ece150.scavenger.remote.EObjectiveConfirmedType;
import com.project.ece150.scavenger.remote.IRemoteClientObserver;

import java.io.FileOutputStream;
import java.util.List;


public class ActiveObjectiveDialogFragment extends DialogFragment
        implements IRemoteClientObserver,
        View.OnClickListener{

    private ObjectivesFragment.OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private String mTitle;
    private Button mCompareButton;

    public ActiveObjectiveDialogFragment() {
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

    }

    @Override
    public void onObjectiveGetReceived(IObjective objective) {
        ActiveObjectiveRecyclerViewAdapter adapter = new ActiveObjectiveRecyclerViewAdapter(objective, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.invalidate();
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
        View view = (LinearLayout) i.inflate(R.layout.fragment_active_objective_dialog, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        b.setView(view);
        return b.create();
    }

    @Override
    public void onClick(View v) {
        Bitmap image = ((ActiveObjectiveRecyclerViewAdapter)mRecyclerView.getAdapter()).getValue().getImage();

        try {
            //Write file
            String filename = "bitmap.png";
            FileOutputStream stream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);

            //Cleanup
            stream.close();
            image.recycle();

            //Pop intent
            Intent in1 = new Intent(getActivity(), ConfirmationActivity.class);
            in1.putExtra("image", filename);
            getActivity().startActivityForResult(in1, MainActivity.CONFIRM_OBJECTIVE_REQUEST);
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

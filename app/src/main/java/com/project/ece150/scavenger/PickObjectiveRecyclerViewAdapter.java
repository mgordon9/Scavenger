package com.project.ece150.scavenger;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IObjective} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class PickObjectiveRecyclerViewAdapter extends RecyclerView.Adapter<PickObjectiveRecyclerViewAdapter.ViewHolder> {

    private final HashMap<Integer, IObjective> mDistanceObjectiveMap;
    private final List<Integer> mSortedDist;
    private final OnListFragmentInteractionListener mListener;

    public PickObjectiveRecyclerViewAdapter(List<IObjective> items, OnListFragmentInteractionListener listener) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                MapFragment.mGoogleApiClient);
        HashMap unsortedMap = new HashMap();
        List<Integer> sortedDistances = new ArrayList<Integer>();

        for (IObjective item: items) {
            double lat = Math.abs(item.getLatitude() - location.getLatitude());
            double longitude = Math.abs(item.getLongitude() - location.getLongitude());
            double distance = Math.sqrt(lat * lat + longitude * longitude);
            int distanceMeters = (int)(distance * 111320.0);
            unsortedMap.put(distanceMeters,item);
        }

        SortedSet<Integer> sortedKeys = new TreeSet(unsortedMap.keySet());
        Iterator it = sortedKeys.iterator();
        while (it.hasNext()) {
            sortedDistances.add((Integer) it.next());
        }
        mSortedDist = sortedDistances;
        mDistanceObjectiveMap = unsortedMap;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pick_fragment_objective, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mDistanceObjectiveMap.get(mSortedDist.get(position));
        holder.mThumbnail.setImageBitmap( mDistanceObjectiveMap.get(mSortedDist.get(position)).getThumbnail());
        holder.mContentView.setText(mDistanceObjectiveMap.get(mSortedDist.get(position)).getTitle()
                        + "\nWithin: " + mSortedDist.get(position) + " Meters"
                        + "\n" + mDistanceObjectiveMap.get(mSortedDist.get(position)).getInfo());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDistanceObjectiveMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mThumbnail;
        public final TextView mContentView;
        public IObjective mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mThumbnail = (ImageView)view.findViewById(R.id.objective_thumb_nail);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
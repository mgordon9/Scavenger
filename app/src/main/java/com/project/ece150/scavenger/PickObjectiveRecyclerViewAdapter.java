package com.project.ece150.scavenger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IObjective} and makes a call to the
 * specified {@link ObjectivesFragment.OnListFragmentInteractionListener}.
 */
public class PickObjectiveRecyclerViewAdapter extends RecyclerView.Adapter<PickObjectiveRecyclerViewAdapter.ViewHolder> {

    private final List<IObjective> mValues;
    private final ObjectivesFragment.OnListFragmentInteractionListener mListener;

    public PickObjectiveRecyclerViewAdapter(List<IObjective> items, ObjectivesFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
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
        holder.mItem = mValues.get(position);
        holder.mThumbnail.setImageBitmap(mValues.get(position).getThumbnail());
        holder.mContentView.setText(mValues.get(position).getTitle() + "\n" + mValues.get(position).getInfo());

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
        return mValues.size();
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

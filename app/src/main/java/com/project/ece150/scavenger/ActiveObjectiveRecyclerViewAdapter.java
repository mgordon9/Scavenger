package com.project.ece150.scavenger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IObjective} and makes a call to the
 * specified {@link ObjectivesFragment.OnListFragmentInteractionListener}.
 */
public class ActiveObjectiveRecyclerViewAdapter extends RecyclerView.Adapter<ActiveObjectiveRecyclerViewAdapter.ViewHolder> {

    private final IObjective mValue;
    View.OnClickListener mListener;

    public ActiveObjectiveRecyclerViewAdapter(IObjective item, View.OnClickListener listener) {
        mValue = item;
        mListener = listener;
    }

    public IObjective getValue() { return mValue; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_objective, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValue;
        holder.mPicture.setImageBitmap(mValue.getImage());
        holder.mContentView.setText(mValue.getTitle() + "\n" + mValue.getInfo());

        holder.mButton.setClickable(true);
        holder.mButton.setOnClickListener(mListener);

    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mPicture;
        public final TextView mContentView;
        public IObjective mItem;
        private Button mButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPicture = (ImageView)view.findViewById(R.id.objective_picture);
            mContentView = (TextView) view.findViewById(R.id.info);
            mButton = (Button) view.findViewById(R.id.comparePictureButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

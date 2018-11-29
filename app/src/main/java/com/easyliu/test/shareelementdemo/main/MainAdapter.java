package com.easyliu.test.shareelementdemo.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.easyliu.test.shareelementdemo.R;
import com.easyliu.test.shareelementdemo.utils.Utils;

import java.util.List;

/**
 * @author easyliu
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MainAdapter(List<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        Utils.updateView(holder.mContentView, holder.mContentView.getContext(), holder.mItem, false, R.drawable.ic_launcher_background, null);
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onListFragmentInteraction(position,holder.mContentView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mContentView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mContentView = (ImageView) view;
        }

        @Override
        public String toString() {
            return super.toString() + mItem;
        }
    }
}

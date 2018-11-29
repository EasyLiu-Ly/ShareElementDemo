package com.easyliu.test.shareelementdemo.second;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.easyliu.test.shareelementdemo.R;
import com.easyliu.test.shareelementdemo.utils.Utils;

import java.util.ArrayList;

/**
 * @author easyliu
 */
public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.MyViewHolder> {
    private ArrayList<String> mArrayList;

    public SecondAdapter(ArrayList<String> arrayList) {
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_second, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Utils.updateView(holder.mImageView, holder.mImageView.getContext(), mArrayList.get(position),
                false, R.drawable.ic_launcher_background, null);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_transition);
        }
    }
}


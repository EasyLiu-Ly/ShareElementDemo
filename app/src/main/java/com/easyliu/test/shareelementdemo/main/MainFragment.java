package com.easyliu.test.shareelementdemo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyliu.test.shareelementdemo.DataGenerator;
import com.easyliu.test.shareelementdemo.utils.Constant;
import com.easyliu.test.shareelementdemo.R;
import com.easyliu.test.shareelementdemo.second.SecondActivity;
import com.easyliu.test.shareelementdemo.utils.Utils;

/**
 * @author easyliu
 */
public class MainFragment extends Fragment implements OnListFragmentInteractionListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RecyclerView mRecyclerView;

    public static MainFragment newInstance(int columnCount) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setAdapter(new MainAdapter(DataGenerator.ITEMS, this));
        }
        return view;
    }

    @Override
    public void onListFragmentInteraction(int position, View transitionView) {
        Intent intent = new Intent(getActivity(), SecondActivity.class);
        ActivityOptionsCompat activityOptions = null;
        if (transitionView != null) {
            ViewCompat.setTransitionName(transitionView, DataGenerator.ITEMS.get(position));
            Pair pair = Pair.create(transitionView, ViewCompat.getTransitionName(transitionView));
            activityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), pair);
            Utils.setSharedElementExitTransition(getActivity());
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.JUMP_POSITION, position);
        bundle.putStringArrayList(Constant.IMG_URLS, DataGenerator.ITEMS);
        intent.putExtras(bundle);
        Utils.startActivityForResult(getActivity(), intent, activityOptions, 1, true);
    }
}

package com.easyliu.test.shareelementdemo.main;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.easyliu.test.shareelementdemo.BaseActivity;
import com.easyliu.test.shareelementdemo.R;
import com.easyliu.test.shareelementdemo.utils.Constant;

import java.util.List;
import java.util.Map;

/**
 * @author easyliu
 */
public class MainActivity extends BaseActivity {

    private Bundle mTmpReenterState;
    private int mCurrentPosition;
    private String mTransitionName;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTransition();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainFragment = MainFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().
                add(R.id.fl_main_content, mMainFragment).commitAllowingStateLoss();
    }

    private void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                }

                @Override
                public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                }

                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    super.onMapSharedElements(names, sharedElements);
                    if (mTmpReenterState != null) {
                        mTmpReenterState = null;
                        names.clear();
                        sharedElements.clear();
                        names.add(mTransitionName);
                        if (getTransitionRecyclerView() != null) {
                            View transitionView = getTransitionRecyclerView().getLayoutManager().findViewByPosition(mCurrentPosition);
                            if (transitionView != null) {
                                sharedElements.put(mTransitionName, transitionView);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        onActivityReenter(this, resultCode, data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResult(this, resultCode, data);
    }

    public void onActivityResult(FragmentActivity activity, int resultCode, Intent data) {
        onRecyclerViewReenter(activity, resultCode, data);
    }

    public void onActivityReenter(FragmentActivity activity, int resultCode, Intent data) {
        onRecyclerViewReenter(activity, resultCode, data);
    }

    private RecyclerView getTransitionRecyclerView() {
        if (mMainFragment != null) {
            return mMainFragment.getRecyclerView();
        }
        return null;
    }

    private void onRecyclerViewReenter(final FragmentActivity activity, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && getTransitionRecyclerView() != null) {
            try {
                mTmpReenterState = new Bundle(data.getExtras());
                mCurrentPosition = mTmpReenterState.getInt(Constant.RETURN_POSITION);
                mTransitionName = mTmpReenterState.getString(Constant.TRANSITION_NAME);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getTransitionRecyclerView().getLayoutManager();
                linearLayoutManager.scrollToPositionWithOffset(mCurrentPosition, 0);
                activity.supportPostponeEnterTransition();
                getTransitionRecyclerView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        try {
                            getTransitionRecyclerView().getViewTreeObserver().removeOnPreDrawListener(this);
                            getTransitionRecyclerView().requestLayout();
                            activity.supportStartPostponedEnterTransition();
                        } catch (Exception e) {
                        }
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }

}

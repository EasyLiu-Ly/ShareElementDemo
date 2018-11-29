package com.easyliu.test.shareelementdemo.second;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.easyliu.test.shareelementdemo.BaseActivity;
import com.easyliu.test.shareelementdemo.CallbackPagerSnapHelper;
import com.easyliu.test.shareelementdemo.DataGenerator;
import com.easyliu.test.shareelementdemo.R;
import com.easyliu.test.shareelementdemo.utils.Constant;
import com.easyliu.test.shareelementdemo.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author easyliu
 */
public class SecondActivity extends BaseActivity {
    private int mPlayingPosition;
    private int mComingPosition;
    private String mComingUrl;
    private ImageView mImageView;
    private RelativeLayout mTransitionLayout;
    private boolean mIsReturning = false;
    private ArrayList<String> mArrayList;
    /**
     * 是否已经执行过了转场动画,防止多次执行
     */
    private boolean mHasShowTransition;

    /**
     * 转场超时的runnable
     */
    private Runnable mTransitionTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            mImageView.setImageResource(R.drawable.ic_launcher_background);
            startActivityTransition();
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        }
        supportPostponeEnterTransition();
        Utils.setSharedElementEnterTransition(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getExtraData();
        startTransition();
    }

    private void getExtraData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mComingPosition = bundle.getInt(Constant.JUMP_POSITION);
            mArrayList = bundle.getStringArrayList(Constant.IMG_URLS);
            mPlayingPosition = mComingPosition;
            mComingUrl = DataGenerator.ITEMS.get(mComingPosition);
        }
    }

    private void startTransition() {
        mImageView = findViewById(R.id.iv_transition);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImageView.setTransitionName(mComingUrl);
        }
        mTransitionLayout = findViewById(R.id.rl_transition);
        Utils.updateView(mImageView, this, mComingUrl, false, R.drawable.ic_launcher_background, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                startActivityTransition();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                startActivityTransition();
                return false;
            }
        });
        //设置超时，防止当图片没有加载出来的时候转场卡住
        mHandler.postDelayed(mTransitionTimeOutRunnable, 400);
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                //在不是ViewPager的情况下是有这个end回调的,这个回调会比较快
                if (!mIsReturning) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initRecyclerView();
                        }
                    }, 100);
                }
            }

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                //更新共享元素,这样返回的时候动画就能连续
                if (mIsReturning && mRecyclerView != null) {
                    names.clear();
                    names.add(mArrayList.get(mPlayingPosition));
                    sharedElements.clear();
                    View view = mRecyclerView.getLayoutManager().findViewByPosition(mPlayingPosition).findViewById(R.id.iv_transition);
                    if (view != null) {
                        sharedElements.put(mArrayList.get(mPlayingPosition), view);
                    }
                }
            }

            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        CallbackPagerSnapHelper callbackPagerSnapHelper = new CallbackPagerSnapHelper();
        callbackPagerSnapHelper.setPagerToPositionListener(new CallbackPagerSnapHelper.IPagerToPositionListener() {
            @Override
            public void pagerToPosition(int position) {
                mPlayingPosition = position;
            }
        });
        callbackPagerSnapHelper.attachToRecyclerView(mRecyclerView);
        SecondAdapter adapter = new SecondAdapter(mArrayList);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mPlayingPosition);
        if (mTransitionLayout != null) {
            mTransitionLayout.setVisibility(View.GONE);
        }
    }

    private void startActivityTransition() {
        if (!mHasShowTransition) {
            mHasShowTransition = true;
            mHandler.removeCallbacks(mTransitionTimeOutRunnable);
            supportStartPostponedEnterTransition();
        }
    }

    @Override
    public void finishAfterTransition() {
        setResult(Activity.RESULT_OK, getBundleData());
        super.finishAfterTransition();
    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setResult(Activity.RESULT_OK, getBundleData());
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        mIsReturning = true;
        super.onBackPressed();
    }

    private Intent getBundleData() {
        Intent data = new Intent();
        data.putExtra(Constant.RETURN_POSITION, mPlayingPosition);
        data.putExtra(Constant.TRANSITION_NAME, mArrayList.get(mPlayingPosition));
        return data;
    }

}

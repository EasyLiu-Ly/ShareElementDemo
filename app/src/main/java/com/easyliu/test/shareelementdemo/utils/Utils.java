package com.easyliu.test.shareelementdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.ChangeBounds;
import android.transition.TransitionSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * @author easyliu
 * Created by easyliu on 2018/11/28 20:19.
 * easyliu@tencent.com
 * Copyright (c) 2018 Tencent. All rights reserved.
 */
public class Utils {

    public static void updateView(final ImageView imageView, final Context context, final String url, final boolean isCircle, final int defaultResId, final RequestListener<Drawable> requestListener) {
        RequestOptions requestOptions;
        if (isCircle) {
            requestOptions = RequestOptions.circleCropTransform();
        } else {
            requestOptions = new RequestOptions();
        }
        requestOptions = requestOptions.placeholder(defaultResId).dontAnimate();
        Glide.with(context).load(url).apply(requestOptions.set(GifOptions.DECODE_FORMAT, DecodeFormat.PREFER_RGB_565))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (requestListener != null) {
                            requestListener.onLoadFailed(e, model, target, isFirstResource);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (requestListener != null) {
                            requestListener.onResourceReady(resource, model, target, dataSource, isFirstResource);
                        }
                        return false;
                    }
                }).into(imageView);
    }


    public static void setSharedElementExitTransition(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet transitionSet = new TransitionSet();
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(250);
            changeBounds.setInterpolator(new FastOutSlowInInterpolator());
            transitionSet.addTransition(changeBounds);
            activity.getWindow().setSharedElementExitTransition(transitionSet);
            activity.getWindow().setSharedElementReenterTransition(transitionSet);
        }
    }

    public static void setSharedElementEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet transitionSet = new TransitionSet();
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setDuration(250);
            changeBounds.setInterpolator(new FastOutSlowInInterpolator());
            transitionSet.addTransition(changeBounds);
            activity.getWindow().setSharedElementEnterTransition(transitionSet);
            activity.getWindow().setSharedElementReturnTransition(transitionSet);
        }
    }

    public static void startActivityForResult(Activity activity, Intent intent, ActivityOptionsCompat activityOptionsCompat, int requestCode, boolean forResult) {
        //转场动画支持的最低版本以上才走这种方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activityOptionsCompat != null) {
            ActivityCompat.startActivity(activity, intent, activityOptionsCompat.toBundle());
        } else {
            if (forResult) {
                //转场动画支持的最低版本以下走这种方式
                activity.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivity(intent);
            }
        }
    }
}

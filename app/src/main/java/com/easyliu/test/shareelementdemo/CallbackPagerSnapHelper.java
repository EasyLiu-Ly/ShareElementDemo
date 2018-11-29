package com.easyliu.test.shareelementdemo;

import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

/**
 * @author easyliu
 */
public class CallbackPagerSnapHelper extends PagerSnapHelper {
    private IPagerToPositionListener mPagerToPositionListener;

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        if (mPagerToPositionListener != null && position != RecyclerView.NO_POSITION) {
            mPagerToPositionListener.pagerToPosition(position);
            return position;
        }
        return position;
    }

    public void setPagerToPositionListener(IPagerToPositionListener pagerToPositionListener) {
        mPagerToPositionListener = pagerToPositionListener;
    }

    public interface IPagerToPositionListener {
        void pagerToPosition(int position);
    }
}

package com.wzt.ui.banner.core;

import android.content.Context;
import android.widget.Scroller;

/**
 * Created by Kyrie
 * Date: 2020/7/18
 */
public class HiBannerScroller extends Scroller {
    private int mDuration;
    public HiBannerScroller(Context context,int duration) {
        super(context);
        mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}

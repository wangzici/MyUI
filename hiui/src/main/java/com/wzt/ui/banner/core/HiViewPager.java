package com.wzt.ui.banner.core;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * Created by Kyrie
 * Date: 2020/7/17
 * 实现自动翻页的ViewPager
 */
public class HiViewPager extends ViewPager {
    /**
     * 时间间隔
     */
    private int mIntervalTime;

    /**
     * 是否开启自动轮播
     */
    private boolean mAutoPlay = true;
    private boolean isLayout;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //切换到下一个
            next();
            mHandler.postDelayed(this, mIntervalTime);
        }
    };

    public HiViewPager(@NonNull Context context) {
        super(context);
    }

    public HiViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIntervalTime(int intervalTime) {
        this.mIntervalTime = intervalTime;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (!mAutoPlay) {
            mHandler.removeCallbacks(mRunnable);
        }
        //若已经在界面显示时调用setAutoPlay，直接开始自动轮播
        if (mAutoPlay && isAttachedToWindow()) {
            start();
        }
    }

    public void stop() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private void start() {
        mHandler.removeCallbacksAndMessages(null);
        if (mAutoPlay) {
            mHandler.postDelayed(mRunnable, mIntervalTime);
        }
    }

    /**
     * 设置下一个要显示的item，并返回item的pos
     * @return 下一个要显示item的pos
     */
    private int next() {
        int nextPosition = -1;
        if (getAdapter() == null || getAdapter().getCount() <= 1) {
            stop();
            return nextPosition;
        }
        nextPosition = getCurrentItem() + 1;
        if (nextPosition >= getAdapter().getCount()) {
            nextPosition = ((HiBannerAdapter) getAdapter()).getFirstItem();
        }
        setCurrentItem(nextPosition, true);
        return nextPosition;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                start();
                break;
            case MotionEvent.ACTION_DOWN:
                stop();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (((Activity) getContext()).isFinishing()) {
            //ViewPager典型问题2：若从RecycleView等控件被回收后，正在播放的动画会被中断
            //解决办法：只在Activity真的被finishing时才去调用
            super.onDetachedFromWindow();
        }
        stop();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //ViewPager典型问题1：若从RecycleView等控件被回收后，重新绑定第一次滚动会无动画
        if (isLayout && getAdapter() != null && getAdapter().getCount() > 0) {
            try {
                Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
                mFirstLayout.setAccessible(true);
                mFirstLayout.set(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        start();
    }
}

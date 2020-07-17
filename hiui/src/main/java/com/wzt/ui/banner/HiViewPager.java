package com.wzt.ui.banner;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

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
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //切换到下一个
            int nextItem = next();
            setCurrentItem(nextItem);
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
    }

    public void stop() {
        mHandler.removeCallbacksAndMessages(null);
        mAutoPlay = false;
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
            // TODO: 2020/7/17 获取第一个item的索引
            nextPosition = 0;
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
}

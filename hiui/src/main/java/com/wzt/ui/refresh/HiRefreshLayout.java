package com.wzt.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Kyrie
 * Date: 2020/7/15
 */
public class HiRefreshLayout extends FrameLayout implements HiRefresh {
    private HiOverView.HiRefreshState mState;
    private GestureDetector mGestureDetector;
    private HiRefresh.HiRefreshListener mHiRefreshListener;
    protected HiOverView mHiOverView;
    private int mLastY;
    //刷新时是否禁止滚动
    private boolean disableRefreshScroll;
    private AutoScroll mAutoScroller;

    public HiRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), hiGestrueListener);
        mAutoScroller = new AutoScroll();
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void refreshFinished() {
        View head = getChildAt(0);
        mHiOverView.onFinish();
        mHiOverView.setState(HiOverView.HiRefreshState.STATE_INIT);
        int bottom = head.getBottom();
        if (bottom > 0) {
            recover(bottom);
        }
        mState = HiOverView.HiRefreshState.STATE_INIT;
    }

    @Override
    public void setRefreshListener(HiRefreshListener hiRefreshListener) {
        this.mHiRefreshListener = hiRefreshListener;
    }

    @Override
    public void setRefreshOverView(HiOverView hiOverView) {
        if (mHiOverView != null) {
            removeView(mHiOverView);
        }
        mHiOverView = hiOverView;
        //会占用一个屏幕的大小
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mHiOverView, 0, params);
    }

    HiGestrueListener hiGestrueListener = new HiGestrueListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //发生横向滑动或禁止了滑动，则不进行处理
            if (Math.abs(distanceX) > Math.abs(distanceY) || mHiRefreshListener != null && !mHiRefreshListener.enableRefresh()) {
                return false;
            }
            if (disableRefreshScroll && mState == HiOverView.HiRefreshState.STATE_REFRESH) {//正在刷新时禁止滚动，则直接不进行处理消费事件
                return true;
            }
            View head = getChildAt(0);
            View child = HiScrollUtil.findScrollableChild(HiRefreshLayout.this);
            if (HiScrollUtil.childScrolled(child)) {
                //若内部的列表已经发生了滚动，则不进行其它处理
                return false;
            }
            //没有刷新,且为下拉 或者 在头部已经划出的时候上拉
            if (mState != HiOverView.HiRefreshState.STATE_REFRESH && (distanceY < 0 || head.getBottom() > 0)) {
                //不在回弹的状态
                if (mState != HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                    int seed;
                    //计算速度
                    if (head.getBottom() < mHiOverView.mPullRefreshHeight) {
                        // 这里的LastY在直接替换成distanceY之后也可以的样子
                        seed = (int) (mLastY / mHiOverView.minDamp);
                    } else {
                        seed = (int) (mLastY / mHiOverView.maxDamp);
                    }

                    boolean bool = moveDown(seed, true);
                    mLastY = (int) -distanceY;
                    return bool;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    /**
     * 根据偏移量移动header与child
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动触发
     * @return 是否消费该事件
     */
    private boolean moveDown(int offsetY, boolean nonAuto) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;
        if (childTop <= 0) {//异常情况补充
            offsetY = -childTop;
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {
                mState = HiOverView.HiRefreshState.STATE_INIT;
            }
        } else if (mState == HiOverView.HiRefreshState.STATE_REFRESH && childTop > mHiOverView.mPullRefreshHeight) {
            //正在下拉刷新中，禁止继续下拉
            return false;
        } else if (childTop <= mHiOverView.mPullRefreshHeight) {
            if (mHiOverView.getState() != HiOverView.HiRefreshState.STATE_VISIBLE && nonAuto) {
                mHiOverView.onVisible();
                mHiOverView.setState(HiOverView.HiRefreshState.STATE_VISIBLE);
                mState = HiOverView.HiRefreshState.STATE_VISIBLE;
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == mHiOverView.mPullRefreshHeight && mState == HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                refresh();
            }
        }else {
            if (mHiOverView.getState() != HiOverView.HiRefreshState.STATE_OVER && nonAuto) {
                //超出刷新位置
                mHiOverView.onOver();
                mHiOverView.setState(HiOverView.HiRefreshState.STATE_OVER);
            }
            head.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        }
        if (mHiOverView != null) {
            mHiOverView.onScroll(offsetY, mHiOverView.mPullRefreshHeight);
        }
        return true;
    }

    private void refresh() {
        if (mHiRefreshListener != null) {
            mState = HiOverView.HiRefreshState.STATE_REFRESH;
            mHiOverView.onRefresh();
            mHiOverView.setState(HiOverView.HiRefreshState.STATE_REFRESH);
            mHiRefreshListener.onRefresh();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View head = getChildAt(0);
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            //松开手
            if (head.getBottom() > 0) {
                if (mState != HiOverView.HiRefreshState.STATE_REFRESH) {//非正在刷新状态
                    recover(head.getBottom());
                    // TODO: 2020/7/15 为何需要返回false
                    return false;
                }
            }
            //松开手之后把mLastY重置为0
            mLastY = 0;
        }
        //把其它触摸事件都交给GestureDetector处理
        boolean consumed = mGestureDetector.onTouchEvent(ev);
        if (head.getBottom() > 0 &&
                (consumed || (mState != HiOverView.HiRefreshState.STATE_INIT && mState != HiOverView.HiRefreshState.STATE_REFRESH))) {
            ev.setAction(MotionEvent.ACTION_CANCEL);//目的是为了让父类接收不到真实的事件
            return super.dispatchTouchEvent(ev);
        }
        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    /**
     * 回弹
     * @param dis 回弹距离
     */
    private void recover(int dis) {
        if (mHiRefreshListener != null && dis > mHiOverView.mPullRefreshHeight) {
            //滚动到指定位置
            mAutoScroller.recover(dis - mHiOverView.mPullRefreshHeight);
            mState = HiOverView.HiRefreshState.STATE_OVER_RELEASE;
        } else {
            mAutoScroller.recover(dis);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View head = getChildAt(0);
        View child = getChildAt(1);
        if (head != null && child != null) {
            int childTop = child.getTop();
            if (mState == HiOverView.HiRefreshState.STATE_REFRESH) {
                head.layout(left, mHiOverView.mPullRefreshHeight - head.getMeasuredHeight(), right, mHiOverView.mPullRefreshHeight);
                child.layout(left, mHiOverView.mPullRefreshHeight, right, mHiOverView.mPullRefreshHeight + child.getMeasuredHeight());
            } else {
                head.layout(left, childTop - head.getMeasuredHeight(), right, childTop);
                child.layout(left, childTop, right, childTop + child.getMeasuredHeight());
            }
            View other;
            for (int i = 2; i < getChildCount(); i++) {
                other = getChildAt(i);
                other.layout(left, top, right, bottom);
            }
        }
    }

    private class AutoScroll implements Runnable {
        private Scroller mScroller;
        private int mLastY;
        private boolean mIsFinished;

        public AutoScroll() {
            mScroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (mScroller.computeScrollOffset()) {
                moveDown(mLastY - mScroller.getCurrY(), false);
                mLastY = mScroller.getCurrY();
                post(this);//开始下一次滚动
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        /**
         * 开始执行autoScroll的任务
         * @param dis 滚动的总距离
         */
        void recover(int dis) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            mScroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        boolean isFinished() {
            return mIsFinished;
        }
    }

}

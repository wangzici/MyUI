package com.wzt.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wzt.util.HiDisplayUtil;

/**
 * 下拉刷新的OverLay视图，通过重载这个类来定义自己的Overlay
 * Created by Kyrie
 * Date: 2020/7/15
 */
public abstract class HiOverView extends FrameLayout {
    public enum HiRefreshState {
        /**
         * 初始态
         */
        STATE_INIT,
        /**
         * header展示的状态
         */
        STATE_VISIBLE,
        /**
         * 正在刷新
         */
        STATE_REFRESH,
        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER,
        /**
         * 超出刷新位置后松开手后的状态
         */
        STATE_OVER_RELEASE
    }

    protected HiRefreshState mState = HiRefreshState.STATE_INIT;
    /**
     * 触发下拉刷新所需的最小高度
     */
    public int mPullRefreshHeight;
    /**
     * 阻尼系数
     */
    public float minDamp = 1.6f;
    public float maxDamp = 2.2f;
    public HiOverView(@NonNull Context context) {
        super(context);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    private void preInit() {
        mPullRefreshHeight = HiDisplayUtil.dp2px(66, getResources());
        init();
    }

    /**
     * 初始化
     */
    public abstract void init();

    protected abstract void onScroll(int scrollY, int pullRefreshHeight);

    /**
     * 显示Overlay
     */
    protected abstract void onVisible();

    /**
     * 超过OverLay，释放就会加载
     */
    protected abstract void onOver();

    /**
     * 开始刷新
     */
    protected abstract void onRefresh();

    /**
     * 加载完成
     */
    protected abstract void onFinish();

    public void setState(HiRefreshState state) {
        mState = state;
    }

    public HiRefreshState getState() {
        return mState;
    }
}

package com.wzt.ui.banner.core;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.wzt.ui.R;
import com.wzt.ui.banner.HiBanner;
import com.wzt.ui.banner.indicator.HiCircleIndicator;
import com.wzt.ui.banner.indicator.HiIndicator;

import java.util.List;

/**
 * Created by Kyrie
 * Date: 2020/7/18
 * HiBanner的代理，辅助HiBanner完成各种功能的控制，使HiBanner保持整洁
 */
public class HiBannerDelegate implements IHiBanner,ViewPager.OnPageChangeListener{
    private Context mContext;
    private HiBanner mBanner;
    private HiBannerAdapter mAdapter;
    private HiIndicator<?> mHiIndicator;
    private boolean mAutoPlay;
    private boolean mLoop;
    private List<? extends HiBannerMo> mHiBannerMos;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mIntervalTime = 5000;
    private HiBanner.OnBannerClickListener mOnBannerClickListener;
    private HiViewPager mHiViewPager;
    private int mScrollDuration = -1;

    public HiBannerDelegate(Context context, HiBanner hiBanner) {
        this.mContext = context;
        this.mBanner = hiBanner;
    }

    @Override
    public void setBannerData(int layoutResId, @NonNull List<? extends HiBannerMo> models) {
        mHiBannerMos = models;
        init(layoutResId);
    }

    @Override
    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        setBannerData(R.layout.hi_banner_item_image, models);
    }

    @Override
    public void setHiIndicator(HiIndicator<?> hiIndicator) {
        this.mHiIndicator = hiIndicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
        if (mAdapter != null) mAdapter.setAutoPlay(autoPlay);
        if (mHiViewPager != null) mHiViewPager.setAutoPlay(autoPlay);
    }

    @Override
    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            this.mIntervalTime = intervalTime;
        }
    }

    @Override
    public void setBindAdapter(IBindAdapter bindAdapter) {
        mAdapter.setBindAdapter(bindAdapter);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.mOnBannerClickListener = onBannerClickListener;
    }

    @Override
    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
    }

    private void init(int layoutResId) {
        if (mAdapter == null) {
            mAdapter = new HiBannerAdapter(mContext);
        }
        if (mHiIndicator == null) {
            mHiIndicator = new HiCircleIndicator(mContext);
        }
        mAdapter.setLayoutResId(layoutResId);
        mAdapter.setBannerData(mHiBannerMos);
        mAdapter.setAutoPlay(mAutoPlay);
        mAdapter.setLoop(mLoop);
        mAdapter.setBannerClickListener(mOnBannerClickListener);

        mHiViewPager = new HiViewPager(mContext);
        mHiViewPager.setAutoPlay(mAutoPlay);
        mHiViewPager.setIntervalTime(mIntervalTime);
        mHiViewPager.addOnPageChangeListener(this);
        mHiViewPager.setAdapter(mAdapter);

        if ((mLoop || mAutoPlay) && mAdapter.getRealCount() != 0) {
            //为了实现无限轮播，需要让第一个item到达最中间的位置
            int firstItem = mAdapter.getFirstItem();
            mHiViewPager.setCurrentItem(firstItem, false);
        }
        //由于init可能被多次调用
        mBanner.removeAllViews();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBanner.addView(mHiViewPager, params);
        FrameLayout.LayoutParams indicatorParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        indicatorParams.gravity = Gravity.BOTTOM | Gravity.END;
        mBanner.addView(mHiIndicator.get(), params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null && mAdapter.getRealCount() != 0) {
            mOnPageChangeListener.onPageScrolled(position % mAdapter.getRealCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        position = position % mAdapter.getRealCount();
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
        if (mHiIndicator != null) {
            mHiIndicator.onPointChange(position, mAdapter.getRealCount());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}

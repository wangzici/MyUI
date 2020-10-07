package com.wzt.ui.banner.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Created by Kyrie
 * Date: 2020/7/18
 */
public class HiBannerAdapter extends PagerAdapter implements IBindAdapter{
    private Context mContext;
    private SparseArray<HiBannerViewHolder> mCachedViews;
    private List<? extends HiBannerMo> models;
    private IHiBanner.OnBannerClickListener mBannerClickListener;
    private IBindAdapter mBindAdapter;
    private boolean mAutoPlay = true;
    private boolean mLoop = false;
    private int mLayoutResId = -1;

    public HiBannerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        this.models = models;
        initCachedView();
        notifyDataSetChanged();
    }

    public void setAutoPlay(boolean autoPlay) {
        this.mAutoPlay = autoPlay;
    }

    public void setLoop(boolean loop) {
        this.mLoop = loop;
    }

    public void setLayoutResId(@LayoutRes int layoutResId) {
        this.mLayoutResId = layoutResId;
    }

    public void setBannerClickListener(IHiBanner.OnBannerClickListener bannerClickListener) {
        this.mBannerClickListener = bannerClickListener;
    }

    public void setBindAdapter(IBindAdapter bindAdapter) {
        this.mBindAdapter = bindAdapter;
    }

    @Override
    public int getCount() {
        //为了实现无限轮播
        return (mAutoPlay || mLoop) ? Integer.MAX_VALUE : getRealCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }
        HiBannerViewHolder viewHolder = mCachedViews.get(realPosition);
        //如果此view已经被添加过
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        HiBannerMo model = models.get(realPosition);
        onBind(viewHolder, model, realPosition);
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    @Override
    public void onBind(final HiBannerViewHolder viewHolder, final HiBannerMo mo, final int position) {
        if (mBannerClickListener != null) {
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBannerClickListener.onBannerClick(viewHolder, mo, position);
                }
            });
        }
        if (mBindAdapter != null) {
            //交给业务层去绑定，实现解耦
            mBindAdapter.onBind(viewHolder, mo, position);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //这里原本需要手动remove，但是我们缓存起来不需要remove
//        super.destroyItem(container, position, object);
    }

    /**
     * 获取真实页面数量
     */
    public int getRealCount() {
        return models == null ? 0 : models.size();
    }

    /**
     * 获取初次展示界面的位置
     */
    public int getFirstItem() {
        //为了能让我们通过position%getRealCount()来得到当前model的真实位置
        //所以需要第一个item处于正中间，且可以被getRealCount整除
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
    }

    private void initCachedView() {
        mCachedViews = new SparseArray<>();
        for (int i = 0; i < getRealCount(); i++) {
            HiBannerViewHolder viewHolder = new HiBannerViewHolder(createView(LayoutInflater.from(mContext), null));
            mCachedViews.put(i, viewHolder);
        }
    }

    private View createView(LayoutInflater inflater, ViewGroup parent) {
        if (mLayoutResId == -1) {
            throw new IllegalArgumentException("you must be set setLayoutResId first");
        }
        return inflater.inflate(mLayoutResId, parent);
    }

    public static class HiBannerViewHolder {
        //用于保存find到的view
        private SparseArray<View> viewSparseArray;
        View rootView;

        public HiBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public <V extends View> V findViewById(@IdRes int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (this.viewSparseArray == null) {
                viewSparseArray = new SparseArray<>(1);
            }
            V childView = (V) viewSparseArray.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
            }
            return childView;
        }
    }
}

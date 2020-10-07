package com.wzt.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.wzt.ui.R;
import com.wzt.ui.tab.common.IHiTabLayout;
import com.wzt.util.HiDisplayUtil;
import com.wzt.util.HiViewUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kyrie
 * Date: 2020/6/27
 */
public class HiTabBottomLayout extends FrameLayout implements IHiTabLayout<HiTabBottom, HiTabBottomInfo<?>> {
    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";
    List<OnTabSelectedListener<HiTabBottomInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    HiTabBottomInfo<?> selectedInfo = null;
    List<HiTabBottomInfo<?>> infoList;
    private float bottomAlpha = 1f;
    private int tabBottomHeight = 50;
    //头部线条
    private float bottomLineHeight = 0.5f;
    private String bottomLineColor = "#dfe0e1";

    public HiTabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBottomAlpha(float bottomAlpha) {
        this.bottomAlpha = bottomAlpha;
    }

    public void setTabBottomHeight(int tabBottomHeight) {
        this.tabBottomHeight = tabBottomHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    @Override
    public HiTabBottom findTab(@NonNull HiTabBottomInfo<?> data) {
        FrameLayout frameLayout = findViewWithTag(TAG_TAB_BOTTOM);
        final int position = infoList.indexOf(data);
        if (position >= 0) {
            return (HiTabBottom) frameLayout.getChildAt(position);
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabBottomInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        //注意此处是>0，因为第一个为不需要删除的view，如ScrollView等
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
        Iterator<OnTabSelectedListener<HiTabBottomInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabBottom) {
                iterator.remove();
            }
        }
        this.infoList = infoList;
        selectedInfo = null;
        addBackground();
        //此处若使用LinearLayout会在动态修改HiTabBottom高度时导致其gravity的bottom失效
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setTag(TAG_TAB_BOTTOM);
        final int size = infoList.size();
        final int width = HiDisplayUtil.getDisplayWidthInPx(getContext()) / size;
        final int height = HiDisplayUtil.dp2px(tabBottomHeight, getResources());
        for (int i = 0; i < size; i++) {
            final HiTabBottomInfo<?> info = infoList.get(i);
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;
            final HiTabBottom hiTabBottom = new HiTabBottom(getContext());
            tabSelectedChangeListeners.add(hiTabBottom);
            hiTabBottom.setHiTabInfo(info);
            frameLayout.addView(hiTabBottom, params);
            hiTabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
        }
        LayoutParams flParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(frameLayout, flParams);
        fixContentView();
    }

    private void addBottomLine() {
        View view = new View(getContext());
        view.setBackgroundColor(Color.parseColor(bottomLineColor));
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(bottomLineHeight, getResources()));
        params.bottomMargin = HiDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hi_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    private void onSelected(@NonNull HiTabBottomInfo<?> nextInfo) {
        if (nextInfo != selectedInfo) {
            final int index = infoList.indexOf(nextInfo);
            dispatchSelected(index, selectedInfo, nextInfo);
            selectedInfo = nextInfo;
        }
    }

    private void dispatchSelected(int index, HiTabBottomInfo<?> pre, HiTabBottomInfo<?> next) {
        for (OnTabSelectedListener<HiTabBottomInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(index, pre, next);
        }
    }

    /**
     * 修复内容区域的底部Padding
     */
    private void fixContentView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = HiViewUtil.findTypeView(rootView, RecyclerView.class);
        if (targetView == null) {
            targetView = HiViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if (targetView == null) {
            targetView = HiViewUtil.findTypeView(rootView, AbsListView.class);
        }
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
            //需要padding处可以绘制，否则底部padding不会显示
            targetView.setClipToPadding(false);
        }
    }
}

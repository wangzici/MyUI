package com.wzt.ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.wzt.ui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kyrie
 * Date: 2020/7/10
 */
public class HiTabTopLayout extends HorizontalScrollView implements IHiTabLayout<HiTabTop, HiTabTopInfo<?>> {
    private List<HiTabTopInfo<?>> infoList;
    List<OnTabSelectedListener<HiTabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    HiTabTopInfo<?> selectedInfo;

    public HiTabTopLayout(Context context) {
        this(context, null);
    }

    public HiTabTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public HiTabTop findTab(@NonNull HiTabTopInfo<?> data) {
        ViewGroup ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof HiTabTop) {
                HiTabTop tab = (HiTabTop) child;
                if (tab.getHiTabInfo() == data) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabTopInfo<?>> listener) {
        if (!tabSelectedChangeListeners.contains(listener)) {
            tabSelectedChangeListeners.add(listener);
        }
    }

    @Override
    public void defaultSelected(@NonNull HiTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }

        Iterator iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabTop) {
                iterator.remove();
            }
        }
        selectedInfo = null;


        this.infoList = infoList;
        LinearLayout linearLayout = getRootLayout(true);
        for (final HiTabTopInfo<?> info : infoList) {
            HiTabTop hiTabTop = new HiTabTop(getContext());
            hiTabTop.setHiTabInfo(info);
            tabSelectedChangeListeners.add(hiTabTop);
            linearLayout.addView(hiTabTop);
            hiTabTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
        }
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(rootView, layoutParams);
        } else if (clear) {
            rootView.removeAllViews();
        }
        return rootView;
    }

    private void onSelected(@NonNull HiTabTopInfo<?> nextInfo) {
        if (nextInfo != selectedInfo) {
            final int index = infoList.indexOf(nextInfo);
            dispatchSelected(index, selectedInfo, nextInfo);
            selectedInfo = nextInfo;
            autoScroll(selectedInfo);
        }
    }

    int tabWidth;
    /**
     * 自动滚动，实现点击的位置能够自动滚动以展示前后2个
     *
     * @param nextInfo 点击tab的info
     */
    private void autoScroll(HiTabTopInfo<?> nextInfo) {
        HiTabTop hiTabTop = findTab(nextInfo);
        if(hiTabTop == null) return;
        int index = infoList.indexOf(nextInfo);
        int[] loc = new int[2];
        //获取点击控件在屏幕的位置
        hiTabTop.getLocationInWindow(loc);
        if (tabWidth == 0) {
            tabWidth = hiTabTop.getWidth();
        }
        int scrollWidth;
        final boolean isLeft = hiTabTop.getRight() < getWidth() / 2 + tabWidth;
        if (isLeft) {
            scrollWidth = rangeScrollWidth(index, -2);
        } else {
            scrollWidth = rangeScrollWidth(index, 2);
        }
        scrollBy(scrollWidth, 0);
    }

    /**
     * 获取可滚动的范围
     *
     * @param index 从第几个开始
     * @param range 向前向后的范围
     * @return 可滚动的范围
     */
    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i <= Math.abs(range); i++) {
            int next;
            if (range < 0) {
                next = index + range + i;
            } else {
                next = index + range - i;
            }
            if (next >= 0 && next < infoList.size()) {
                scrollWidth += scrollWidth(next, range < 0);
            }
        }
        return scrollWidth;
    }

    /**
     * 指定位置的控件可滚动的距离
     *
     * @param index   指定位置的控件
     * @param toLeft 是否是点击了屏幕右侧
     * @return 可滚动的距离
     */
    private int scrollWidth(int index, boolean toLeft) {
        HiTabTop tabTop = findTab(infoList.get(index));
        if (tabTop == null) {
            return 0;
        }
        Rect rect = new Rect();
        tabTop.getLocalVisibleRect(rect);
        if (toLeft) {
            if (rect.left < -tabWidth) {//完全不可见
                return -tabWidth;
            } else {
                return -rect.left;
            }
        } else {
            if (rect.right > tabWidth) {//完全不可见
                return tabWidth;
            } else {
                return tabWidth - rect.right;
            }
        }
    }

    private void dispatchSelected(int index, HiTabTopInfo<?> pre, HiTabTopInfo<?> next) {
        for (OnTabSelectedListener<HiTabTopInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(index, pre, next);
        }
    }
}

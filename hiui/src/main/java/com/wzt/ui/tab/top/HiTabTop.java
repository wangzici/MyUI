package com.wzt.ui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wzt.ui.R;
import com.wzt.ui.tab.common.IHiTab;

public class HiTabTop extends RelativeLayout implements IHiTab<HiTabTopInfo<?>> {
    HiTabTopInfo<?> tabInfo;
    TextView tabNameView;
    ImageView tabImageView;
    private View indicator;

    public HiTabTop(Context context) {
        this(context, null);
    }

    public HiTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_top, this);
        tabNameView = findViewById(R.id.tv_name);
        tabImageView = findViewById(R.id.iv_image);
        indicator = findViewById(R.id.tab_top_indicator);
    }

    @Override
    public void setHiTabInfo(@NonNull HiTabTopInfo<?> data) {
        tabInfo = data;
        inflateInfo(true, false);
    }

    public HiTabTopInfo<?> getHiTabInfo() {
        return tabInfo;
    }

    /**
     * 改变单个tab的高度
     */
    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        tabNameView.setVisibility(GONE);
    }

    private void inflateInfo(boolean init, boolean selected) {
        if (tabInfo.tabType == HiTabTopInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabNameView.setVisibility(GONE);
            }
            tabImageView.setImageBitmap(selected ? tabInfo.selectedBitmap : tabInfo.defaultBitmap);
            indicator.setVisibility(selected ? VISIBLE : INVISIBLE);
        } else if (tabInfo.tabType == HiTabTopInfo.TabType.TEXT) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabNameView.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                    tabNameView.setVisibility(VISIBLE);
                }
            }
            int color = getTextColor(selected ? tabInfo.selectedColor : tabInfo.defaultColor);
            tabNameView.setTextColor(color);
            indicator.setVisibility(selected ? VISIBLE : INVISIBLE);
        }
    }

    @Override
    public void onTabSelectedChange(int index, @Nullable HiTabTopInfo<?> prevInfo, @NonNull HiTabTopInfo<?> nextInfo) {
        if (prevInfo != nextInfo) {
            if (nextInfo == tabInfo) {
                inflateInfo(false, true);
            } else if (prevInfo == tabInfo) {
                inflateInfo(false, false);
            }
        }
    }

    private int getTextColor(Object color) {
        if (color instanceof Integer) {
            return (int) color;
        } else if (color instanceof String) {
            return Color.parseColor((String) color);
        }
        return -1;
    }
}

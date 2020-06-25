package com.wzt.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wzt.ui.R;
import com.wzt.ui.tab.common.IHiTab;

public class HiTabBottom extends RelativeLayout implements IHiTab<HiTabBottomInfo<?>> {
    HiTabBottomInfo<?> tabInfo;
    TextView tabNameView;
    ImageView tabImageView;
    TextView tabIconView;

    public HiTabBottom(Context context) {
        this(context, null);
    }

    public HiTabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_bottom, this);
        tabNameView = findViewById(R.id.tv_name);
        tabImageView = findViewById(R.id.iv_image);
        tabIconView = findViewById(R.id.tv_icon);
    }

    @Override
    public void setHiTabInfo(@NonNull HiTabBottomInfo<?> data) {
        tabInfo = data;
        inflateInfo(true, false);
    }

    /**
     * 改变单个tab的高度
     */
    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        tabImageView.setVisibility(GONE);
    }

    private void inflateInfo(boolean init, boolean selected) {
        if (tabInfo.tabType == HiTabBottomInfo.TabType.BITMAP) {
            if (init) {
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
                tabImageView.setVisibility(VISIBLE);
            }
            tabImageView.setImageBitmap(selected ? tabInfo.selectedBitmap : tabInfo.defaultBitmap);
        } else if (tabInfo.tabType == HiTabBottomInfo.TabType.ICON) {
            if (init) {
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
                tabIconView.setVisibility(VISIBLE);
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
                tabIconView.setTypeface(typeface);
            }
            tabIconView.setText(selected && !TextUtils.isEmpty(tabInfo.selectedIconName) ? tabInfo.selectedIconName : tabInfo.defaultIconName);
            int color = getTextColor(selected ? tabInfo.selectedColor : tabInfo.defaultColor);
            tabNameView.setTextColor(color);
            tabIconView.setTextColor(color);
        }
    }

    @Override
    public void onTabSelectedChange(int index, @Nullable HiTabBottomInfo<?> prevInfo, @NonNull HiTabBottomInfo<?> nextInfo) {
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

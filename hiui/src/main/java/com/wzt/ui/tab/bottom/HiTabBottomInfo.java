package com.wzt.ui.tab.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

/**
 * 单个底部导航栏对应的实体类
 *
 * @param <Color> 可为Integer或Color类型
 */
public class HiTabBottomInfo<Color> {
    public enum TabType {
        BITMAP,//图标通过Bitmap设置
        ICON//图标通过iconFont设置
    }

    public TabType tabType;
    public Class<? extends Fragment> fragment;
    public String name;

    public Bitmap defaultBitmap;
    public Bitmap selectedBitmap;

    public String iconFont;
    public String defaultIconName;
    public String selectedIconName;
    public Color defaultColor;
    public Color selectedColor;

    public HiTabBottomInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        tabType = TabType.BITMAP;
    }

    public HiTabBottomInfo(String name, String iconFont, String defaultIconName, String selectedIconName, Color defaultColor, Color selectedColor) {
        this.name = name;
        this.iconFont = iconFont;
        this.defaultIconName = defaultIconName;
        this.selectedIconName = selectedIconName;
        this.defaultColor = defaultColor;
        this.selectedColor = selectedColor;
        tabType = TabType.ICON;
    }
}

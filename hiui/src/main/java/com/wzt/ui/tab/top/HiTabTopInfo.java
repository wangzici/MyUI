package com.wzt.ui.tab.top;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

/**
 * 单个顶部导航栏对应的实体类
 *
 * @param <Color> 可为Integer或Color类型
 */
public class HiTabTopInfo<Color> {
    public enum TabType {
        BITMAP,//图标通过Bitmap设置
        TEXT//图标通过iconFont设置
    }

    public TabType tabType;
    public Class<? extends Fragment> fragment;
    public String name;

    public Bitmap defaultBitmap;
    public Bitmap selectedBitmap;

    public Color defaultColor;
    public Color selectedColor;

    public HiTabTopInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        tabType = TabType.BITMAP;
    }

    public HiTabTopInfo(String name, Color defaultColor, Color selectedColor) {
        this.name = name;
        this.defaultColor = defaultColor;
        this.selectedColor = selectedColor;
        tabType = TabType.TEXT;
    }
}

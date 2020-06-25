package com.wzt.ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

/**
 * HiTab对外接口
 */
public interface IHiTab<D> extends IHiTabLayout.OnTabSelectedListener<D>{
    /**
     * 设置HiTab的data
     */
    void setHiTabInfo(@NonNull D data);

    /**
     * 动态修改本HiTab的大小
     */
    void resetHeight(@Px int height);
}

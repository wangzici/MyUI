package com.wzt.ui.banner.indicator;

import android.view.View;

/**
 * Created by Kyrie
 * Date: 2020/7/18
 * 实现该接口来定义所需的样式
 */
public interface HiIndicator<T extends View> {
    T get();

    /**
     * 初始化Indicator
     *
     * @param count 幻灯片数量
     */
    void onInflate(int count);

    /**
     * 幻灯片切换回调
     *
     * @param current 切换到的幻灯片位置
     * @param count   幻灯片数量
     */
    void onPointChange(int current, int count);
}

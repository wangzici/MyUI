package com.wzt.ui.banner.core;

/**
 * Created by Kyrie
 * Date: 2020/7/18
 * HiBanner数据绑定接口，用于实现数据绑定与框架解耦
 * 其实就是把数据和界面的相关操作交给用户自己去进行
 */
public interface IBindAdapter {
    void onBind(HiBannerAdapter.HiBannerViewHolder viewHolder, HiBannerMo mo, int position);
}

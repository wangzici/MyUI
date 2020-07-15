package com.wzt.ui.refresh;

/**
 * Created by Kyrie
 * Date: 2020/7/15
 */
public interface HiRefresh {
    /**
     * 刷新时是否禁止滚动
     * @param disableRefreshScroll
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 刷新完成
     */
    void refreshFinished();

    /**
     * 设置刷新监听器
     * @param hiRefreshListener
     */
    void setRefreshListener(HiRefreshListener hiRefreshListener);

    /**
     * 设置下拉刷新头部view
     * @param hiOverView
     */
    void setRefreshOverView(HiOverView hiOverView);

    interface HiRefreshListener{
        void onRefresh();

        /**
         * 是否启用下拉刷新
         */
        boolean enableRefresh();
    }
}

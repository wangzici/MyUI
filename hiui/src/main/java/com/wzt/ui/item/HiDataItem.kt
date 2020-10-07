package com.wzt.ui.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class HiDataItem<DATA, VH : RecyclerView.ViewHolder>(data: DATA){
    private lateinit var mAdapter: HiAdapter
    var mData: DATA? = null
    init {
        mData = data
    }

    abstract fun onBindData(holder: VH, position: Int)

    fun setAdapter(adapter: HiAdapter) {
        this.mAdapter = adapter
    }

    open fun getLayoutFromRes(): Int {
        return -1
    }

    open fun getItemView(): View? {
        return null
    }

    /**
     * 刷新列表
     */
    fun refreshItem() {
        mAdapter.refreshItem(this)
    }

    /**
     * 从列表上移除
     */
    fun removeItem() {
        mAdapter.removeItem(this)
    }

    /**
     * 该item在列表上占据几列，只可在GridLayoutManager上使用
     */
    fun getSpanSize(): Int {
        return 0
    }
}
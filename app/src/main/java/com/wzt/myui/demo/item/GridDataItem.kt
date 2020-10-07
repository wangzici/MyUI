package com.wzt.myui.demo.item

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wzt.myui.R
import com.wzt.ui.item.HiDataItem

class GridDataItem(data: ItemData) : HiDataItem<ItemData, GridDataItem.MyHolder>(data) {

    override fun onBindData(holder: MyHolder, position: Int) {
        holder.imageView?.setImageResource(R.drawable.fire)
    }

    override fun getLayoutFromRes(): Int {
        return R.layout.layout_list_item_grid
    }

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView? = null

        init {
            imageView = view.findViewById(R.id.item_image)
        }
    }
}
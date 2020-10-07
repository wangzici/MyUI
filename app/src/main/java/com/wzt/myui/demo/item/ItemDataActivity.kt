package com.wzt.myui.demo.item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wzt.myui.R
import com.wzt.ui.item.HiAdapter
import com.wzt.ui.item.HiDataItem
import kotlinx.android.synthetic.main.activity_item_data.*

class ItemDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_data)
        val adapter = HiAdapter(this)
        rv_item_data.adapter = adapter
        rv_item_data.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        val itemList = mutableListOf<HiDataItem<*, out RecyclerView.ViewHolder>>()
        itemList.add(GridDataItem(ItemData()))
        adapter.addItems(itemList, false)
    }
}
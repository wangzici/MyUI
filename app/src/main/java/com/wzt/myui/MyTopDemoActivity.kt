package com.wzt.myui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.wzt.ui.tab.top.HiTabTopInfo
import kotlinx.android.synthetic.main.tab_top_demo.*

/**
 * Created by Kyrie
 * Date: 2020/7/10
 *
 */
class MyTopDemoActivity : AppCompatActivity() {
    private var tabsStr = arrayOf("热门", "服装", "数码", "鞋子", "零食", "家电", "汽车", "百货", "家居", "装修", "运动")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_top_demo)
        initTabTop()
    }

    private fun initTabTop() {
        val defaultColor = ContextCompat.getColor(this, R.color.tabBottomDefaultColor)
        val tintColor = ContextCompat.getColor(this, R.color.tabBottomTintColor)
        val infoList = tabsStr.map {
            HiTabTopInfo(it, defaultColor, tintColor)
        }
        tab_top_layout.inflateInfo(infoList)
        tab_top_layout.addTabSelectedChangeListener { _, _, info ->
            Toast.makeText(this@MyTopDemoActivity, info.name, Toast.LENGTH_SHORT).show()
        }
        tab_top_layout.defaultSelected(infoList[0])
    }
}
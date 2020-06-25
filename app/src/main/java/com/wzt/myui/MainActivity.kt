package com.wzt.myui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wzt.ui.tab.bottom.HiTabBottomInfo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeInfo = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "ffd44949"
        )
        tab_bottom.setHiTabInfo(homeInfo)
    }
}

package com.wzt.myui

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import com.wzt.ui.tab.bottom.HiTabBottomInfo
import com.wzt.util.HiDisplayUtil
import kotlinx.android.synthetic.main.tab_bottom_demo.*

/**
 * Created by Kyrie
 * Date: 2020/6/27
 *
 */
class MyBottomDemoActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_bottom_demo)
        initTabBottom()
    }

    private fun initTabBottom() {
        //TODO: 这里的*意思？
        hitablayout.setBottomAlpha(0.85f)
        val bottomInfoList: MutableList<HiTabBottomInfo<*>> = ArrayList()
        val homeInfo = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoRecommend = HiTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#ff656667",
            "#ffd44949"
        )

/*        val infoCategory = HiTabBottomInfo(
            "分类",
            "fonts/iconfont.ttf",
            getString(R.string.if_category),
            null,
            "#ff656667",
            "#ffd44949"
        )*/
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire)
        val infoCategory = HiTabBottomInfo<Int>("分类", bitmap, bitmap)

        val infoChat = HiTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoProfile = HiTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.if_profile),
            null,
            "#ff656667",
            "#ffd44949"
        )
        bottomInfoList.add(homeInfo)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCategory)
        bottomInfoList.add(infoChat)
        bottomInfoList.add(infoProfile)
        hitablayout.inflateInfo(bottomInfoList)
        hitablayout.defaultSelected(homeInfo)
        hitablayout.addTabSelectedChangeListener { _, _, info ->
            Toast.makeText(this@MyBottomDemoActivity, info.name, Toast.LENGTH_SHORT).show()
        }
        hitablayout.findTab(infoCategory)?.apply {
            resetHeight(HiDisplayUtil.dp2px(66f, resources))
        }
    }
}
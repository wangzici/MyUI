package com.wzt.myui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.wzt.ui.refresh.HiRefresh
import kotlinx.android.synthetic.main.activity_hi_refresh.*

/**
 * Created by Kyrie
 * Date: 2020/7/15
 *
 */
class HiRefreshDemoActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_refresh)
        refresh_layout.setRefreshOverView(HiTextOverView(this))
        refresh_layout.setRefreshListener(object : HiRefresh.HiRefreshListener{
            override fun onRefresh() {
                Handler().postDelayed({ refresh_layout.refreshFinished()}, 1000)
            }

            override fun enableRefresh(): Boolean {
                return true
            }
        })
    }
}
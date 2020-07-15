package com.wzt.myui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import com.wzt.ui.refresh.HiOverView
import kotlinx.android.synthetic.main.hi_refresh_overview.view.*

/**
 * Created by Kyrie
 * Date: 2020/7/15
 *
 */
class HiTextOverView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HiOverView(context, attrs, defStyleAttr) {
    override fun init() {
        LayoutInflater.from(context).inflate(R.layout.hi_refresh_overview, this, true)

    }

    override fun onScroll(scrollY: Int, pullRefreshHeight: Int) {

    }

    override fun onVisible() {
        tv_over_view.text = "下拉刷新"
    }

    override fun onOver() {
        tv_over_view.text = "松开刷新"
    }

    override fun onRefresh() {
        tv_over_view.text = "正在刷新..."
        val operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        iv_rotate.startAnimation(operatingAnim)
    }

    override fun onFinish() {
        iv_rotate.clearAnimation()
    }

}
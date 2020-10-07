package com.wzt.ui.banner.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.get
import com.wzt.ui.R
import com.wzt.util.HiDisplayUtil

/**
 * Created by Kyrie
 * Date: 2020/7/18
 *
 */
class HiCircleIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), HiIndicator<FrameLayout> {

    companion object {
        private const val VWC = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    /**
     * 正常状态下的指示点
     */
    @DrawableRes
    private val mPointNormal = R.drawable.shape_point_normal

    /**
     * 选中状态下的指示点
     */
    @DrawableRes
    private val mPointSelected = R.drawable.shape_point_select

    /**
     * 指示点左右内间距
     */
    private var mPointLeftRightPadding = 0

    /**
     * 指示点上下内间距
     */
    private var mPointTopBottomPadding = 0

    init {
        mPointLeftRightPadding = HiDisplayUtil.dp2px(5f, context.resources)
        mPointTopBottomPadding = HiDisplayUtil.dp2px(15f, context.resources)
    }

    override fun get(): FrameLayout {
        return this
    }

    override fun onInflate(count: Int) {
        removeAllViews()
        if (count < 0) {
            return
        }
        val groupView = LinearLayout(context)
        groupView.orientation = LinearLayout.HORIZONTAL
        for (i in 0 until count){
            val imageView = ImageView(context)
            val imageViewParams = LinearLayout.LayoutParams(VWC, VWC)
            imageViewParams.rightMargin = mPointLeftRightPadding
            imageViewParams.topMargin = mPointTopBottomPadding
            imageViewParams.bottomMargin = mPointTopBottomPadding
            imageViewParams.leftMargin = mPointLeftRightPadding
            imageViewParams.gravity = Gravity.CENTER_VERTICAL
            imageView.setImageResource(if(i == 0)mPointSelected else mPointNormal)
            groupView.addView(imageView, imageViewParams)
        }
        val groupViewParams = LayoutParams(VWC, VWC)
        groupViewParams.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        addView(groupView,groupViewParams)
    }

    override fun onPointChange(current: Int, count: Int) {
        val viewGroup = getChildAt(0) as ViewGroup
        for (i in 0 until count) {
            val imageView = viewGroup.getChildAt(i) as ImageView
            imageView.setImageResource(if(i ==current) mPointSelected else mPointNormal)
        }
    }

}
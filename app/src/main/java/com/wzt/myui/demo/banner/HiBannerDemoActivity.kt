package com.wzt.myui.demo.banner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.wzt.myui.R
import com.wzt.ui.banner.core.HiBannerMo
import kotlinx.android.synthetic.main.activity_hi_banner_demo.*

class HiBannerDemoActivity : AppCompatActivity() {
    private var urls = arrayOf(
        "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera7.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera8.jpeg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_banner_demo)
        initViews()
        auto_play.setOnCheckedChangeListener{ _, isChecked ->
            hi_banner.setAutoPlay(isChecked)
        }
    }

    private fun initViews() {
        val moList: MutableList<HiBannerMo> = ArrayList()
        for (i in 0..7) {
            val mo = BannerMo()
            mo.url = urls[i % urls.size]
            moList.add(mo)
        }
        hi_banner.setAutoPlay(false)
        hi_banner.setIntervalTime(2000)
        hi_banner.setBannerData(R.layout.banner_item_layout, moList)
        hi_banner.setScrollDuration(1000)
        hi_banner.setBindAdapter{ viewHolder, mo, _ ->
            val imageView = viewHolder.findViewById<ImageView>(R.id.iv_image)
            Glide.with(this).load(mo.url).into(imageView)
            val titleView: TextView = viewHolder.findViewById(R.id.tv_title)
            titleView.text = mo.url

        }
    }
}

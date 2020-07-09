package com.wzt.myui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_bottom_layout -> {
                val intent = Intent(this@MainActivity, MyBottomDemoActivity::class.java)
                startActivity(intent)
            }
            R.id.tv_top_layout->{
                val intent = Intent(this@MainActivity, MyTopDemoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_bottom_layout.setOnClickListener(this)
        tv_top_layout.setOnClickListener(this)
    }
}

package com.hloong.frame.tab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hloong.frame.R
import com.hloong.ui.tap.bottom.LongTabBottomInfo
import kotlinx.android.synthetic.main.activity_tab_bottom.*

class TabBottomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_bottom)
        initTabBottom()
    }

    private fun initTabBottom() {
        tabLay.setTabAlpha(0.8f)
        var list = ArrayList<LongTabBottomInfo<*>>()
        val home = LongTabBottomInfo(
            "Home",
            "fonts/iconfont.ttf",
            getString(R.string.app_name),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val recommend=LongTabBottomInfo(
            "Recommend",
            "fonts/iconfont.ttf",
            getString(R.string.app_name),
            null,
            "#ff656667",
            "#ffd44949"
        )
    }
}
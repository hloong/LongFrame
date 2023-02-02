package com.hloong.frame.tab

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hloong.frame.R
import com.hloong.ui.tab.bottom.LongTabBottomInfo
import com.hloong.ui.tab.common.ILongTabLayout
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
//        val home = LongTabBottomInfo(
//            "Home",
//            "fonts/iconfont.ttf",
//            getString(R.string.if_home),
//            null,
//            "#ff656667",
//            "#ffd44949"
//        )
        val bitmap = BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher,null)
        val b = LongTabBottomInfo<String>(
            "分类",bitmap,bitmap
        )
//        val recommend=LongTabBottomInfo(
//            "Recommend",
//            "fonts/iconfont.ttf",
//            getString(R.string.if_home),
//            null,
//            "#ff656667",
//            "#ffd44949"
//        )
        list.add(b)
        list.add(b)
        list.add(b)
        tabLay.inflateInfo(list)
        tabLay.addTabSelectedChangeListener(object:ILongTabLayout.OnTabSelectedListener<LongTabBottomInfo<*>>{
            override fun onTabSelectedChange(
                index: Int,
                prevInfo: LongTabBottomInfo<*>,
                nextInfo: LongTabBottomInfo<*>
            ) {
                Toast.makeText(this@TabBottomActivity,nextInfo.name,Toast.LENGTH_SHORT).show()
            }

        })
    }
}
package com.hloong.frame.tab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hloong.frame.R
import com.hloong.lib.longlog.LongLog
import com.hloong.ui.tab.common.ITabLayout
import com.hloong.ui.tab.top.TabTopInfo
import com.hloong.ui.tab.top.TabTopLayout
import kotlinx.android.synthetic.main.activity_tab_top.*

class TabTopActivity : AppCompatActivity() {
    val tabTitle = arrayListOf<String>(
        "热门","数码","零食","衣服","鞋子","汽车","家具","装修","运动","百货","家电"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_top)

        initTabTop()
    }

    private fun initTabTop() {
        var infoList = ArrayList<TabTopInfo<*>>()
        val defColor = resources.getColor(R.color.tabDefaultColor)
        val tintColor = resources.getColor(R.color.tabTintColor)
        for (s in tabTitle){
            infoList.add(TabTopInfo(s,defColor,tintColor))
        }
        tab_top.inflateInfo(infoList)
        tab_top.addTabSelectedChangeListener(object : ITabLayout.OnTabSelectedListener<TabTopInfo<*>>{
            override fun onTabSelectedChange(
                index: Int,
                prevInfo: TabTopInfo<*>,
                nextInfo: TabTopInfo<*>
            ) {
                LongLog.d("index-->"+index+"--"+prevInfo.name)
            }
        })
        tab_top.defaultSelected(infoList[0])
    }


}
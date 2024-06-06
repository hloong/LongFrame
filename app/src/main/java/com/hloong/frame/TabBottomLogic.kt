package com.hloong.frame

import android.app.ActivityManager
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.fragment.app.FragmentManager
import com.hloong.frame.fragment.HomeFragment
import com.hloong.lib.longlog.LongLog
import com.hloong.lib.util.DisplayUtil
import com.hloong.ui.tab.bottom.LongTabBottomInfo
import com.hloong.ui.tab.bottom.LongTabBottomLayout
import com.hloong.ui.tab.common.ILongTabLayout
import com.hloong.ui.tab.top.FragmentTabView
import com.hloong.ui.tab.top.TabViewAdapter
import kotlinx.android.synthetic.main.activity_tab_bottom.*

open class TabBottomLogic constructor(activityProvider:ActivityProvider) {
    var fragmentTabView:FragmentTabView?=null
    lateinit var activityProvider:ActivityProvider
    var currentItemIndex:Int =0
    lateinit var tabBottomLayout:LongTabBottomLayout
    private var infoList=ArrayList<LongTabBottomInfo<*>>()

    init {
        this.activityProvider = activityProvider
        initTabBottom()
    }

    fun initTabBottom(){
        tabBottomLayout = activityProvider.findViewById(R.id.tabLay)
        tabBottomLayout.setTabAlpha(0.8f)

        val defColor=activityProvider.getResources().getColor(R.color.tabDefaultColor)
        val tintColor = activityProvider.getResources().getColor(R.color.tabTintColor)

        val home = LongTabBottomInfo("Home", "fonts/iconfont.ttf", activityProvider.getString(R.string.if_home),
            null, defColor, tintColor)
        val bitmap = BitmapFactory.decodeResource(activityProvider.getResources(),R.mipmap.ic_launcher,null)
        val b = LongTabBottomInfo<String>("cate",bitmap,bitmap)
        val recommend=LongTabBottomInfo("Recommend", "fonts/iconfont.ttf",
            activityProvider.getString(R.string.if_recommend), null,  defColor, tintColor)

        b.fragment = HomeFragment::class.java
        infoList.add(home)
        infoList.add(recommend)
        infoList.add(b)
        infoList.add(home)
        infoList.add(recommend)

        tabBottomLayout.inflateInfo(infoList)
        initFragmentTabView()
        tabBottomLayout.addTabSelectedChangeListener(object: ILongTabLayout.OnTabSelectedListener<LongTabBottomInfo<*>>{
            override fun onTabSelectedChange(index: Int, prevInfo: LongTabBottomInfo<*>, nextInfo: LongTabBottomInfo<*>) {
                fragmentTabView!!.setCurrentItem(index)
                currentItemIndex = index
            }
        })
        tabBottomLayout.defaultSelected(infoList[currentItemIndex])

//        val tabMiddle = tabBottomLayout.findTab(list[2])
//        tabMiddle?.apply { resetHeight(DisplayUtil.dp2px(66f)) }
    }

    fun initFragmentTabView(){
        var tabViewAdapter = TabViewAdapter(activityProvider.getSupportFragmentManager(),infoList!!)
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view)
        fragmentTabView!!.setAdapter(tabViewAdapter)
    }



}




interface ActivityProvider{
    fun <T : View?> findViewById(@IdRes id: Int): T
    fun getResources():Resources
    fun getSupportFragmentManager():FragmentManager
    fun getString(@StringRes resId:Int):String
}
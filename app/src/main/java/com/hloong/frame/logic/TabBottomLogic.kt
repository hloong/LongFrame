package com.hloong.frame

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.hloong.frame.fragment.*
import com.hloong.ui.tab.bottom.TabBottomInfo
import com.hloong.ui.tab.bottom.TabBottomLayout
import com.hloong.ui.tab.common.ITabLayout
import com.hloong.ui.common.tab.FragmentTabView
import com.hloong.ui.common.tab.TabViewAdapter

open class TabBottomLogic constructor(activityProvider:ActivityProvider) {
    var fragmentTabView: FragmentTabView?=null
    lateinit var activityProvider:ActivityProvider
    var currentItemIndex:Int =0
    lateinit var tabBottomLayout:TabBottomLayout
    private var infoList=ArrayList<TabBottomInfo<*>>()

    init {
        this.activityProvider = activityProvider
        initTabBottom()
    }

    fun initTabBottom(){
        tabBottomLayout = activityProvider.findViewById(R.id.tabLay)
        tabBottomLayout.setTabAlpha(0.8f)

        val defColor=activityProvider.getResources().getColor(R.color.tabDefaultColor)
        val tintColor = activityProvider.getResources().getColor(R.color.tabTintColor)

        val home = TabBottomInfo("Home", "fonts/iconfont.ttf", activityProvider.getString(R.string.if_home), null, defColor, tintColor)
        home.fragment = HomeFragment::class.java

        val bitmap = BitmapFactory.decodeResource(activityProvider.getResources(),R.mipmap.ic_launcher,null)
        val category = TabBottomInfo<String>("cate",bitmap,bitmap)
        category.fragment = CategoryFragment::class.java

        val recommend=TabBottomInfo("Recommend", "fonts/iconfont.ttf", activityProvider.getString(R.string.if_recommend), null,  defColor, tintColor)
        recommend.fragment = RecommendFragment::class.java

        val favorite = TabBottomInfo("Favorite","fonts/iconfont.ttf",activityProvider.getString(R.string.if_favorite),null,defColor,tintColor)
        favorite.fragment = FavoriteFragment::class.java

        val profile = TabBottomInfo("profile","fonts/iconfont.ttf",activityProvider.getString(R.string.if_profile),null,defColor,tintColor)
        profile.fragment = ProfileFragment::class.java

        infoList.add(home)
        infoList.add(recommend)
        infoList.add(category)
        infoList.add(favorite)
        infoList.add(profile)

        tabBottomLayout.inflateInfo(infoList)
        initFragmentTabView()
        tabBottomLayout.addTabSelectedChangeListener(object: ITabLayout.OnTabSelectedListener<TabBottomInfo<*>>{
            override fun onTabSelectedChange(index: Int, prevInfo: TabBottomInfo<*>, nextInfo: TabBottomInfo<*>) {
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
    fun getSupportFragmentManager(): FragmentManager
    fun getString(@StringRes resId:Int):String
}
package com.hloong.ui.tab.top

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.hloong.lib.util.DisplayUtil
import com.hloong.ui.tab.common.ITabLayout
import com.hloong.ui.tab.common.ITabLayout.OnTabSelectedListener
import kotlin.math.abs

open class TabTopLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,defStyleAttr:Int = 0
) : HorizontalScrollView(context, attrs,defStyleAttr), ITabLayout<TabTop?, TabTopInfo<*>> {

    private var tabSelectedChangeListeners = ArrayList<OnTabSelectedListener<TabTopInfo<*>>>()
    private var selectedInfo:TabTopInfo<*>?=null
    private var tabWith:Int = 0

    private var infoList = ArrayList<TabTopInfo<*>>()
    override fun findTab(data: TabTopInfo<*>): TabTop? {
        val ll = getRootLayout(false)
        for (i in 0 until ll.childCount) {
            val child = ll.getChildAt(i)
            if (child is TabTop) {
                val tab: TabTop = child as TabTop
                if (tab.getLongTabInfo() == data) {
                    return tab
                }
            }
        }
        return null
    }


    override fun inflateInfo(infoList: List<TabTopInfo<*>>) {
        if (infoList.isEmpty()){
            return
        }
        this.infoList = infoList as ArrayList<TabTopInfo<*>>
        var linearLayout = getRootLayout(true)
        selectedInfo = null
        //清除之前添加的HiTabBottom listener，Tips：Java foreach remove问题
        val iterator: MutableIterator<OnTabSelectedListener<TabTopInfo<*>>> = tabSelectedChangeListeners.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() is TabTop) {
                iterator.remove()
            }
        }

        for (i in infoList.indices) {
            var info: TabTopInfo<*> = infoList[i]
            //Tips：为何不用LinearLayout：当动态改变child大小后Gravity.BOTTOM会失效
            var tab = TabTop(context)
            tabSelectedChangeListeners.add(tab as OnTabSelectedListener<TabTopInfo<*>>)
            tab.setLongTabInfo(info)
            linearLayout.addView(tab)
            tab.setOnClickListener { onSelected(info) }
        }
    }

    private fun getRootLayout(clear:Boolean):LinearLayout{
        var rootView:LinearLayout ?= null
        if (getChildAt(0) != null){
            rootView = getChildAt(0) as LinearLayout
        }
        if (rootView == null){
            rootView = LinearLayout(context)
            rootView.orientation = LinearLayout.HORIZONTAL
            val flParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
            flParams.gravity = Gravity.CENTER
            addView(rootView, flParams)
        }else if (clear){
            rootView.removeAllViews()
        }
        return rootView
    }


    private fun onSelected(nextInfo: TabTopInfo<*>) {
        for (listener in tabSelectedChangeListeners) {
            try {
                listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo!!, nextInfo)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        selectedInfo = nextInfo
        autoScroll(nextInfo)
    }



    override fun addTabSelectedChangeListener(listener: OnTabSelectedListener<TabTopInfo<*>>?) {
        tabSelectedChangeListeners.add(listener!!)
    }

    override fun defaultSelected(defaultInfo: TabTopInfo<*>) {
        onSelected(defaultInfo)
    }

    private fun autoScroll(nextInfo:TabTopInfo<*>){
        var tabTop = findTab(nextInfo)
        if (tabTop == null) return
        val index = infoList.indexOf(nextInfo)
        var loc = IntArray(2)
        tabTop.getLocationInWindow(loc)
        if (tabWith == 0){
            tabWith = tabTop.width
        }
        var scrollWidth=0
        if ((loc[0]+tabWith/2) > DisplayUtil.getDisplayWidthInPx(context)/2){
            scrollWidth = rangeScrollWidth(index,2)
        }else{
            scrollWidth = rangeScrollWidth(index,-2)
        }
        scrollTo(scrollX+scrollWidth,0)
    }

    private fun rangeScrollWidth(index: Int, range: Int): Int {
        var scrollWidth = 0
        for (i in 0..abs(range)){
            var next:Int = if (range<0){
                range+i+index
            }else{
                range-i+index
            }
            if (next>=0 && next<infoList.size){
                if (range<0){
                    scrollWidth -= scrollWidth(next, false)
                }else{
                    scrollWidth += scrollWidth(next,false)
                }
            }
        }
        return scrollWidth
    }
    private fun scrollWidth(index:Int,toRight:Boolean):Int{
        var target = findTab(infoList.get(index))
        if (target == null ) return 0
        var rect = Rect()
        target.getLocalVisibleRect(rect)
        if (toRight){
            if (rect.right>tabWith){
                return tabWith
            }else{
                return tabWith-rect.right
            }
        }else{
            if (rect.right <= -tabWith){
                return tabWith
            }else if(rect.left > 0){
                return rect.left
            }
            return 0
        }
    }
}

package com.hloong.ui.tab.bottom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import com.hloong.lib.longlog.LongLog
import com.hloong.lib.util.DisplayUtil
import com.hloong.lib.util.ViewUtil
import com.hloong.ui.R
import com.hloong.ui.tab.common.ILongTabLayout
import com.hloong.ui.tab.common.ILongTabLayout.OnTabSelectedListener

open class LongTabBottomLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,defStyleAttr:Int = 0
) : FrameLayout(context, attrs,defStyleAttr), ILongTabLayout<LongTabBottom?, LongTabBottomInfo<*>> {

    private var tabSelectedChangeListeners = ArrayList<OnTabSelectedListener<LongTabBottomInfo<*>>>()
    private var selectedInfo:LongTabBottomInfo<*>?=null
    private var bottomAlpha = 1f
    //TabBottom高度
    private var tabBottomHeight = 50f
    //TabBottom的头部线条高度
    private val bottomLineHeight = 0.5f
    //TabBottom的头部线条颜色
    private val bottomLineColor = "#dfe0e1"
    private var infoList = ArrayList<LongTabBottomInfo<*>>()
    companion object{
        const val TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM"

    }


    override fun findTab(data: LongTabBottomInfo<*>): LongTabBottom? {
        val ll = findViewWithTag<ViewGroup>(TAG_TAB_BOTTOM)
        for (i in 0 until ll.childCount) {
            val child = ll.getChildAt(i)
            if (child is LongTabBottom) {
                val tab: LongTabBottom = child as LongTabBottom
                if (tab.getLongTabInfo() == data) {
                    return tab
                }
            }
        }
        return null
    }


    override fun inflateInfo(infoList: List<LongTabBottomInfo<*>>) {
        if (infoList.isEmpty()){
            return
        }
        this.infoList = infoList as ArrayList<LongTabBottomInfo<*>>
        // 移除之前已经添加的View
        for (i in childCount - 1 downTo 1) {//等价于  int i = getChildCount() - 1; i > 0; i--
            removeViewAt(i)
        }

        selectedInfo = null
        addBackground()
        //清除之前添加的HiTabBottom listener，Tips：Java foreach remove问题
        val iterator: MutableIterator<OnTabSelectedListener<LongTabBottomInfo<*>>> = tabSelectedChangeListeners.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() is LongTabBottom) {
                iterator.remove()
            }
        }

        val ll = FrameLayout(context)
        val width = DisplayUtil.getDisplayWidthInPx(context) / infoList.size
        val height = DisplayUtil.dp2px(tabBottomHeight)
        ll.tag = TAG_TAB_BOTTOM
        for (i in infoList.indices) {
            var info: LongTabBottomInfo<*> = infoList[i]
            //Tips：为何不用LinearLayout：当动态改变child大小后Gravity.BOTTOM会失效
            var params = LayoutParams(width, height)
            params.gravity = Gravity.BOTTOM
            params.leftMargin = i * width
            var tabBottom = LongTabBottom(context)
            tabSelectedChangeListeners.add(tabBottom as OnTabSelectedListener<LongTabBottomInfo<*>>)
            tabBottom.setLongTapInfo(info)
            ll.addView(tabBottom, params)
            tabBottom.setOnClickListener { onSelected(info) }
        }
        val flParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        flParams.gravity = Gravity.BOTTOM
        addBottomLine()
        addView(ll, flParams)
        try {
            fixContentView()
        }catch (e :Exception){
            e.printStackTrace()
        }
    }

    private fun addBottomLine() {
        val bottomLine = View(context)
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor))
        val bottomLineParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(bottomLineHeight, resources))
        bottomLineParams.gravity = Gravity.BOTTOM
        bottomLineParams.bottomMargin = DisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, resources)
        addView(bottomLine, bottomLineParams)
        bottomLine.alpha = bottomAlpha
    }

    private fun onSelected(info: LongTabBottomInfo<*>) {
        for (listener in tabSelectedChangeListeners) {
            try {
                listener.onTabSelectedChange(infoList.indexOf(info), selectedInfo!!, info)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        selectedInfo = info
    }

    private fun addBackground() {
        val view = LayoutInflater.from(context).inflate(R.layout.long_bottom_layout_bg,null)
        val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(tabBottomHeight,resources))
        params.gravity = Gravity.BOTTOM
        addView(view, params)
        view.alpha = bottomAlpha
    }

    override fun addTabSelectedChangeListener(listener: OnTabSelectedListener<LongTabBottomInfo<*>>?) {
        tabSelectedChangeListeners.add(listener!!)
    }

    override fun defaultSelected(defaultInfo: LongTabBottomInfo<*>) {
        onSelected(defaultInfo)
    }
    fun setTabAlpha(alpha:Float){
        this.bottomAlpha = alpha
    }
    fun setTabHeight(height:Float){
        this.tabBottomHeight = height
    }

    fun resetHeight(height: Int){
        val layoutParams = layoutParams
        layoutParams.height = height
        setLayoutParams(layoutParams)

    }

    fun fixContentView(){
        if (getChildAt(0) !is ViewGroup) {
            return
        }
        var rootView = getChildAt(0) as ViewGroup ?: return
        var targetView:ViewGroup?=null
        targetView = ViewUtil.findTypeView(rootView, RecyclerView::class.java)
        if (targetView == null) {
            targetView = ViewUtil.findTypeView(rootView, ScrollView::class.java)
        }
        if (targetView == null) {
            targetView = ViewUtil.findTypeView(rootView, AbsListView::class.java)
        }
        if (targetView != null) {
            targetView.setPadding(
                0, 0, 0, DisplayUtil.dp2px(
                    tabBottomHeight,
                    resources
                )
            )
            targetView.clipToPadding = false
        }

    }


}

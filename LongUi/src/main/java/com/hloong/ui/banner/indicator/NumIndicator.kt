package com.hloong.ui.banner.indicator

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.hloong.lib.util.DisplayUtil

class NumIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0) :
    FrameLayout(context, attrs, defStyleAttr),IIndicator<FrameLayout> {
    val VWC: Int = ViewGroup.LayoutParams.WRAP_CONTENT


    /**
     * 指示点左右内间距
     */
    private var mPointLeftRightPadding = 0

    /**
     * 指示点上下内间距
     */
    private var mPointTopBottomPadding = 0

    init {
        mPointLeftRightPadding = DisplayUtil.dp2px(10f, getContext().resources)
        mPointTopBottomPadding = DisplayUtil.dp2px(10f, getContext().resources)
    }
    override fun get(): FrameLayout {
        return this
    }

    override fun onInflate(count: Int) {
        removeAllViews()
        if (count <= 0) {
            return
        }

        val groupView = LinearLayout(context)
        groupView.orientation = LinearLayout.HORIZONTAL
        groupView.setPadding(0, 0, mPointLeftRightPadding, mPointTopBottomPadding)

        val indexTv = TextView(context)
        indexTv.text = "1"
        indexTv.setTextColor(Color.WHITE)
        groupView.addView(indexTv)

        val symbolTv = TextView(context)
        symbolTv.text = " / "
        symbolTv.setTextColor(Color.WHITE)
        groupView.addView(symbolTv)

        val countTv = TextView(context)
        countTv.text = count.toString()
        countTv.setTextColor(Color.WHITE)
        groupView.addView(countTv)

        val groupViewParams: LayoutParams = LayoutParams(VWC,VWC)
        groupViewParams.gravity = Gravity.END or Gravity.BOTTOM
        addView(groupView, groupViewParams)
    }

    override fun onPointChange(current: Int, count: Int) {
        val viewGroup = getChildAt(0) as ViewGroup
        val indexTv = viewGroup.getChildAt(0) as TextView
        val countTv = viewGroup.getChildAt(2) as TextView
        indexTv.text = (current + 1).toString()
        countTv.text = count.toString()
    }

}
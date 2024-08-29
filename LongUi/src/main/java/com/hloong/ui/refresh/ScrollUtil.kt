package com.hloong.ui.refresh

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.hloong.lib.longlog.LongLog

object ScrollUtil {

    /**
     * 判断child是否发生了滚动
     *
     * @param child
     * @return true 发生了滚动
     */
    fun childScrolled(child: View): Boolean {
        if (child is AdapterView<*>) {
            val adapterView = child
            if (adapterView.firstVisiblePosition != 0
                || adapterView.firstVisiblePosition == 0 && adapterView.getChildAt(0) != null && adapterView.getChildAt(
                    0
                ).top < 0
            ) {
                return true
            }
        } else if (child.scrollY > 0) {
            return true
        }
        if (child is RecyclerView) {
            val recyclerView = child
            val view = recyclerView.getChildAt(0)
            val firstPosition = recyclerView.getChildAdapterPosition(view)
            LongLog.d("----:top", view.top.toString() + "")
            return firstPosition != 0 || view.top != 0
        }
        return false
    }

    /**
     * 查找可以滚动的child
     *
     * @return 可以滚动的child
     */
    fun findScrollableChild(viewGroup: ViewGroup): View {
        var child = viewGroup.getChildAt(1)
        if (child is RecyclerView || child is AdapterView<*>) {
            return child
        }
        if (child is ViewGroup) { //往下多找一层
            val tempChild = child.getChildAt(0)
            if (tempChild is RecyclerView || tempChild is AdapterView<*>) {
                child = tempChild
            }
        }
        return child
    }
}
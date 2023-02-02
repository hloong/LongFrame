package com.hloong.ui.tab.common

import android.view.ViewGroup
import androidx.annotation.Nullable


interface ILongTabLayout<Tab : ViewGroup?, D> {
    fun findTab(data: D): Tab
    fun addTabSelectedChangeListener(listener: OnTabSelectedListener<D>?)
    fun defaultSelected(defaultInfo: D)
    fun inflateInfo(infoList: List<D>)
    interface OnTabSelectedListener<D> {
        fun onTabSelectedChange(index: Int, @Nullable prevInfo: D, nextInfo: D)
    }
}

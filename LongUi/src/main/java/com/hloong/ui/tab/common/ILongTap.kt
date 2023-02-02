package com.hloong.ui.tab.common

import com.hloong.ui.tab.common.ILongTabLayout.OnTabSelectedListener

interface ILongTap<D> : OnTabSelectedListener<D> {
    fun setLongTapInfo(data:D)
    fun resetHeight(height:Int)
}
package com.hloong.ui.tap.common

import com.hloong.ui.tap.common.ILongTabLayout.OnTabSelectedListener

interface ILongTap<D> : OnTabSelectedListener<D> {
    fun setLongTapInfo(data:D)
    fun resetHeight(height:Int)
}
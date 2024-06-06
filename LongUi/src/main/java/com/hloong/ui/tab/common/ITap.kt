package com.hloong.ui.tab.common


interface ITap<D> : ITabLayout.OnTabSelectedListener<D> {
    fun setLongTabInfo(data:D)
    fun resetHeight(height:Int)
}
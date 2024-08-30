package com.hloong.ui.banner.indicator

import android.view.View

interface IIndicator<T : View?> {
    fun get(): T

    /**
     * 初始化Indicator
     *
     * @param count 幻灯片数量
     */
    fun onInflate(count: Int)

    /**
     * 幻灯片切换回调
     *
     * @param current 切换到的幻灯片位置
     * @param count   幻灯片数量
     */
    fun onPointChange(current: Int, count: Int)
}

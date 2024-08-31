package com.hloong.ui.banner.core

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class BannerScroller @JvmOverloads constructor(context: Context?, interpolator: Interpolator?=null, flywheel: Boolean=false) :
    Scroller(context, interpolator, flywheel) {
    /**
     * 值越大，滑动越慢
     */
    private var mDuration = 1000

    constructor(context: Context?,duration: Int) : this(context) {
        mDuration = duration
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, duration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy)
    }
}
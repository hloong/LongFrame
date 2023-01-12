package com.hloong.lib.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.TypedValue
import android.view.WindowManager
import com.hloong.lib.util.AppGlobals.get

object DisplayUtil{
    fun dp2px(dp: Float, resources: Resources): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    fun sp2px(sp: Float, resources: Resources): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            resources.displayMetrics
        ).toInt()
    }

    fun dp2px(dp: Float): Int {
        val resources = get()!!.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    fun sp2px(sp: Float): Int {
        val resources = get()!!.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            resources.displayMetrics
        ).toInt()
    }


    fun getDisplayWidthInPx(context: Context): Int {
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (wm != null) {
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }
        return 0
    }

    fun getDisplayHeightInPx(context: Context): Int {
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (wm != null) {
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y
        }
        return 0
    }
}
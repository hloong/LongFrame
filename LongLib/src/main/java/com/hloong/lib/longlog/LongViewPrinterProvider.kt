package com.hloong.lib.longlog

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hloong.lib.util.DisplayUtil.dp2px

class LongViewPrinterProvider(
    private val rootView: FrameLayout,
    private val recyclerView: RecyclerView
) {
    private var floatingView: View? = null
    private var isOpen = false
    private var logView: FrameLayout? = null
    fun showFloatingView() {
        if (rootView.findViewWithTag<View?>(TAG_FLOATING_VIEW) != null) {
            return
        }
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.END
        val floatingView = genFloatingView()
        floatingView.tag = TAG_FLOATING_VIEW
        floatingView.setBackgroundColor(Color.BLACK)
        floatingView.alpha = 0.8f
        params.bottomMargin = dp2px(200f, recyclerView.resources)
        rootView.addView(genFloatingView(), params)
    }

    fun closeFloatingView() {
        rootView.removeView(genFloatingView())
    }

    private fun genFloatingView(): View {
        if (floatingView != null) {
            return floatingView!!
        }
        val textView = TextView(rootView.context)
        textView.setPadding(10, 10, 10, 10)
        textView.setOnClickListener(View.OnClickListener {
            if (!isOpen) {
                showLogView()
            }
        })
        textView.text = "LongLog"
        return textView.also { floatingView = it }
    }

    private fun showLogView() {
        if (rootView.findViewWithTag<View?>(TAG_LOG_VIEW) != null) {
            return
        }
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp2px(240f, rootView.resources)
        )
        params.gravity = Gravity.BOTTOM
        val logView = genLogView()
        logView.tag = TAG_LOG_VIEW
        rootView.addView(genLogView(), params)
        isOpen = true
    }

    private fun genLogView(): View {
        if (logView != null) {
            return logView!!
        }
        val logView = FrameLayout(rootView.context)
        logView.setBackgroundColor(Color.BLACK)
        logView.addView(recyclerView)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.END
        val tvClose = TextView(rootView.context)
        tvClose.setPadding(10, 10, 10, 10)
        tvClose.setOnClickListener { closeLogView() }
        tvClose.text = "Close"
        logView.addView(tvClose, params)
        return logView.also { this.logView = it }
    }

    fun closeLogView() {
        isOpen = false
        rootView.removeView(genLogView())
    }

    companion object {
        private val TAG_FLOATING_VIEW = "TAG_FLOATING_VIEW"
        private val TAG_LOG_VIEW = "TAG_LOG_VIEW"
    }
}
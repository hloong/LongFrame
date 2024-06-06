package com.hloong.ui.tab.top

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.hloong.ui.R
import com.hloong.ui.tab.common.ITap


open class TabTop constructor(context: Context?, attrs: AttributeSet?=null, defStyleAttr: Int = 0) :
        RelativeLayout(context, attrs, defStyleAttr), ITap<TabTopInfo<*>?> {
        private var tabInfo: TabTopInfo<*>? = null
        private var tabImageView: ImageView? = null
        private var indicator: View? = null
        private var tabNameView: TextView? = null
        init {
            LayoutInflater.from(context).inflate(R.layout.tab_top, this)
            tabImageView = findViewById(R.id.iv_image)
            tabNameView = findViewById(R.id.tv_name)
            indicator = findViewById(R.id.tab_top_indicator)
        }

    override fun setLongTabInfo(data: TabTopInfo<*>?) {
        this.tabInfo = data
        inflateInfo(false,true)
    }


    fun getTabImageView(): ImageView?{
        return tabImageView
    }

    fun getTabNameView():TextView?{
        return tabNameView
    }
    fun getLongTabInfo():TabTopInfo<*>?{
        return tabInfo
    }

    /**
         * 改变某个tab的高度
         *
         * @param height
         */
    override fun resetHeight(@Px height: Int) {
            val layoutParams = layoutParams
            layoutParams.height = height
            setLayoutParams(layoutParams)
            tabNameView!!.visibility = View.GONE
        }

    override fun onTabSelectedChange(
        index: Int,
        prevInfo: TabTopInfo<*>?,
        nextInfo: TabTopInfo<*>?
    ) {
        if (prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) {
            return
        }
        if (prevInfo == tabInfo) {
            inflateInfo(false, false)
        } else {
            inflateInfo(true, false)
        }
    }

    private fun inflateInfo(selected: Boolean, init: Boolean) {
            if (tabInfo!!.tabType === TabTopInfo.TabType.TEXT) {
                if (init) {
                    tabImageView!!.visibility = GONE
                    tabNameView!!.visibility = VISIBLE
                    if (!TextUtils.isEmpty(tabInfo!!.name)) {
                        tabNameView!!.text = tabInfo!!.name
                    }
                }
                if (selected) {
                    indicator!!.visibility = VISIBLE
                    tabNameView!!.setTextColor(getTextColor(tabInfo!!.tintColor))
                } else {
                    indicator!!.visibility = GONE
                    tabNameView!!.setTextColor(getTextColor(tabInfo!!.defaultColor))
                }
            } else if (tabInfo!!.tabType === TabTopInfo.TabType.BITMAP) {
                if (init) {
                    tabImageView!!.visibility = VISIBLE
                    tabNameView!!.visibility = GONE
                }
                if (selected) {
                    indicator!!.visibility = VISIBLE
                    tabImageView!!.setImageBitmap(tabInfo!!.selectedBitmap)
                } else {
                    indicator!!.visibility = GONE
                    tabImageView!!.setImageBitmap(tabInfo!!.defaultBitmap)
                }
            }
        }



        @ColorInt
        private fun getTextColor(color: Any?): Int {
            return if (color is String) {
                Color.parseColor(color)
            } else {
                color as Int
            }
        }
    }


package com.hloong.ui.tap.bottom

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
import com.hloong.ui.tap.common.ILongTap


class LongTapBottom(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
        RelativeLayout(context, attrs, defStyleAttr), ILongTap<LongTapBottomInfo<*>?> {
        constructor(context: Context?) : this(context, null) {}
        constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}

        private var tabInfo: LongTapBottomInfo<*>? = null
        private var tabImageView: ImageView? = null
        private var tabIconView: TextView? = null
        private var tabNameView: TextView? = null
        init {
            init()
        }

        private fun init() {
            LayoutInflater.from(context).inflate(R.layout.long_tab_bottom, this)
            tabImageView = findViewById(R.id.long_iv_image)
            tabIconView = findViewById(R.id.long_tv_icon)
            tabNameView = findViewById(R.id.long_tv_name)
        }

    override fun setLongTapInfo(data: LongTapBottomInfo<*>?) {
        this.tabInfo = data
        inflateInfo(false,true)
    }


    fun getTabImageView(): ImageView?{
        return tabImageView
    }
    fun getTabIconView():TextView?{
        return tabIconView
    }
    fun getTabNameView():TextView?{
        return tabNameView
    }
    fun getLongTabInfo():LongTapBottomInfo<*>?{
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
        prevInfo: LongTapBottomInfo<*>?,
        nextInfo: LongTapBottomInfo<*>?
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
            if (tabInfo!!.tabType === LongTapBottomInfo.TabType.ICON) {
                if (init) {
                    tabImageView!!.visibility = GONE
                    tabIconView!!.visibility = VISIBLE
                    val typeface = Typeface.createFromAsset(context.assets, tabInfo!!.iconFont)
                    tabIconView!!.setTypeface(typeface)
                    if (!TextUtils.isEmpty(tabInfo!!.name)) {
                        tabNameView!!.text = tabInfo!!.name
                    }
                }
                if (selected) {
                    tabIconView!!.text = if (TextUtils.isEmpty(tabInfo!!.selectedIconName)) tabInfo!!.defaultIconName else tabInfo!!.selectedIconName
                    tabIconView!!.setTextColor(getTextColor(tabInfo!!.tintColor))
                    tabNameView!!.setTextColor(getTextColor(tabInfo!!.tintColor))
                } else {
                    tabIconView!!.text = tabInfo!!.defaultIconName
                    tabIconView!!.setTextColor(getTextColor(tabInfo!!.defaultColor))
                    tabNameView!!.setTextColor(getTextColor(tabInfo!!.defaultColor))
                }
            } else if (tabInfo!!.tabType === LongTapBottomInfo.TabType.BITMAP) {
                if (init) {
                    tabImageView!!.visibility = VISIBLE
                    tabIconView!!.visibility = GONE
                    if (!TextUtils.isEmpty(tabInfo!!.name)) {
                        tabNameView!!.text = tabInfo!!.name
                    }
                }
                if (selected) {
                    tabImageView!!.setImageBitmap(tabInfo!!.selectedBitmap)
                } else {
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


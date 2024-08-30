package com.hloong.ui.banner.core

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import androidx.viewpager.widget.ViewPager

class ViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private val mContext: Context? = null
    private val mCachedViews: SparseArray<BannerAdapter.BannerViewHolder> =
        SparseArray<BannerAdapter.BannerViewHolder>()
    private val mBannerClickListener: IBanner.OnBannerClickListener? = null
    private val mBindAdapter: IBindAdapter? = null
    private val models: List<BannerModel?>? = null

    /**
     * 是否开启自动轮
     */
    private val mAutoPlay = true

    /**
     * 非自动轮播状态下是否可以循环切换
     */
    private val mLoop = false
    private val mLayoutResId = -1
    init {

    }

    fun setAutoPlay(autoPlay:Boolean){
        if (!autoPlay){

        }
    }

}
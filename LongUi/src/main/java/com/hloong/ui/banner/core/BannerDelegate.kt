package com.hloong.ui.banner.core

import android.content.Context
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.hloong.ui.R
import com.hloong.ui.banner.Banner
import com.hloong.ui.banner.indicator.CircleIndicator
import com.hloong.ui.banner.indicator.IIndicator

class BannerDelegate:IBanner, OnPageChangeListener {
    var mContext:Context?=null
    var banner:Banner?=null
    private var mAdapter: BannerAdapter? = null
    private var mIndicator: IIndicator<*>? = null
    private var mAutoPlay = false
    private var mLoop = false
    private var mBannerModel: List<BannerModel?>? = null
    private var mOnPageChangeListener: OnPageChangeListener? = null
    private var mIntervalTime = 5000
    private var mOnBannerClickListener: IBanner.OnBannerClickListener? = null
    private var mViewPager: LgViewPager? = null
    private var mScrollDuration = -1

    constructor (context: Context,banner: Banner){
        this.mContext = context
        this.banner = banner
    }
    override fun setBannerData(layoutResId: Int, models: List<BannerModel?>) {
        mBannerModel=models
        init(layoutResId)
    }

    override fun setBannerData(models: List<BannerModel?>) {
        setBannerData(R.layout.banner_item_image,models)
    }

    override fun setIIndicator(indicator: IIndicator<*>) {
        this.mIndicator = indicator
    }

    override fun setAutoPlay(autoPlay: Boolean) {
        this.mAutoPlay = autoPlay
        if (mAdapter != null) mAdapter!!.setAutoPlay(autoPlay)
        if (mViewPager != null) mViewPager!!.setAutoPlay(autoPlay)
    }

    override fun setLoop(loop: Boolean) {
        this.mLoop = loop
    }

    override fun setIntervalTime(intervalTime: Int) {
        if (intervalTime > 0){
            this.mIntervalTime = intervalTime
        }
    }

    override fun setBindAdapter(bindAdapter: IBindAdapter?) {
        mAdapter!!.setBindAdapter(bindAdapter)
    }

    override fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener?) {
        this.mOnPageChangeListener = onPageChangeListener
    }

    override fun setOnBannerClickListener(onBannerClickListener: IBanner.OnBannerClickListener?) {
        this.mOnBannerClickListener=onBannerClickListener
    }

    override fun setScrollDuration(duration: Int) {
        this.mScrollDuration=duration
        if (mViewPager != null && duration > 0) mViewPager!!.setScrollDuration(duration)
    }

    private fun init(@LayoutRes layoutResId: Int) {
        if (mAdapter == null) {
            mAdapter = BannerAdapter(mContext!!)
        }
        if (mIndicator == null) {
            mIndicator = CircleIndicator(mContext!!)
        }
        mIndicator!!.onInflate(mBannerModel!!.size)
        mAdapter!!.setLayoutResId(layoutResId)
        mAdapter!!.setBannerData(mBannerModel!!)
        mAdapter!!.setAutoPlay(mAutoPlay)
        mAdapter!!.setLoop(mLoop)
        mAdapter!!.setOnBannerClickListener(mOnBannerClickListener)

        mViewPager = LgViewPager(mContext!!)
        mViewPager!!.setIntervalTime(mIntervalTime)
        mViewPager!!.addOnPageChangeListener(this)
        mViewPager!!.setAutoPlay(mAutoPlay)
        if (mScrollDuration > 0) mViewPager!!.setScrollDuration(mScrollDuration)
        val layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )

        mViewPager!!.setAdapter(mAdapter)

        if ((mLoop || mAutoPlay) && mAdapter!!.getRealCount() !== 0) {
            //无限轮播关键点：使第一张能反向滑动到最后一张，已达到无限滚动的效果
            val firstItem = mAdapter!!.getFirstItem()
            mViewPager!!.setCurrentItem(firstItem, false)
        }

        //清除缓存view
        banner!!.removeAllViews()
        banner!!.addView(mViewPager, layoutParams)
        banner!!.addView(mIndicator!!.get(), layoutParams)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (null != mOnPageChangeListener && mAdapter!!.getRealCount() != 0){
            mOnPageChangeListener!!.onPageScrolled(position % mAdapter!!.getRealCount(),
                positionOffset,positionOffsetPixels)
        }
    }

    override fun onPageSelected(position: Int) {
        if (mAdapter!!.getRealCount() === 0) {
            return
        }
        var realPosition = position
        realPosition = position%mAdapter!!.getRealCount()
        mOnPageChangeListener?.onPageSelected(realPosition)
        if (mIndicator != null) {
            mIndicator!!.onPointChange(realPosition, mAdapter!!.getRealCount())
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (mOnPageChangeListener != null){
            mOnPageChangeListener!!.onPageScrollStateChanged(state)
        }
    }
}
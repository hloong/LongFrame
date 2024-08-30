package com.hloong.ui.banner.core

import androidx.annotation.LayoutRes
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.hloong.ui.banner.indicator.IIndicator

interface IBanner {
    fun setBannerData(@LayoutRes layoutResId: Int, models: List<BannerModel?>)

    fun setBannerData(models: List<BannerModel?>)

    fun setIIndicator(indicator: IIndicator<*>)

    fun setAutoPlay(autoPlay: Boolean)

    fun setLoop(loop: Boolean)

    fun setIntervalTime(intervalTime: Int)

    fun setBindAdapter(bindAdapter: IBindAdapter?)

    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener?)

    fun setOnBannerClickListener(onBannerClickListener: IBanner.OnBannerClickListener?)

    fun setScrollDuration(duration: Int)

    interface OnBannerClickListener {
        fun onBannerClick(
            viewHolder: BannerAdapter.BannerViewHolder,
            bannerMo: BannerModel,
            position: Int
        )
    }
}
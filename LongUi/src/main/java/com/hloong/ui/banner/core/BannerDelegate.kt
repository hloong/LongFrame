package com.hloong.ui.banner.core

import android.content.Context
import androidx.viewpager.widget.ViewPager
import com.hloong.ui.banner.Banner
import com.hloong.ui.banner.indicator.IIndicator

class BannerDelegate:IBanner {
    var mContext:Context?=null
    var banner:Banner?=null

    constructor (context: Context,banner: Banner){
        this.mContext = context
        this.banner = banner
    }
    override fun setBannerData(layoutResId: Int, models: List<BannerModel?>) {
    }

    override fun setBannerData(models: List<BannerModel?>) {
    }

    override fun setIIndicator(indicator: IIndicator<*>) {
    }

    override fun setAutoPlay(autoPlay: Boolean) {
    }

    override fun setLoop(loop: Boolean) {
    }

    override fun setIntervalTime(intervalTime: Int) {
    }

    override fun setBindAdapter(bindAdapter: IBindAdapter?) {
    }

    override fun setOnPageChangeListener(onPageChangeListener: ViewPager.OnPageChangeListener?) {
    }

    override fun setOnBannerClickListener(onBannerClickListener: IBanner.OnBannerClickListener?) {
    }

    override fun setScrollDuration(duration: Int) {
    }
}
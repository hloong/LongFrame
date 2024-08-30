package com.hloong.ui.banner

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.hloong.ui.R
import com.hloong.ui.banner.core.BannerDelegate
import com.hloong.ui.banner.core.BannerModel
import com.hloong.ui.banner.core.IBanner
import com.hloong.ui.banner.core.IBindAdapter
import com.hloong.ui.banner.indicator.IIndicator


/**
 * 核心问题：
 * 1. 如何实现UI的高度定制？
 * 2. 作为有限的item如何实现无线轮播呢？
 * 3. Banner需要展示网络图片，如何将网络图片库和Banner组件进行解耦？
 * 4. 指示器样式各异，如何实现指示器的高度定制？
 * 5. 如何设置ViewPager的滚动速度？
 */
class Banner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr), IBanner {
    var delegate: BannerDelegate = BannerDelegate(getContext(), this)

    init {
        initCustomAttrs(context, attrs)
    }

    private fun initCustomAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner)
        val autoPlay = typedArray.getBoolean(R.styleable.Banner_autoPlay, true)
        val loop = typedArray.getBoolean(R.styleable.Banner_loop, false)
        val intervalTime = typedArray.getInteger(R.styleable.Banner_intervalTime, -1)
        setAutoPlay(autoPlay)
        setLoop(loop)
        setIntervalTime(intervalTime)
        typedArray.recycle()
    }

    override fun setBannerData(@LayoutRes layoutResId: Int, models: List<BannerModel?>) {
        delegate.setBannerData(layoutResId, models)
    }

    override fun setBannerData(models: List<BannerModel?>) {
        delegate.setBannerData(models)
    }

    override fun setIIndicator(indicator: IIndicator<*>) {
        delegate.setIIndicator(indicator)
    }

    override fun setAutoPlay(autoPlay: Boolean) {
        delegate.setAutoPlay(autoPlay)
    }

    override fun setLoop(loop: Boolean) {
        delegate.setLoop(loop)
    }

    override fun setIntervalTime(intervalTime: Int) {
        delegate.setIntervalTime(intervalTime)
    }

    override fun setBindAdapter(bindAdapter: IBindAdapter?) {
        delegate.setBindAdapter(bindAdapter)
    }

    override fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener?) {
        delegate.setOnPageChangeListener(onPageChangeListener)
    }

    override fun setScrollDuration(duration: Int) {
        delegate.setScrollDuration(duration)
    }

    override fun setOnBannerClickListener(onBannerClickListener: IBanner.OnBannerClickListener?) {
        delegate.setOnBannerClickListener(onBannerClickListener)
    }
}
package com.hloong.ui.banner.core

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.PagerAdapter

class BannerAdapter: PagerAdapter() {

    private var mContext: Context ?= null
    private var mCachedViews = SparseArray<BannerViewHolder>()
    private var mBannerClickListener: IBanner.OnBannerClickListener? = null
    private var mBindAdapter: IBindAdapter? = null
    private var models: List<BannerModel?>? = null

    /**
     * 是否开启自动轮
     */
    private var mAutoPlay = true
    /**
     * 非自动轮播状态下是否可以循环切换
     */
    private var mLoop = false
    private var mLayoutResId = -1

    fun BannerAdapter(mContext: Context) {
        this.mContext = mContext
    }
    fun setBannerData(models: List<BannerModel?>) {
        this.models = models
        initCachedView()
        notifyDataSetChanged()
    }

    private fun initCachedView() {
        mCachedViews = SparseArray<BannerViewHolder>()
        for (i in models!!.indices) {
            val viewHolder = BannerViewHolder(createView(LayoutInflater.from(mContext), null))
            mCachedViews.put(i, viewHolder)
        }
    }

    fun setBindAdapter(bindAdapter: IBindAdapter?) {
        this.mBindAdapter = bindAdapter
    }

    fun setOnBannerClickListener(onBannerClickListener: IBanner.OnBannerClickListener?) {
        this.mBannerClickListener = onBannerClickListener
    }

    fun setLayoutResId(@LayoutRes layoutResId: Int) {
        this.mLayoutResId = layoutResId
    }

    fun setAutoPlay(autoPlay: Boolean) {
        this.mAutoPlay = autoPlay
    }

    fun setLoop(loop: Boolean) {
        this.mLoop = loop
    }
    private fun createView(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        require(mLayoutResId != -1) { "you must be set setLayoutResId first" }

        return layoutInflater.inflate(mLayoutResId, parent, false)
    }
    /**
     * 获取Banner页面数量
     *
     * @return
     */
    fun getRealCount(): Int {
        return models?.size ?: 0
    }
    override fun getCount(): Int {
        //无限轮播关键点
        return if (mAutoPlay) Int.MAX_VALUE else (if (mLoop) Int.MAX_VALUE else getRealCount())
    }

    /**
     * 获取初次展示的item位置
     *
     * @return
     */
    fun getFirstItem(): Int {
        return Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % getRealCount()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var realPosition = position
        if (getRealCount() > 0) {
            realPosition = position % getRealCount()
        }
        val viewHolder: BannerViewHolder = mCachedViews[realPosition]
        if (container == viewHolder.rootView.parent) {
            container.removeView(viewHolder.rootView)
        }

        onBind(viewHolder, models!![realPosition]!!, realPosition)
        if (viewHolder.rootView.parent != null) {
            (viewHolder.rootView.parent as ViewGroup).removeView(viewHolder.rootView)
        }
        container.addView(viewHolder.rootView)
        return viewHolder.rootView
    }

    protected fun onBind(
        viewHolder: BannerViewHolder,
        bannerMo: BannerModel,
        position: Int) {
        viewHolder.rootView.setOnClickListener(View.OnClickListener {
            if (mBannerClickListener != null) {
                mBannerClickListener!!.onBannerClick(viewHolder, bannerMo, position)
            }
        })
        if (mBindAdapter != null) {
            mBindAdapter!!.onBind(viewHolder, bannerMo, position)
        }
    }

    class BannerViewHolder internal constructor(var rootView: View) {
        private var viewHolderSparseArr: SparseArray<View?>? = null

        fun <V : View?> findViewById(id: Int): V? {
            if (rootView !is ViewGroup) {
                return rootView as V
            }
            if (this.viewHolderSparseArr == null) {
                this.viewHolderSparseArr = SparseArray(1)
            }

            var childView = viewHolderSparseArr!![id] as V?
            if (childView == null) {
                childView = rootView.findViewById<V>(id)
                viewHolderSparseArr!!.put(id, childView)
            }
            return childView
        }
    }



}
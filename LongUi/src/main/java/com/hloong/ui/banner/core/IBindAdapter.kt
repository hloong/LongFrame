package com.hloong.ui.banner.core

interface IBindAdapter {
    fun onBind(viewHolder: BannerAdapter.BannerViewHolder?, mo: BannerModel?, position: Int)
}
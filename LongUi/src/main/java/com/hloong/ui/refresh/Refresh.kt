package com.hloong.ui.refresh

interface Refresh {
    /**
     * 刷新时是否禁止滚动
     *
     * @param disableRefreshScroll 否禁止滚动
     */
    fun setDisableRefreshScroll(disableRefreshScroll: Boolean)

    /**
     * 刷新完成
     */
    fun refreshFinished()

    /**
     * 设置下拉刷新的监听器
     *
     * @param refreshListener 刷新的监听器
     */
    fun setRefreshListener(refreshListener: RefreshListener?)

    /**
     * 设置下拉刷新的视图
     *
     * @param hiOverView 下拉刷新的视图
     */
    fun setRefreshOverView(overView: OverView?)

    interface RefreshListener {
        fun onRefresh()

        fun enableRefresh(): Boolean
    }
}
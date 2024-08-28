package com.hloong.ui.refresh

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.hloong.lib.util.DisplayUtil

abstract class OverView @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null,defStyleAttr:Int = 0) :
    FrameLayout(context, attrs,defStyleAttr) {
        enum class RefreshState{
            INIT,
            /**
             * Header展示的状态
             * */
            VISIBLE,
            /**
             * 超出可刷新距离的状态
             */
            REFRESH,
            /**
             * 超出可刷新位置松开手后的状态
             */
            OVER_REFRESH
        }

    var mState=RefreshState.INIT

    /**
     * 触发下拉刷新 需要的最小高度
     */
    var mPullRefreshHeight: Int = 0

    /**
     * 最小阻尼
     */
    var minDamp: Float = 1.6f

    /**
     * 最大阻尼
     */
    var maxDamp: Float = 2.2f
    init {
        preInit()
    }

    private fun preInit() {
        mPullRefreshHeight = DisplayUtil.dp2px(66f, resources)
        init()
    }
    /**
     * 初始化
     */
    abstract fun init()

    protected abstract fun onScroll(scrollY: Int, pullRefreshHeight: Int)

    /**
     * 显示Overlay
     */
    protected abstract fun onVisible()

    /**
     * 超过Overlay，释放就会加载
     */
    abstract fun onOver()

    /**
     * 开始加载
     */
    abstract fun onRefresh()

    /**
     * 加载完成
     */
    abstract fun onFinish()

    /**
     * 设置状态
     *
     * @param state 状态
     */
    fun setState(state: RefreshState) {
        mState = state
    }

    /**
     * 获取状态
     *
     * @return 状态
     */
    fun getState(): RefreshState {
        return mState
    }
}

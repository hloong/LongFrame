package com.hloong.ui.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.Scroller
import com.hloong.lib.longlog.LongLog
import kotlin.math.abs

class RefreshLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,defStyleAttr:Int = 0
) : FrameLayout(context, attrs,defStyleAttr),Refresh {
    val TAG: String = RefreshLayout::class.java.simpleName
    private var state: OverView.RefreshState? = null
    private var gestureDetector: GestureDetector? = null
    private var autoScroller: RefreshLayout.AutoScroller? = null
    private var refreshListener: Refresh.RefreshListener? = null
    var overView: OverView? = null
    private var mLastY = 0
    //刷新时是否禁止滚动
    private var disableRefreshScroll = false
    private var gestureDetectorListener = object:GestureDetectorListener(){
        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            if (abs(p2)> abs(p3) || refreshListener != null&&!refreshListener!!.enableRefresh()){
                //横向滑动，或者刷新被禁止则不处理
                return false
            }
            if (disableRefreshScroll && state == OverView.RefreshState.REFRESH){
                return true//刷新时是否禁止滑动
            }
            var head = getChildAt(0)
            var child = ScrollUtil.findScrollableChild(this@RefreshLayout)
            if (ScrollUtil.childScrolled(child)){
                return false//如果列表发生了滚动则不处理
            }
            //如果没有刷新或没有达到刷新的距离，且头部已经划出或者下拉
            if ((state != OverView.RefreshState.REFRESH || head.bottom<=overView!!.mPullRefreshHeight) &&
                (head.bottom>0 || p3 <=0)){
                if (state != OverView.RefreshState.OVER_REFRESH){
                    var seed=0
                    if (child.top < overView!!.mPullRefreshHeight){
                        seed = (mLastY/overView!!.minDamp).toInt()
                    }else{
                        seed = (mLastY/overView!!.maxDamp).toInt()
                    }
                    var bool = moveDown(seed,true)
                    mLastY = (-p3).toInt()
                    return bool
                }else{
                    return false
                }
            }else{
                return false
            }
        }
    }
    init {
        autoScroller = AutoScroller()
        gestureDetector = GestureDetector(context, gestureDetectorListener)
    }
    override fun setDisableRefreshScroll(disableRefreshScroll: Boolean) {
        this.disableRefreshScroll=disableRefreshScroll
    }

    override fun refreshFinished() {
        var head = getChildAt(0)
        LongLog.i(this@RefreshLayout::class.java.simpleName, "refreshFinished head-bottom:" + head.bottom)
        overView!!.onFinish()
        overView!!.setState(OverView.RefreshState.INIT)
        var bottom = head.bottom
        if (bottom > 0){
            recover(bottom)
        }
        state = OverView.RefreshState.INIT
    }

    override fun setRefreshListener(refreshListener: Refresh.RefreshListener?) {
        this.refreshListener=refreshListener
    }

    override fun setRefreshOverView(overView: OverView?) {
        if (overView != null){
            removeView(overView)
        }
        this.overView=overView
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(this.overView, 0, params)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {


        //事件分发处理
        if (!autoScroller!!.isFinish()) {
            return false
        }
        var head = getChildAt(0)
        if (ev == null) return super.dispatchTouchEvent(ev)
        if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL
            || ev.action==MotionEvent.ACTION_POINTER_INDEX_MASK ){
            //松开手
            if (head.bottom > 0){
                if (state != OverView.RefreshState.REFRESH){
                    recover(head.bottom)
                    return false
                }
            }
            mLastY = 0
        }
        val consumed: Boolean = gestureDetector!!.onTouchEvent(ev)
        LongLog.i(TAG, "gesture consumed：$consumed")
        if ((consumed || (state !== OverView.RefreshState.INIT && state !== OverView.RefreshState.REFRESH)) && head.bottom != 0) {
            ev.action = MotionEvent.ACTION_CANCEL //让父类接受不到真实的事件
            return super.dispatchTouchEvent(ev)
        }
        return if (consumed) {
            true
        } else {
            super.dispatchTouchEvent(ev)
        }
    }

    private fun recover(dis: Int) {
        if (refreshListener != null && dis > overView!!.mPullRefreshHeight){
            autoScroller!!.recover(dis-overView!!.mPullRefreshHeight)
            state = OverView.RefreshState.OVER_REFRESH

        }else{
            autoScroller!!.recover(dis)
        }
    }


    /**
     * 借助Scroller实现视图的自动滚动
     * https://juejin.im/post/5c7f4f0351882562ed516ab6
     * 注意需要inner修饰词来访问父类的私有变量
     */
    inner class AutoScroller : Runnable{

        private var mScroller: Scroller? = null
        private var mLastY = 0
        private var mIsFinished = false

        constructor(){
            mScroller= Scroller(context,LinearInterpolator())
            mIsFinished = true
        }

        override fun run() {
            if (mScroller!!.computeScrollOffset()){//还未滚动完成
                moveDown(mLastY- mScroller!!.currY,false)
                mLastY = mScroller!!.currY
                post(this)
            }else{
                removeCallbacks(this)
                mIsFinished = true
            }
        }

        fun recover(dis:Int){
            if (dis<=0){
                return
            }
            removeCallbacks(this)
            mLastY = 0
            mIsFinished = false
            mScroller!!.startScroll(0,0,0,dis,300)
            post(this)
        }
        fun isFinish():Boolean{
            return mIsFinished
        }
    }


    /**
     * 根据偏移量移动header与child
     * @param offsetY 偏移量
     * @param nonAuto 是否非自动滚动触发
     * @return
     */
    private fun moveDown(offsetY: Int, nonAuto: Boolean): Boolean {
        var offsetY = offsetY
        LongLog.i("111", "changeState:$nonAuto")
        val head = getChildAt(0)
        val child = getChildAt(1)
        val childTop = child.top + offsetY

        LongLog.i(
            "-----",
            "moveDown head-bottom:" + head.bottom + ",child.getTop():" + child.top + ",offsetY:" + offsetY
        )
        if (childTop <= 0) { //异常情况的补充
            LongLog.i(TAG, "childTop<=0,mState$state")
            offsetY = -child.top
            //移动head与child的位置，到原始位置
            head.offsetTopAndBottom(offsetY)
            child.offsetTopAndBottom(offsetY)
            if (state !== OverView.RefreshState.REFRESH) {
                state = OverView.RefreshState.INIT
            }
        } else if (state === OverView.RefreshState.REFRESH && childTop > overView!!.mPullRefreshHeight) {
            //如果正在下拉刷新中，禁止继续下拉
            return false
        } else if (childTop <= overView!!.mPullRefreshHeight) { //还没超出设定的刷新距离
            if (overView!!.getState() !== OverView.RefreshState.VISIBLE && nonAuto) { //头部开始显示
                overView!!.onVisible()
                overView!!.setState(OverView.RefreshState.VISIBLE)
                state = OverView.RefreshState.VISIBLE
            }
            head.offsetTopAndBottom(offsetY)
            child.offsetTopAndBottom(offsetY)
            if (childTop == overView!!.mPullRefreshHeight && state === OverView.RefreshState.OVER_REFRESH) {
                LongLog.i(TAG, "refresh，childTop：$childTop")
                refresh()
            }
        } else {
            if (overView!!.getState() !== OverView.RefreshState.OVER && nonAuto) {
                //超出刷新位置
                overView!!.onOver()
                overView!!.setState(OverView.RefreshState.OVER)
            }
            head.offsetTopAndBottom(offsetY)
            child.offsetTopAndBottom(offsetY)
        }
        if (overView != null) {
            overView!!.onScroll(head.bottom, overView!!.mPullRefreshHeight)
        }
        return true
    }

    /**
     * 刷新
     */
    private fun refresh() {
        if (refreshListener != null) {
            state = OverView.RefreshState.REFRESH
            overView!!.onRefresh()
            overView!!.setState(OverView.RefreshState.REFRESH)
            refreshListener!!.onRefresh()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val head = getChildAt(0)
        val child = getChildAt(1)
        if (head != null && child != null){
            var childTop = child.top
            if (state == OverView.RefreshState.REFRESH){
                head.layout(0, overView !!.mPullRefreshHeight-head.measuredHeight,right, overView!!.mPullRefreshHeight)
                child.layout(0, overView !!.mPullRefreshHeight,right, overView!!.mPullRefreshHeight+child.measuredHeight)
            }else{
                head.layout(0, childTop-head.measuredHeight,right, childTop)
                child.layout(0, childTop,right, childTop+child.measuredHeight)
            }
            var other:View?=null
            for (i in 2 until childCount) {
                other = getChildAt(i)
                other.layout(0, top, right, bottom)
            }

        }
    }
}
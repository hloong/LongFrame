package com.hloong.ui.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.Scroller

class RefreshLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,defStyleAttr:Int = 0
) : FrameLayout(context, attrs,defStyleAttr),Refresh {
    val TAG: String = RefreshLayout::class.java.simpleName
    private var state: OverView.RefreshState? = null
    private var gestureDetector: GestureDetector? = null
    private val autoScroller: RefreshLayout.AutoScroller? = null
    private var refreshListener: Refresh.RefreshListener? = null
    var overView: OverView? = null
    private val mLastY = 0
    //刷新时是否禁止滚动
    private var disableRefreshScroll = false
    private var gestureDetectorListener:GestureDetectorListener = object:GestureDetectorListener(){
        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return super.onScroll(p0, p1, p2, p3)
        }
    }
    init {
        gestureDetector = GestureDetector(context, gestureDetectorListener)
    }
    override fun setDisableRefreshScroll(disableRefreshScroll: Boolean) {
        this.disableRefreshScroll=disableRefreshScroll
    }

    override fun refreshFinished() {

    }

    override fun setRefreshListener(refreshListener: Refresh.RefreshListener?) {
        this.refreshListener=refreshListener
    }

    override fun setRefreshOverView(overView: OverView?) {
        this.overView=overView
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        var head = getChildAt(0)
        if (ev == null) return super.dispatchTouchEvent(ev)
        if (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_CANCEL
            || ev.action==MotionEvent.ACTION_POINTER_INDEX_MASK ){
            //松开手
            if (head.bottom > 0){
                if (state != OverView.RefreshState.REFRESH){
                    recover(head.bottom)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
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
}
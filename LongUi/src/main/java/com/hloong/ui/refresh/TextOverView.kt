package com.hloong.ui.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.hloong.ui.R

class TextOverView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    OverView(context, attrs, defStyleAttr) {
    var mText:TextView?=null
    var mRotateView:ImageView?=null
    override fun init() {
        LayoutInflater.from(context).inflate(R.layout.refresh_overview,this,true)
        mText = findViewById(R.id.text)
        mRotateView = findViewById(R.id.iv_rotate)
    }

    override fun onScroll(scrollY: Int, pullRefreshHeight: Int) {

    }

    override fun onVisible() {
        mText!!.text ="下拉刷新"
    }

    override fun onOver() {
        mText!!.text = "松开刷新"
    }

    override fun onRefresh() {
        mText!!.text = "正在刷新..."
        val operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        val lin = LinearInterpolator()
        operatingAnim.interpolator = lin
        mRotateView!!.startAnimation(operatingAnim)
    }

    override fun onFinish() {
        mRotateView!!.clearAnimation()
    }
}
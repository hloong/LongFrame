package com.hloong.frame

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hloong.ui.banner.core.BannerAdapter
import com.hloong.ui.banner.core.BannerModel
import com.hloong.ui.banner.core.IBindAdapter
import com.hloong.ui.banner.indicator.CircleIndicator
import com.hloong.ui.banner.indicator.IIndicator
import kotlinx.android.synthetic.main.activity_banner.*
import kotlinx.android.synthetic.main.activity_banner.view.*

class BannerActivity : AppCompatActivity() {
    var autoPlay = false
    var indicator:IIndicator<*>?=null
    var list: MutableList<BannerModel> = ArrayList()
    val src = arrayListOf<String>(
        "https://images.freeimages.com/images/large-previews/a08/tracer-1182509.jpg",
        "https://images.freeimages.com/images/large-previews/406/tree-1174007.jpg",
        "https://images.freeimages.com/images/large-previews/b3d/light-1169780.jpg",
        "https://images.freeimages.com/images/large-previews/44e/red-lines-1181665.jpg")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)
        initView(CircleIndicator(this),false)
        auto_play.setOnCheckedChangeListener { _, b ->
            autoPlay = b
            initView(indicator!!,autoPlay)
        }
        tv_switch.setOnClickListener {

        }
    }

    fun initView(indicator:IIndicator<*>,autoPlay:Boolean){
        for (i in 0..3){
            var m = BannerModel()
            m.url = src[i]
            list.add(m)
        }
        banner.setAutoPlay(autoPlay)
        banner.setIntervalTime(2000)
        banner.setIIndicator(indicator)
        banner.setBannerData(R.layout.banner_item_layout,list)
        banner.setBindAdapter(object:IBindAdapter{
            override fun onBind(
                viewHolder: BannerAdapter.BannerViewHolder?,
                mo: BannerModel?,
                position: Int
            ) {
               var iv = viewHolder!!.findViewById<ImageView>(R.id.iv_image)
                var tv = viewHolder!!.findViewById<TextView>(R.id.tv_title)
                Glide.with(this@BannerActivity).load(mo!!.url).into(iv!!)
                tv!!.text = mo.url
            }
        })
    }


}
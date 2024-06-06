package com.hloong.ui.common.tab

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

class FragmentTabView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr:Int = 0
) : FrameLayout(context, attrs,defStyleAttr){
    var mAdapter: TabViewAdapter?= null
    var currentPosition:Int = 0


    fun getAdapter(): TabViewAdapter {
        return mAdapter!!
    }

    fun setAdapter(adapter: TabViewAdapter){
        if (mAdapter!=null || adapter == null){
            return
        }
        mAdapter = adapter
        currentPosition=-1
    }

    fun setCurrentItem(position:Int){
        if (position<0 || position>= mAdapter!!.getCount()){
            return
        }
        if (currentPosition != position){
            currentPosition= position
            mAdapter!!.instantiateItem(this,position)
        }
    }

    fun getCurrentItem():Int{
        return currentPosition
    }
    fun getCurrentFragment():Fragment{
        if (mAdapter == null){
            throw IllegalArgumentException("adatper null")
        }
        return mAdapter!!.getCurrentFragment()
    }

}
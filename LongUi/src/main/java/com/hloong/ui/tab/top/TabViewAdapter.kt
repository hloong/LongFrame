package com.hloong.ui.tab.top

import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.PagerAdapter
import com.hloong.ui.tab.bottom.LongTabBottomInfo

class TabViewAdapter @JvmOverloads constructor(fragmentManager: FragmentManager, infoList:List<LongTabBottomInfo<*>>){
    var mCurFragment:Fragment? = null
    val  mFragmentManager:FragmentManager?=null
     var mInfoList:List<LongTabBottomInfo<*>> = arrayListOf()
    fun getCount(): Int {
        return mInfoList.size
    }

    fun getCurrentFragment():Fragment{
        return mCurFragment!!
    }

     fun instantiateItem(container: ViewGroup, position: Int) {
        var mTransaction = mFragmentManager!!.beginTransaction()
        if (mCurFragment != null){
            mTransaction.hide(mCurFragment!!)
        }
        val name=container.id.toString() +":"+position
        var fragment:Fragment= mFragmentManager.findFragmentByTag(name)!!
        if (fragment != null){
            mTransaction.show(fragment)
        }else{
            fragment= getItem(position)!!
            if (!fragment.isAdded){
                mTransaction.add(container.id,fragment,name)
            }
        }
        mCurFragment = fragment
        mTransaction.commitAllowingStateLoss()
    }

    fun getItem(pos:Int): Fragment? {
        mInfoList[pos].fragment!!.newInstance()
        return null
    }

}
package com.hloong.ui.tab.top

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.hloong.ui.tab.bottom.LongTabBottomInfo

class HTabViewAdapter @JvmOverloads constructor(fragmentManager: FragmentManager,infoList:List<LongTabBottomInfo<*>>){
    val mCurFragment:Fragment? = null
    val  mFragmentManager:FragmentManager?=null
    lateinit var mInfoList:List<LongTabBottomInfo<*>>

}
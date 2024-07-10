package com.hloong.frame.tab

import android.os.Bundle
import android.view.View
import com.hloong.frame.ActivityProvider
import com.hloong.frame.R
import com.hloong.frame.TabBottomLogic
import com.hloong.ui.common.component.BaseActivity

class TabBottomActivity : BaseActivity(),ActivityProvider{
    var tabBottomLogic:TabBottomLogic?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_bottom)
        tabBottomLogic = TabBottomLogic(this)
    }


}
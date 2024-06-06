package com.hloong.frame.tab

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hloong.frame.ActivityProvider
import com.hloong.frame.R
import com.hloong.frame.TabBottomLogic
import com.hloong.lib.longlog.LongLog
import com.hloong.lib.util.DisplayUtil
import com.hloong.ui.common.component.BaseActivity
import com.hloong.ui.tab.bottom.LongTabBottomInfo
import com.hloong.ui.tab.common.ILongTabLayout
import kotlinx.android.synthetic.main.activity_tab_bottom.*

class TabBottomActivity : BaseActivity(),ActivityProvider{
    var tabBottomLogic:TabBottomLogic?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_bottom)
        tabBottomLogic = TabBottomLogic(this)
    }

}
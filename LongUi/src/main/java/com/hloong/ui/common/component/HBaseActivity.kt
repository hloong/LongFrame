package com.hloong.ui.common.component

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hloong.ui.common.BaseActionInterface

class HBaseActivity : AppCompatActivity(),BaseActionInterface{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
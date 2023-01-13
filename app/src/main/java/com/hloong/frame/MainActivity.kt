package com.hloong.frame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hloong.lib.longlog.*
import com.hloong.lib.longlog.base.LongLogConfig
import com.hloong.lib.longlog.base.LongLogType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var viewPrinter:LongViewPrinter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPrinter = LongViewPrinter(this)
        tv_log.setOnClickListener {
            printLog()
        }
        viewPrinter!!.viewPrinterProvider.showFloatingView()
    }

    private fun printLog(){
        LongLogManager.getInstance().addPrinters(viewPrinter);
        LongLog.log(object: LongLogConfig(){
            override fun includeThread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0
            }
        }, LongLogType.E,"Print","-1-1-1-")
        LongLog.a("long_log:123")
    }

}
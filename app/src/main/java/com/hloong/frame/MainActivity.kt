package com.hloong.frame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hloong.longlog.LongLog
import com.hloong.longlog.LongLogConfig
import com.hloong.longlog.LongLogType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_log.setOnClickListener {
            LongLog.a("123hi")
        }
    }

    private fun printLog(){
        LongLog.log(object:LongLogConfig(){
            override fun includeThread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0
            }
        },LongLogType.E,"-------","5566")
        LongLog.a("long_log:123")
    }

}
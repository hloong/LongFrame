package com.hloong.lib.longlog

import android.util.Log
import com.hloong.lib.longlog.base.LongLogConfig
import com.hloong.lib.longlog.base.LongLogPrinter

class LongConsolePrinter : LongLogPrinter {
    override fun print(config: LongLogConfig, level: Int, tag: String?, msg: String) {
        val len = msg.length
        val countOfSub = len / LongLogConfig.MAX_LEN
        if (countOfSub > 0) {
            val log = StringBuilder()
            var index = 0
            for (i in 0 until countOfSub) {
                log.append(msg.substring(index, index + LongLogConfig.MAX_LEN))
                index += LongLogConfig.MAX_LEN
            }
            if (index != len) {
                log.append(msg.substring(index, len))
            }
            Log.println(level, tag, log.toString())
        } else {
            Log.println(level, tag, msg)
        }
    }
}
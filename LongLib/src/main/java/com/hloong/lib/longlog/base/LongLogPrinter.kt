package com.hloong.lib.longlog.base

interface LongLogPrinter {
    fun print(config: LongLogConfig, level: Int, tag: String?, msg: String)
}
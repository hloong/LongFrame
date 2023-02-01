package com.hloong.lib.longlog.base

abstract class LongLogConfig {
    companion object {
       const val MAX_LEN = 512
        val THREAD_FORMATTER = LongThreadFormatter()
        val STACK_TRACE_FORMATTER = LongStackTraceFormatter()
    }
    open fun enable(): Boolean {
        return true
    }
    open fun getGlobalTag(): String{
        return "LongLog"
    }

    open fun injectJsonParser(): JsonParser? {
        return null
    }

    open fun includeThread(): Boolean {
        return false
    }

    /**
     * get stack depth
     * @return default 5
     */
    open fun stackTraceDepth(): Int {
        return 5
    }

    fun printers(): Array<LongLogPrinter>? {
        return null
    }

    interface JsonParser {
        fun toJson(obj: Any): String
    }


}
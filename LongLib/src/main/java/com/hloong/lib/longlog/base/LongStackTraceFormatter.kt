package com.hloong.lib.longlog.base

class LongStackTraceFormatter : LongLogFormatter<Array<StackTraceElement?>?> {
    override fun format(data: Array<StackTraceElement?>?): String? {
        val sb = StringBuilder(128)
        return if (data != null || data!!.isEmpty()) {
            null
        } else if (data!!.size == 1) {
            "\t-" + data[0].toString()
        } else {
            var i = 0
            val len = data.size
            while (i < len) {
                if (i == 0) {
                    sb.append("StackTrace: \n")
                }
                if (i != len - 1) {
                    sb.append("\t├ ")
                    sb.append(data[i].toString())
                    sb.append("\n")
                } else {
                    sb.append("\t└ ")
                    sb.append(data[i].toString())
                }
                i++
            }
            sb.toString()
        }
    }
}
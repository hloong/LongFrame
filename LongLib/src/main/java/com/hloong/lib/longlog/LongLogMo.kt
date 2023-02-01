package com.hloong.lib.longlog

import java.text.SimpleDateFormat
import java.util.*

class LongLogMo(var timeMillis: Long, var level: Int, var tag: String, var log: String) {
    fun flattenedLog(): String {
        return """
              ${flattened}
              $log
              """.trimIndent()//分别打印2行，
    }

    var flattened: String = ""
        get() = format(timeMillis) + '|' + level + '|' + tag + "|:"

    private fun format(timeMillis: Long): String {
        return sdf.format(timeMillis)
    }

    companion object {
        private var sdf = SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA)
    }
}
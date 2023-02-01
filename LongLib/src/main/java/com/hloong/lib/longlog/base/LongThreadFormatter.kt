package com.hloong.lib.longlog.base

class LongThreadFormatter : LongLogFormatter<Thread?> {
    override fun format(data: Thread?): String? {
        return "Thread:" + data!!.name
    }
}
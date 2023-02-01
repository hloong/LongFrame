package com.hloong.lib.longlog.base

interface LongLogFormatter<T> {
    fun format(data: T): String?
}
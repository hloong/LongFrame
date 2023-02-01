package com.hloong.lib.longlog

import com.hloong.lib.longlog.base.LongLogConfig
import com.hloong.lib.longlog.base.LongLogPrinter
import java.util.*

class LongLogManager private constructor(var config: LongLogConfig, printers: Array<out LongLogPrinter>) {
    private var printers: MutableList<LongLogPrinter> = ArrayList()

    init {
        this.printers.addAll(listOf(*printers))
    }

    fun getPrinters(): List<LongLogPrinter> {
        return printers
    }

    fun addPrinters(printer: LongLogPrinter) {
        printers.add(printer)
    }

    fun removePrinters(printer: LongLogPrinter?) {
        if (printer != null) {
            printers.remove(printer)
        }
    }

    companion object {
        @JvmStatic
        var instance: LongLogManager? = null
            private set

        fun init(config: LongLogConfig, vararg printers: LongLogPrinter) {
            instance = LongLogManager(config, printers)
        }
    }
}
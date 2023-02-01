package com.hloong.lib.longlog

import com.hloong.lib.longlog.LongLogManager.Companion.instance
import com.hloong.lib.longlog.base.LongLogConfig
import com.hloong.lib.longlog.base.LongLogType
import com.hloong.lib.longlog.base.LongStackTraceUtil.getCroppedRealStackTrack
import org.jetbrains.annotations.NotNull
import java.util.*

object LongLog {
    private var LOG_PACKAGE: String? = null

    init {
        val className = LongLog::class.java.name
        LOG_PACKAGE = className.substring(
            0,
            className.lastIndexOf('.') + 1
        )
    }
    fun v(vararg contents: Any) {
        log(LongLogType.V, *contents)
    }

    fun vt(tag: String, vararg contents: Any) {
        log(LongLogType.V, tag, *contents)
    }

    fun d(vararg contents: Any) {
        log(LongLogType.D, *contents)
    }

    fun dt(tag: String, vararg contents: Any) {
        log(LongLogType.D, tag, *contents)
    }

    fun i(vararg contents: Any) {
        log(LongLogType.I, *contents)
    }

    fun it(tag: String, vararg contents: Any) {
        log(LongLogType.I, tag, *contents)
    }

    fun w(vararg contents: Any) {
        log(LongLogType.W, *contents)
    }

    fun wt(tag: String, vararg contents: Any) {
        log(LongLogType.W, tag, *contents)
    }

    fun e(vararg contents: Any) {
        log(LongLogType.E, *contents)
    }

    fun et(tag: String, vararg contents: Any) {
        log(LongLogType.E, tag, *contents)
    }

    fun a(vararg contents: Any) {
        log(LongLogType.A, *contents)
    }

    fun at(tag: String, vararg contents: Any) {
        log(LongLogType.A, tag, *contents)
    }



    fun log(@LongLogType.TYPE type: Int, vararg contents: Any) {
        log(type, instance!!.config.getGlobalTag(), *contents)
    }

    fun log(@LongLogType.TYPE type: Int, @NotNull tag: String, vararg contents: Any) {
        log(instance!!.config, type, tag, *contents)
    }

    fun log(config: LongLogConfig, @LongLogType.TYPE type: Int, tag: String, vararg contents: Any) {
        if (!config.enable()) {
            return
        }
        val sb = StringBuilder()
        if (config.includeThread()) {
            val threadInfo = LongLogConfig.THREAD_FORMATTER.format(Thread.currentThread())
            sb.append(threadInfo).append("\n")
        }
        if (config.stackTraceDepth() > 0) {
            val stackTrace = LongLogConfig.STACK_TRACE_FORMATTER.format(
                getCroppedRealStackTrack(
                    Throwable().stackTrace,
                    LOG_PACKAGE, config.stackTraceDepth()
                )
            )
            sb.append(stackTrace).append("\n")
        }
        var body = parseBody(config,*contents)
        if (body != null) { //替换转义字符\
            body = body.replace("\\\"", "\"")
        }
        sb.append(body)
        val printers =
            (if (config.printers() != null) Arrays.asList(*config.printers()!!) else instance!!.getPrinters())
                ?: return
        for (printer in printers) {
            printer!!.print(config, type, tag, sb.toString())
        }
    }



    private fun parseBody(config: LongLogConfig,vararg contents: Any): String {
        if (config.injectJsonParser() != null) {
            return if (contents.size == 1 && contents[0] is String) {
                contents[0] as String
            } else config.injectJsonParser()!!.toJson(contents)
        }
        val sb = StringBuilder()
        for (o in contents) {
            sb.append(o.toString()).append(";")
        }
        if (sb.isNotEmpty()) {
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }
}
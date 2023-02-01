package com.hloong.lib.longlog.base

import kotlin.math.min

object LongStackTraceUtil {
    /**
     * To get clip stack information
     * @param stackTrace
     * @param ignorePackage
     * @param maxDepth
     * @return
     */
    @JvmStatic
    fun getCroppedRealStackTrack(
        stackTrace: Array<StackTraceElement>,
        ignorePackage: String?,
        maxDepth: Int
    ): Array<StackTraceElement?> {
        return cropStackTrace(getRealStackTrack(stackTrace, ignorePackage), maxDepth)
    }

    /**
     * To get the stack information except ignore the package name
     * @param stackTrace
     * @param ignorePackage
     * @return
     */
    private fun getRealStackTrack(
        stackTrace: Array<StackTraceElement>,
        ignorePackage: String?
    ): Array<StackTraceElement?> {
        var ignoreDepth = 0
        val allDepth = stackTrace.size
        var className: String
        for (i in allDepth - 1 downTo 0) {
            className = stackTrace[i].className
            if (ignorePackage != null && className.startsWith(ignorePackage)) {
                ignoreDepth = i + 1
                break
            }
        }
        val realDepth = allDepth - ignoreDepth
        val realStack = arrayOfNulls<StackTraceElement>(realDepth)
        System.arraycopy(stackTrace, ignoreDepth, realStack, 0, realDepth)
        return realStack
    }

    /**
     * Clipping stack information
     * @param callStack
     * @param maxDepth
     * @return
     */
    private fun cropStackTrace(
        callStack: Array<StackTraceElement?>,
        maxDepth: Int
    ): Array<StackTraceElement?> {
        var realDepth = callStack.size
        if (maxDepth > 0) {
            realDepth = min(maxDepth, realDepth)
        }
        val realStack = arrayOfNulls<StackTraceElement>(realDepth)
        System.arraycopy(callStack, 0, realStack, 0, realDepth)
        return realStack
    }
}
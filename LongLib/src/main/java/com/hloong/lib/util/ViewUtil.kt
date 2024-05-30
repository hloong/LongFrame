package com.hloong.lib.util

import android.view.View
import android.view.ViewGroup
import java.util.*

object ViewUtil {
     fun <T> findTypeView(group: ViewGroup, cls: Class<T>): T? {
        if (group == null) {
            return null
        }
        val deque: Deque<View> = ArrayDeque()
        deque.add(group)
        while (!deque.isEmpty()) {
            val node = deque.removeFirst()
            if (cls.isInstance(node)) {
                return cls.cast(node)
            } else if (node is ViewGroup) {
                var container = node
                var i = 0
                val count = container.childCount
                while (i < count) {
                    deque.add(container.getChildAt(i))
                    i++
                }
            }
        }
        return null
    }
}
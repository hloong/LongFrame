package com.hloong.lib.longlog.base;

public interface LongLogFormatter<T> {
    String format(T data);
}

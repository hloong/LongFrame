package com.hloong.lib.longlog;

public interface LongLogFormatter<T> {
    String format(T data);
}

package com.hloong.lib.longlog;

public interface LongLogFromatter<T> {
    String format(T data);
}

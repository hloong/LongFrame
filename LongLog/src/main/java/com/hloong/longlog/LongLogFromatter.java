package com.hloong.longlog;

public interface LongLogFromatter<T> {
    String format(T data);
}

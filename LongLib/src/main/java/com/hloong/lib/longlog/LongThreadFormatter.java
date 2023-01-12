package com.hloong.lib.longlog;

public class LongThreadFormatter implements LongLogFormatter<Thread> {
    @Override
    public String format(Thread data) {
        return "Thread:"+data.getName();
    }
}

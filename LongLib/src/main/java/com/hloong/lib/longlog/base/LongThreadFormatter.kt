package com.hloong.lib.longlog.base;

import com.hloong.lib.longlog.base.LongLogFormatter;

public class LongThreadFormatter implements LongLogFormatter<Thread> {
    @Override
    public String format(Thread data) {
        return "Thread:"+data.getName();
    }
}

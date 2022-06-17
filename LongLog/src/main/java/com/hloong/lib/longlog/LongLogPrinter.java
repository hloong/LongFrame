package com.hloong.lib.longlog;

import org.jetbrains.annotations.NotNull;

public interface LongLogPrinter {
    void print(@NotNull LongLogConfig config,int level,String tag,@NotNull String msg);
}

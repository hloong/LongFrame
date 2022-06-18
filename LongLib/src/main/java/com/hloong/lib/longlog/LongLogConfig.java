package com.hloong.lib.longlog;

public abstract class LongLogConfig {
    static int MAX_LEN = 512;
    static LongThreadFormatter THREAD_FORMATTER = new LongThreadFormatter();
    static LongStackTraceFormatter STACK_TRACE_FORMATTER = new LongStackTraceFormatter();

    public String getGlobalTag(){
        return "LongLog";
    }
    public boolean enable(){
        return true;
    }

    public JsonParser injectJsonParser(){
        return null;
    }

    public boolean includeThread(){
        return false;
    }

    public int stackTraceDepth() {
        return 5;
    }

    public LongLogPrinter[] printers(){
        return null;
    }

    public interface JsonParser{
        String toJson(Object object);
    }



}

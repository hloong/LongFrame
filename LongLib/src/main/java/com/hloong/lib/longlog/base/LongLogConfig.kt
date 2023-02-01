package com.hloong.lib.longlog.base;

public abstract class LongLogConfig {
    public static int MAX_LEN = 512;
    public static LongThreadFormatter THREAD_FORMATTER = new LongThreadFormatter();
    public static LongStackTraceFormatter STACK_TRACE_FORMATTER = new LongStackTraceFormatter();

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

    /**
     * get stack depth
     * @return default 5
     */
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

package com.hloong.longlog;


import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class LongLog {
    public static void v(Object...  contents){
        log(LongLogType.V,contents);
    }
    public static void vt(String tag,Object... contents){
        log(LongLogType.V,tag,contents);
    }
    public static void d(Object...  contents){
        log(LongLogType.D,contents);
    }
    public static void dt(String tag,Object... contents){
        log(LongLogType.D,tag,contents);
    }
    public static void i(Object...  contents){
        log(LongLogType.I,contents);
    }
    public static void it(String tag,Object... contents){
        log(LongLogType.I,tag,contents);
    }
    public static void w(Object...  contents){
        log(LongLogType.W,contents);
    }
    public static void wt(String tag,Object... contents){
        log(LongLogType.W,tag,contents);
    }
    public static void e(Object...  contents){
        log(LongLogType.E,contents);
    }
    public static void et(String tag,Object... contents){
        log(LongLogType.E,tag,contents);
    }
    public static void a(Object...  contents){
        log(LongLogType.A,contents);
    }
    public static void at(String tag,Object... contents){
        log(LongLogType.A,tag,contents);
    }

    public static void log(@LongLogType.TYPE int type,Object... contents){
        log(type,LongLogManager.getInstance().getConfig().getGlobalTag(),contents);
    }
    public static void log(@LongLogType.TYPE int type, @NotNull String tag, Object... contents){
        log(LongLogManager.getInstance().getConfig(),type,tag,contents);
    }
    public static void log(@NotNull LongLogConfig config, @LongLogType.TYPE int type, @NotNull String tag, Object... contents){
        if (!config.enable()){
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeThread()){
            String threadInfo = LongLogConfig.THREAD_FORMATTER.format(Thread.currentThread());
            sb.append(threadInfo).append("\n");
        }
        if (config.stackTraceDepth() > 0){
            String stackTrace = LongLogConfig.STACK_TRACE_FORMATTER.format(new Throwable().getStackTrace());
            sb.append(stackTrace).append("\n");
        }
        String body = parseBody(contents,config);
        sb.append(body);
        List<LongLogPrinter> printers = config.printers() != null
                ? Arrays.asList(config.printers())
                : LongLogManager.getInstance().getPrinters();
        if (printers == null){
            return;
        }
        for (LongLogPrinter printer:printers){
            printer.print(config,type,tag,sb.toString());
        }
    }

    private static String parseBody(@NotNull Object[] contents,@NotNull LongLogConfig config){
        if (config.injectJsonParser() != null){
            return config.injectJsonParser().toJson(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object o: contents){
            sb.append(o.toString()).append(";");
        }
        if (sb.length()>0){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }
}


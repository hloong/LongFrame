package com.hloong.longlog;


import org.jetbrains.annotations.NotNull;

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
    }
    public static void log(@LongLogType.TYPE int type, @NotNull String tag, Object... contents){
    }
}

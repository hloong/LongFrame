package com.hloong.longlog;

public abstract class LongLogConfig {
    public String getGlobalTag(){
        return "LongLog";
    }
    public boolean enable(){
        return true;
    }
}

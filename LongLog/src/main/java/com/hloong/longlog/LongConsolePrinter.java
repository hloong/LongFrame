package com.hloong.longlog;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import static com.hloong.longlog.LongLogConfig.MAX_LEN;

public class LongConsolePrinter implements LongLogPrinter {

    @Override
    public void print(@NotNull LongLogConfig config, int level, String tag, @NotNull String msg) {
        int len = msg.length();
        int countOfSub = len/MAX_LEN;
        if (countOfSub > 0){
            int index = 0;
            for (int i = 0; i < countOfSub ; i++) {
                Log.println(level,tag,msg.substring(index,index+MAX_LEN));
                index+=MAX_LEN;
            }
            if (index != len){
                Log.println(level,tag,msg.substring(index,len));
            }else {
                Log.println(level,tag,msg);
            }
        }
    }
}

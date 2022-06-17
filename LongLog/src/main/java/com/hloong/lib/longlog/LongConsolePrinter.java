package com.hloong.lib.longlog;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import static com.hloong.lib.longlog.LongLogConfig.MAX_LEN;

public class LongConsolePrinter implements LongLogPrinter {

    @Override
    public void print(@NotNull LongLogConfig config, int level, String tag, @NotNull String msg) {
        int len = msg.length();
        int countOfSub = len/MAX_LEN;
        if (countOfSub > 0){
            StringBuilder log = new StringBuilder();
            int index = 0;
            for (int i = 0; i < countOfSub ; i++) {
                log.append(msg.substring(index,index+MAX_LEN));
                index+=MAX_LEN;
            }
            if (index != len){
                log.append(msg.substring(index,len));
            }
            Log.println(level,tag,log.toString());
        }else {
            Log.println(level,tag,msg);
        }
    }
}

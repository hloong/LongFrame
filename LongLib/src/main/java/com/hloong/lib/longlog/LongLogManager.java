package com.hloong.lib.longlog;

import com.hloong.lib.longlog.base.LongLogConfig;
import com.hloong.lib.longlog.base.LongLogPrinter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongLogManager {
    private LongLogConfig config;
    private static LongLogManager instance;
    private List<LongLogPrinter> printers = new ArrayList<>();
    private LongLogManager(LongLogConfig config,LongLogPrinter[] printers){
        this.config = config;
        this.printers.addAll(Arrays.asList(printers));

    }
    public static LongLogManager getInstance(){
        return instance;
    }
    public static void init(@NotNull LongLogConfig config,LongLogPrinter... printers){
        instance = new LongLogManager(config,printers);
    }

    public LongLogConfig getConfig(){
        return config;
    }

    public List<LongLogPrinter> getPrinters() {
        return printers;
    }

    public void addPrinters(LongLogPrinter printer) {
        printers.add(printer);
    }
    public void removePrinters(LongLogPrinter printer) {
        if (printer != null){
            printers.remove(printer);
        }
    }
}

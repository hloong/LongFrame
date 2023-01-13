package com.hloong.lib.longlog;

import com.hloong.lib.longlog.base.LongLogConfig;
import com.hloong.lib.longlog.base.LongLogPrinter;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class LongFilePrinter implements LongLogPrinter {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final String logPath;
    private final long retentionTime;
    private LogWriter writer;
    private volatile  PrintWorker worker;
    private static LongFilePrinter instance;

    public static synchronized LongFilePrinter getInstance(String logPath,long retentionTime){
        if (instance == null){
            instance = new LongFilePrinter(logPath,retentionTime);
        }
        return instance;
    }

    public LongFilePrinter(String logPath, long retentionTime) {
        this.logPath = logPath;
        this.retentionTime = retentionTime;
        this.writer = new LogWriter();
        this.worker = new PrintWorker();
        cleanExpireLog();
    }

    /**
     * 清除过期log
     */
    private void cleanExpireLog() {
        if (retentionTime <= 0){
            return;
        }
        long currentTime = System.currentTimeMillis();
        File logDir = new File(logPath);
        File[] files = logDir.listFiles();
        if (files == null){
            return;
        }
        for (File file:files) {
            if (currentTime-file.lastModified() > retentionTime){
                file.delete();
            }
        }
    }

    @Override
    public void print(@NotNull LongLogConfig config, int level, String tag, @NotNull String msg) {
        long timeMillis = System.currentTimeMillis();
        if (!worker.isRunning()){
            worker.start();
        }
        worker.put(new LongLogMo(timeMillis,level,tag,msg));
    }

    private class PrintWorker implements Runnable{
        private BlockingQueue<LongLogMo> logs = new LinkedBlockingDeque<>();
        private volatile boolean running;
        /**
         * 将log放入打印队列
         * @param log 要被打印的log
         */
        void put(LongLogMo log){
            try {
                logs.put(log);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        /**
         * 判断工作线程是否还在运行中
         * @return true 在运行
         */
        boolean isRunning() {
            synchronized (this){
                return running;
            }
        }

        void start(){
            synchronized (this){
                EXECUTOR.execute(this);
                running = true;
            }
        }

        @Override
        public void run() {
            LongLogMo log;
            while (true){
                try {
                    log = logs.take();
                    doPrint(log);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void doPrint(LongLogMo logMo){
        String lastFileName = writer.getPreFileName();
        if (lastFileName == null) {
            String newFileName = genFileName();
            if (writer.isReady()) {
                writer.close();
            }
            if (!writer.ready(newFileName)) {
                return;
            }
        }
        writer.append(logMo.flattenedLog());
    }
    private String genFileName(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(System.currentTimeMillis()));
    }
    /**
     * 基于BufferedWriter将log写入文件
     */
    private class LogWriter {
        private String preFileName;
        private File logFile;
        private BufferedWriter bufferedWriter;
        boolean isReady() {
            return bufferedWriter != null;
        }
        String getPreFileName() {
            return preFileName;
        }
        /**
         * log写入前的准备操作
         * @param newFileName 要保存log的文件名
         * @return true 表示准备就绪
         */
        boolean ready(String newFileName) {
            preFileName = newFileName;
            logFile = new File(logPath, newFileName);
            // 当log文件不存在时创建log文件
            if (!logFile.exists()) {
                try {
                    File parent = logFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    preFileName = null;
                    logFile = null;
                    return false;
                }
            }
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            } catch (Exception e) {
                e.printStackTrace();
                preFileName = null;
                logFile = null;
                return false;
            }
            return true;
        }
        /**
         * 关闭bufferedWriter
         */
        boolean close() {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    bufferedWriter = null;
                    preFileName = null;
                    logFile = null;
                }
            }
            return true;
        }
        /**
         * 将log写入文件
         * @param flattenedLog 格式化后的log
         */
        void append(String flattenedLog) {
            try {
                bufferedWriter.write(flattenedLog);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

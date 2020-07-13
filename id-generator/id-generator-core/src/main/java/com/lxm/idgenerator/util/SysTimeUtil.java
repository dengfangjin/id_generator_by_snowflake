package com.lxm.idgenerator.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SysTimeUtil {

    private final long period;
    private final AtomicLong now;

    private static SysTimeUtil ourInstance = new SysTimeUtil();

    public static SysTimeUtil getInstance() {
        return ourInstance;
    }

    private SysTimeUtil() {
        this.period = 1;
        this.now = new AtomicLong(System.currentTimeMillis());
        init();
    }

    private void init() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "SysTimeUtil");
                thread.setDaemon(true);
                return thread;
            }
        });
        // 固定每ms获取一次系统日期
        executor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                now.set(System.currentTimeMillis());
            }
        }, period, period, TimeUnit.MILLISECONDS);
    }

    public static long currentTimeMillis() {
        return getInstance().now.get();
    }
}

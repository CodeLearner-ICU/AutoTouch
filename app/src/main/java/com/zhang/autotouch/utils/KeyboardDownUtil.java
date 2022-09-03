package com.zhang.autotouch.utils;

import android.app.Instrumentation;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KeyboardDownUtil {

    static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,1,200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    public static void keyDown(final int key) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                sendKeyDownUpSync(key);
            }
        });
    }

    //必须运行在子线程
    private static void sendKeyDownUpSync(int key) {
        try {
            Instrumentation inst = new Instrumentation();
            inst.sendKeyDownUpSync(key);
        }catch (java.lang.SecurityException e) {
            e.printStackTrace();
        }
    }
}

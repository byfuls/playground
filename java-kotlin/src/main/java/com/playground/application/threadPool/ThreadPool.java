package com.playground.application.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadPool {
    private static volatile ThreadPool instance;
    private ConcurrentHashMap<String, Thread> threadPool;
    private ThreadPool() {
        threadPool = new ConcurrentHashMap<>();
    }

    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadPool.class) {
                if (instance == null) {
                    instance = new ThreadPool();
                }
            }
        }
        return instance;
    }

    public void run(String key, Runnable runnable) {
        if (key.isEmpty() || runnable == null) {
            return;
        }
        if (threadPool.containsKey(key)) {
            return;
        }

        Thread thread = new Thread(runnable);
        threadPool.put(key, thread);
        thread.start();
    }
    public void stop(String key) {
        if (key.isEmpty()) {
            return;
        }
        if (threadPool.containsKey(key)) {
            Thread thread = threadPool.get(key);
            thread.interrupt();
            threadPool.remove(key);
        }
    }
    public List<String> getThreadsKey() {
        return new ArrayList<String>(threadPool.keySet());
    }
}

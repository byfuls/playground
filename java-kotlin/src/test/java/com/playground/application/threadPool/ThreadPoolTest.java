package com.playground.application.threadPool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolTest {
    @Test
    public void testThreadPool() throws InterruptedException {
        ThreadPool instance = ThreadPool.getInstance();

        final String key1 = "test1";
        instance.run(key1, () -> {
            while (true) {
                System.out.println(key1 + " running...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    instance.stop(key1);
                    return;
                }
            }
        });

        final String key2 = "test2";
        instance.run(key2, () -> {
            while (true) {
                System.out.println(key2 + " running...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    instance.stop(key2);
                    return;
                }
            }
        });

        int i = 0;
        while (i == 0) {
            System.out.println(instance.getThreadsKey());
            Thread.sleep(3000);
            i++;
        }
        instance.stop(key1);
        System.out.println(instance.getThreadsKey());

        i = 0;
        while (i == 0) {
            System.out.println(instance.getThreadsKey());
            Thread.sleep(3000);
            i++;
        }
        instance.stop(key2);
        System.out.println(instance.getThreadsKey());
    }
}

package com.matas;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author matas
 * @date 2018/9/29 13:33
 * @email mataszhang@163.com
 */
public class SimpleGauge {
    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private static final ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry)//
            .convertDurationsTo(TimeUnit.MILLISECONDS)//
            .convertRatesTo(TimeUnit.MILLISECONDS)//
            .build();

    private static final BlockingQueue<Long> queue = new LinkedBlockingQueue<>(10);

    public static void main(String[] args) {
        metricRegistry.register(MetricRegistry.name(SimpleGauge.class, "queue-count"), (Gauge<Integer>) queue::size);

        consoleReporter.start(1, TimeUnit.SECONDS);

        new Thread(() -> {
            while (true) {
                randomSleep();
                queue.add(System.currentTimeMillis());
            }
        }).start();

        new Thread(() -> {
            while (true) {
                randomSleep();
                queue.poll();
            }
        }).start();
    }

    private static void randomSleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100, 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.matas;

import com.codahale.metrics.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class TestCachedGague {
    private static final MetricRegistry registry = new MetricRegistry();

    public static void main(String[] args) throws InterruptedException {
        startReport();

        Thread.currentThread().join();
    }

    static void startReport() {
        registry.gauge("cached-value", () -> new CachedGauge<Long>(2, TimeUnit.SECONDS) {
            @Override
            protected Long loadValue() {
                return ThreadLocalRandom.current().nextLong();
            }
        });
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.SECONDS).build();
        reporter.start(1, TimeUnit.SECONDS);
    }
}

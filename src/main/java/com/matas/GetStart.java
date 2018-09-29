package com.matas;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class GetStart {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final Meter requests = registry.meter("requests");

    public static void main(String[] args) {
        startReport();
        while (true) {
            doRequest();
        }
    }

    static void startReport() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.SECONDS).build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    private static void doRequest() {
        requests.mark();
        randomSleep();
    }


    private static void randomSleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100, 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

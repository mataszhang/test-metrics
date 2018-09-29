package com.matas;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class TestRatioGague {
    private static final MetricRegistry registry = new MetricRegistry();
    private static final Meter totalMeter = new Meter();
    private static final Meter successMeter = new Meter();

    public static void main(String[] args) {
        startReport();
        while (true) {
            doRequest();
        }
    }

    static void startReport() {
        registry.gauge(MetricRegistry.name(TestRatioGague.class, "success-ratio"), () -> new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                return Ratio.of(successMeter.getCount(), totalMeter.getCount());
            }
        });

        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.SECONDS).build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    private static void doRequest() {
        totalMeter.mark();
        randomSleep();
        if (ThreadLocalRandom.current().nextInt(0, 10) % ThreadLocalRandom.current().nextInt(1,4) == 0) {
            successMeter.mark();
        }
    }


    private static void randomSleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(100, 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

package com.matas;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.JmxAttributeGauge;
import com.codahale.metrics.MetricRegistry;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author matas
 * @date 2018/9/29 13:33
 * @email mataszhang@163.com
 */
public class TestJmxGauge {
    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private static final ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry)//
            .convertDurationsTo(TimeUnit.MILLISECONDS)//
            .convertRatesTo(TimeUnit.MILLISECONDS)//
            .build();

    private static final BlockingQueue<Long> queue = new LinkedBlockingQueue<>(10);

    public static void main(String[] args) throws MalformedObjectNameException, InterruptedException {
        metricRegistry.register(MetricRegistry.name(TestJmxGauge.class, "HeapMemoryUsage"), new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage"));
        metricRegistry.register(MetricRegistry.name(TestJmxGauge.class, "NonHeapMemoryUsage"), new JmxAttributeGauge(new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage"));

        consoleReporter.start(5, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }
}

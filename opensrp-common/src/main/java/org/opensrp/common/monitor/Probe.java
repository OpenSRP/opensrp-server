package org.opensrp.common.monitor;

public class Probe {
    private Metric metric;
    private long startTime;

    public Probe(Metric metric) {
        this.metric = metric;
        this.startTime = System.nanoTime();
    }

    public long value() {
        return System.nanoTime() - startTime;
    }

    public Metric metric() {
        return metric;
    }
}

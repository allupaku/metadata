package me.althaf.models;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple implementation of an entry observer, just to do the counting.
 * Same cane be done to do advanced processing / calculations.
 */
public class ResultCounter implements IEntryObserver<Map<String,String>, Integer> {

    AtomicInteger count = new AtomicInteger();

    @Override
    public void observe(Map<String, String> data) {
        this.count.incrementAndGet();
    }

    @Override
    public Integer getResult() {
        return this.count.get();
    }

    public String toString(){
        return "Number of records : " + this.count.get() + "\r\n";
    }
}

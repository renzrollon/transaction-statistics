package com.n26.challenge.domain;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

/**
 * Created by renz on 12/17/2017.
 */
public class AtomicReferenceWithTimestamp<V> {

    private final AtomicReference<V> atomicReference;
    private final long timestampInSeconds;

    public AtomicReferenceWithTimestamp(V atomicReference, long timestampInSeconds) {
        this.atomicReference = new AtomicReference<>(atomicReference);
        this.timestampInSeconds = timestampInSeconds;
    }

    public V updateAndGet(UnaryOperator<V> updater) {
        return atomicReference.updateAndGet(updater);
    }

    public V getValue() {
        return atomicReference.get();
    }
    public long getTimestampInSeconds() {
        return timestampInSeconds;
    }

    public AtomicReference<V> getAtomicReference() {
        return atomicReference;
    }


}

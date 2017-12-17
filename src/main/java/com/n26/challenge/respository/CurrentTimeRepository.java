package com.n26.challenge.respository;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Created by renz on 12/17/2017.
 */
@Component
public class CurrentTimeRepository implements Supplier<Long> {

    @Override
    public Long get() {
        return System.currentTimeMillis();
    }
}

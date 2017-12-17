package com.n26.challenge.domain;

import java.math.BigDecimal;

/**
 * Created by renz on 12/17/2017.
 */
public class Transaction {

    private BigDecimal amount;
    private long timestamp;

    public Transaction(){}

    public Transaction(String amount, String timestamp) {
        this.amount = new BigDecimal(amount);
        this.timestamp = Long.valueOf(timestamp);
    }

    public Transaction(BigDecimal amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

}

package com.n26.challenge.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by renz on 12/17/2017.
 */
public class TransactionStatistics {

    public static final int DEFAULT_SCALE = 2;
    public static final TransactionStatistics DEFAULT = builder()
            .count(0)
            .sum(BigDecimal.ZERO)
            .build();

    private final BigDecimal sum;
    private final BigDecimal max;
    private final BigDecimal min;
    private final long count;

    private TransactionStatistics(BigDecimal sum, BigDecimal max, BigDecimal min, long count){
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getAvg() {
        if(count == 0) {
            return BigDecimal.ZERO;
        }
        return getSum().divide(new BigDecimal(count), DEFAULT_SCALE, BigDecimal.ROUND_HALF_EVEN);
    }


    public BigDecimal getMax() {
        return max;
    }


    public BigDecimal getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TransactionStatistics)) {
            return false;
        }
        TransactionStatistics toCompare = (TransactionStatistics) obj;
        return Objects.equals(count, toCompare.count) &&
                Objects.equals(sum, toCompare.sum) &&
                Objects.equals(max, toCompare.max) &&
                Objects.equals(min, toCompare.min);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, sum, max, min);
    }

    public TransactionStatistics add(Transaction transaction) {
        return builder()
            .count(getCount() + 1)
            .sum(getSum().add(transaction.getAmount()))
            .min(getMin() != null ? getMin().min(transaction.getAmount()) : transaction.getAmount())
            .max(getMax() != null ? getMax().max(transaction.getAmount()) : transaction.getAmount())
            .build();
    }

    public TransactionStatistics add(TransactionStatistics transactionStatistics) {
        if (transactionStatistics.equals(DEFAULT)) {
            return this;
        }
        if (this.equals(DEFAULT)) {
            return transactionStatistics;
        }
        return builder()
                .count(getCount() + transactionStatistics.getCount())
                .sum(getSum().add(transactionStatistics.getSum()))
                .min(getMin() != null ? getMin().min(transactionStatistics.getMin()) : transactionStatistics.getMin())
                .max(getMax() != null ? getMax().max(transactionStatistics.getMax()) : transactionStatistics.getMax())
                .build();
    }

    public static TransactionStatistics.Builder builder() {
        return new TransactionStatistics.Builder();
    }


    public static final class Builder {
        private BigDecimal sum;
        private BigDecimal max;
        private BigDecimal min;
        private long count;

        private Builder(){}

        public Builder sum(BigDecimal sum){
            this.sum = sum;
            return this;
        }
        public Builder max(BigDecimal max){
            this.max = max;
            return this;
        }
        public Builder min(BigDecimal min){
            this.min = min;
            return this;
        }
        public Builder sum(String sum){
            this.sum =  new BigDecimal(sum);
            return this;
        }
        public Builder max(String max){
            this.max =  new BigDecimal(max);
            return this;
        }
        public Builder min(String min){
            this.min =  new BigDecimal(min);
            return this;
        }
        public Builder count(long count){
            this.count = count;
            return this;
        }

        public TransactionStatistics build() {
            return new TransactionStatistics(sum, max, min, count);
        }
    }
}

package com.n26.challenge.respository;

import com.n26.challenge.domain.AtomicReferenceWithTimestamp;
import com.n26.challenge.domain.Transaction;
import com.n26.challenge.domain.TransactionStatistics;
import com.n26.challenge.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.MINUTES;


/**
 * Created by renz on 12/17/2017.
 */
@Component
public class TransactionStatisticsCacheImpl implements TransactionStatisticsCache {

    private final static int DEFAULT_SIZE = 60;

    @Autowired
    private Supplier<Long> currentTimeRepository;

    private AtomicReferenceArray<AtomicReferenceWithTimestamp<TransactionStatistics>> atomicTransactions;

    @PostConstruct
    public void init() {
        atomicTransactions = new AtomicReferenceArray<>(DEFAULT_SIZE);
    }

    @Override
    public void addTransaction(Transaction transaction) throws TransactionException {
        AtomicReferenceWithTimestamp<TransactionStatistics> atomicReference =
                getByTimestamp(transaction.getTimestamp());
        atomicReference.updateAndGet(transactionStatistics -> transactionStatistics.add(transaction));
    }

    @Override
    public TransactionStatistics getLastMinute() {
        return getLastSixtySeconds().reduce(TransactionStatistics.DEFAULT, TransactionStatistics::add);
    }

    private AtomicReferenceWithTimestamp<TransactionStatistics> getByTimestamp(long timestamp)
            throws TransactionException {
        long duration = getDurationInSeconds(timestamp);
        if(!isTimestampValid(duration)) {
            throw new TransactionException();
        }
        int index = getIndex(duration);
        return atomicTransactions.updateAndGet(index, reference -> getDefaultIfInvalid(duration, reference));
    }

    private boolean isTimestampValid(long duration) {
        long now = currentTimeRepository.get();
        long toDuration = getDurationInSeconds(now);
        long fromDuration = getDurationInSeconds(now, 1, MINUTES);

        return duration > fromDuration && duration <= toDuration;
    }

    private long getDurationInSeconds(long timestamp) {
        return Duration.of(timestamp, ChronoUnit.MILLIS).get(ChronoUnit.SECONDS);
    }

    private long getDurationInSeconds(long timestamp, long offset, TemporalUnit unit) {
        return Duration.of(timestamp, ChronoUnit.MILLIS).minus(offset, unit).get(ChronoUnit.SECONDS);
    }

    private int getIndex(long duration) {
        return (int) (duration % atomicTransactions.length());
    }

    private AtomicReferenceWithTimestamp<TransactionStatistics> getDefaultIfInvalid(long timestamp,
                                                AtomicReferenceWithTimestamp<TransactionStatistics> atomicReference) {
        if (atomicReference == null || isExpired(timestamp, atomicReference)) {
            return new AtomicReferenceWithTimestamp<>(TransactionStatistics.DEFAULT, timestamp);
        }
        return atomicReference;
    }

    private boolean isExpired(long timestamp, AtomicReferenceWithTimestamp<TransactionStatistics> atomicReference) {
        return atomicReference.getTimestampInSeconds() < timestamp;
    }

    private Stream<TransactionStatistics> getLastSixtySeconds() {
        long now = currentTimeRepository.get();
        long toDuration = getDurationInSeconds(now);
        long fromDuration = getDurationInSeconds(now, 1, MINUTES);

        return IntStream.rangeClosed((int) fromDuration, (int) toDuration)
                .mapToObj(timestampInSeconds ->
                        getNullIfInvalid(timestampInSeconds, atomicTransactions.get(getIndex(timestampInSeconds))))
                .filter(Objects::nonNull)
                .map(AtomicReferenceWithTimestamp::getValue);
    }

    private AtomicReferenceWithTimestamp<TransactionStatistics> getNullIfInvalid(int timestampInSecond,
                                                 AtomicReferenceWithTimestamp<TransactionStatistics> atomicReference) {
        if (atomicReference == null || atomicReference.getTimestampInSeconds() != timestampInSecond) {
            return null;
        }
        return atomicReference;
    }
}

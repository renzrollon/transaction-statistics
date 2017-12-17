package com.n26.challenge.respository;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.domain.TransactionStatistics;
import com.n26.challenge.exception.TransactionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by renz on 12/17/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionStatisticsCacheImplTest {

    @InjectMocks
    private TransactionStatisticsCacheImpl transactionStatisticsCache;

    @Mock
    private CurrentTimeRepository currentTimeRepository;

    @Before
    public void setUp() {
        transactionStatisticsCache.init();
        when(currentTimeRepository.get()).thenReturn(System.currentTimeMillis());
    }

    @Test
    public void getLastMinute_noTransactions_shouldReturnDefaultStatistics() throws Exception {
        TransactionStatistics statistics = transactionStatisticsCache.getLastMinute();

        assertEquals(0, statistics.getCount());
        assertEquals(BigDecimal.ZERO, statistics.getSum());
        assertEquals(null, statistics.getMax());
        assertEquals(null, statistics.getMin());
        assertEquals(BigDecimal.ZERO, statistics.getAvg());
    }

    @Test
    public void getLastMinute_oneValidTransaction_shouldReturnCorrectValues() throws Exception {
        long currentTime = System.currentTimeMillis();
        BigDecimal amount = new BigDecimal("12.30");
        Transaction transaction = new Transaction(amount, currentTime);
        transactionStatisticsCache.addTransaction(transaction);
        TransactionStatistics statistics = transactionStatisticsCache.getLastMinute();

        assertEquals(1, statistics.getCount());
        assertEquals(amount, statistics.getSum());
        assertEquals(amount, statistics.getMax());
        assertEquals(amount, statistics.getMin());
        assertEquals(amount, statistics.getAvg());
    }

    @Test(expected = TransactionException.class)
    public void getLastMinute_oneOldTransaction_shouldReturnDefaultStatistics() throws Exception {
        long currentTime = System.currentTimeMillis();
        BigDecimal amount = new BigDecimal("12.30");
        Transaction transaction = new Transaction(amount, currentTime - 61000);
        transactionStatisticsCache.addTransaction(transaction);
        fail();
    }

    @Test(expected = TransactionException.class)
    public void getLastMinute_oneFutureTransaction_shouldReturnException() throws Exception {
        long currentTime = System.currentTimeMillis();
        BigDecimal amount = new BigDecimal("12.30");
        Transaction transaction = new Transaction(amount, currentTime + 10000);
        transactionStatisticsCache.addTransaction(transaction);
        fail();
    }

    @Test
    public void getLastMinute_twoValidTransactionsSameTime_shouldReturnCorrectValues() throws Exception {
        long currentTime = System.currentTimeMillis();
        BigDecimal amount = new BigDecimal("12.30");
        Transaction transaction = new Transaction(amount, currentTime);
        transactionStatisticsCache.addTransaction(transaction);

        BigDecimal anotherAmount = new BigDecimal("100");
        Transaction anotherTransaction = new Transaction(anotherAmount, currentTime);
        transactionStatisticsCache.addTransaction(anotherTransaction);


        TransactionStatistics statistics = transactionStatisticsCache.getLastMinute();

        assertEquals(2, statistics.getCount());
        assertEquals(amount.add(anotherAmount), statistics.getSum());
        assertEquals(amount.max(anotherAmount), statistics.getMax());
        assertEquals(amount.min(anotherAmount), statistics.getMin());
        assertEquals(new BigDecimal("56.15"), statistics.getAvg());
    }

    @Test
    public void getLastMinute_twoValidTransactionsDifferentTime_shouldReturnCorrectValues() throws Exception {
        long currentTime = System.currentTimeMillis();
        BigDecimal amount = new BigDecimal("12.3");
        Transaction transaction = new Transaction(amount, currentTime);
        transactionStatisticsCache.addTransaction(transaction);

        BigDecimal anotherAmount = new BigDecimal("100");
        Transaction anotherTransaction = new Transaction(anotherAmount, currentTime - 10000);
        transactionStatisticsCache.addTransaction(anotherTransaction);


        TransactionStatistics statistics = transactionStatisticsCache.getLastMinute();

        assertEquals(2, statistics.getCount());
        assertEquals(amount.add(anotherAmount), statistics.getSum());
        assertEquals(amount.max(anotherAmount), statistics.getMax());
        assertEquals(amount.min(anotherAmount), statistics.getMin());
        assertEquals(new BigDecimal("56.15"), statistics.getAvg());
    }
}
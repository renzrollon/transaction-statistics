package com.n26.challenge.respository;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.exception.TransactionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by renz on 12/17/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionRepositoryImplTest {

    @InjectMocks
    private TransactionRepositoryImpl transactionRepository;

    @Mock
    private TransactionStatisticsCache transactionStatisticsCache;

    @Test
    public void save_shouldCallCacheAddTransaction() throws TransactionException {
        Transaction mock = Mockito.mock(Transaction.class);
        transactionRepository.save(mock);

        verify(transactionStatisticsCache).addTransaction(mock);
    }
}
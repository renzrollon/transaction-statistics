package com.n26.challenge.service;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.exception.TransactionException;
import com.n26.challenge.respository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Created by renz on 12/17/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void createTransaction_shouldCallSaveFromRepository() throws TransactionException {
        Transaction mock = Mockito.mock(Transaction.class);
        transactionService.create(mock);

        verify(transactionRepository).save(mock);
    }

    @Test(expected = TransactionException.class)
    public void whenRepoSaveThrowException_shouldThrowException() throws TransactionException {
        doThrow(new TransactionException())
                .when(transactionRepository)
                .save(null);
        transactionService.create(null);
    }

}
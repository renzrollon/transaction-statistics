package com.n26.challenge.service;

import com.n26.challenge.respository.TransactionStatisticsCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by renz on 12/17/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionStatisticsServiceImplTest {

    @InjectMocks
    private TransactionStatisticsServiceImpl transactionStatisticsService;

    @Mock
    private TransactionStatisticsCache transactionStatisticsCache;

    @Test
    public void getDefaultStatistics_shouldCallGetLastMinute() {
        transactionStatisticsService.getDefaultStatistics();

        verify(transactionStatisticsCache).getLastMinute();
    }
}
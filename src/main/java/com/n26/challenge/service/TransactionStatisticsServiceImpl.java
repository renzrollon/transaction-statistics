package com.n26.challenge.service;

import com.n26.challenge.domain.TransactionStatistics;
import com.n26.challenge.respository.TransactionStatisticsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by renz on 12/17/2017.
 */
@Service
public class TransactionStatisticsServiceImpl implements TransactionStatisticsService {

    @Autowired
    private TransactionStatisticsCache transactionStatisticsCache;

    @Override
    public TransactionStatistics getDefaultStatistics() {
        return transactionStatisticsCache.getLastMinute();
    }
}

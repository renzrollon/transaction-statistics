package com.n26.challenge.respository;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by renz on 12/17/2017.
 */
@Component
public class TransactionRepositoryImpl implements TransactionRepository {

    @Autowired
    private TransactionStatisticsCache transactionStatisticsCache;

    @Override
    public void save(Transaction transaction) throws TransactionException {
        transactionStatisticsCache.addTransaction(transaction);
    }

}

package com.n26.challenge.respository;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.domain.TransactionStatistics;
import com.n26.challenge.exception.TransactionException;

/**
 * Created by renz on 12/17/2017.
 */
public interface TransactionStatisticsCache {

    void addTransaction(Transaction transaction) throws TransactionException;
    TransactionStatistics getLastMinute();

}

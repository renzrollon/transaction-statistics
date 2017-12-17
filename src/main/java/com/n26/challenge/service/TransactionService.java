package com.n26.challenge.service;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.exception.TransactionException;

/**
 * Created by renz on 12/17/2017.
 */
public interface TransactionService {
    void create(Transaction transaction) throws TransactionException;
}

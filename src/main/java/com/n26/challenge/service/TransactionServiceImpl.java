package com.n26.challenge.service;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.exception.TransactionException;
import com.n26.challenge.respository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by renz on 12/17/2017.
 */
@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    public TransactionRepository transactionRepository;

    @Override
    public void create(Transaction transaction) throws TransactionException {
        transactionRepository.save(transaction);
    }
}

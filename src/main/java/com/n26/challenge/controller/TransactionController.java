package com.n26.challenge.controller;

import com.n26.challenge.domain.Transaction;
import com.n26.challenge.exception.TransactionException;
import com.n26.challenge.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by renz on 12/17/2017.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody Transaction transaction) throws TransactionException {
        transactionService.create(transaction);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

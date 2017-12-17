package com.n26.challenge.controller;

import com.n26.challenge.domain.TransactionStatistics;
import com.n26.challenge.service.TransactionStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by renz on 12/17/2017.
 */
@RestController
@RequestMapping("/statistics")
public class TransactionStatisticsController {

    @Autowired
    private TransactionStatisticsService transactionStatisticsService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<TransactionStatistics> getDefaultStatistics() {
        TransactionStatistics transactionStatistics = transactionStatisticsService.getDefaultStatistics();

        return new ResponseEntity<>(transactionStatistics, HttpStatus.OK);
    }
}

package com.n26.challenge.controller;

import com.google.gson.Gson;
import com.n26.challenge.domain.Transaction;
import com.n26.challenge.exception.TransactionException;
import com.n26.challenge.service.TransactionService;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by renz on 12/17/2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    private final static String TRANSACTION_ENDPOINT = "/transactions";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void validRequest_shouldReturn201() throws Exception {
        doNothing()
                .when(transactionService).create(any(Transaction.class));

        assertStatus(status().isCreated());
    }

    @Test()
    public void olderThan60Secs_shouldReturn204() throws Exception {
        doThrow(TransactionException.class)
                .when(transactionService).create(any(Transaction.class));

        assertStatus(status().isNoContent());
    }

    private void assertStatus(ResultMatcher resultMatcher) throws Exception {
        mockMvc.perform(post(TRANSACTION_ENDPOINT)
                        .content(createValidTransactionRequest("12.3", "1478192204000"))
                        .contentType("application/json")
        ).andExpect(resultMatcher);
    }

    private String createValidTransactionRequest(String amount, String timestamp) {
        Transaction transaction = new Transaction(amount, timestamp);
        return new Gson().toJson(transaction);
    }

}
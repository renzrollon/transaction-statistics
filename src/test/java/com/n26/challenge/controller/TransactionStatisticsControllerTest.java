package com.n26.challenge.controller;

import com.n26.challenge.domain.TransactionStatistics;
import com.n26.challenge.service.TransactionStatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by renz on 12/17/2017.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionStatisticsController.class)
public class TransactionStatisticsControllerTest {

    private final static String TRANSACTION_STATISTICS_ENDPOINT = "/statistics";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionStatisticsService transactionStatisticsService;

    @Test
    public void getDefaultStatistics_noTransactions() throws Exception {

        when(transactionStatisticsService.getDefaultStatistics())
                .thenReturn(TransactionStatistics.builder()
                        .count(0)
                        .sum("0")
                        .build());

        BigDecimal defaultMinMax = null;
        mockMvc.perform(get(TRANSACTION_STATISTICS_ENDPOINT)
                        .accept("application/json")
        ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("count", is(0)))
                .andExpect(jsonPath("sum", is(0)))
                .andExpect(jsonPath("avg", is(0)))
                .andExpect(jsonPath("max", is(defaultMinMax)))
                .andExpect(jsonPath("min", is(defaultMinMax)));
    }

    @Test
    public void getDefaultStatistics_oneTransaction() throws Exception {
        when(transactionStatisticsService.getDefaultStatistics())
                .thenReturn(TransactionStatistics.builder()
                        .count(1)
                        .sum("100")
                        .max("100")
                        .min("100")
                        .build());

        mockMvc.perform(get(TRANSACTION_STATISTICS_ENDPOINT)
                        .accept("application/json")
        ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("count", is(1)))
                .andExpect(jsonPath("sum", is(100)))
                .andExpect(jsonPath("avg", is(100.00)))
                .andExpect(jsonPath("max", is(100)))
                .andExpect(jsonPath("min", is(100)));
    }

    @Test
    public void getDefaultStatistics_tenTransactions() throws Exception {
        when(transactionStatisticsService.getDefaultStatistics())
                .thenReturn(TransactionStatistics.builder()
                        .count(10)
                        .sum("1000")
                        .max("200")
                        .min("50")
                        .build());

        mockMvc.perform(get(TRANSACTION_STATISTICS_ENDPOINT)
                .accept("application/json")
        ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("count", is(10)))
                .andExpect(jsonPath("sum", is(1000)))
                .andExpect(jsonPath("avg", is(100.00)))
                .andExpect(jsonPath("max", is(200)))
                .andExpect(jsonPath("min", is(50)));
    }
}
package com.pinakibarik.coinexchangeapi.app;

import com.pinakibarik.coinexchangeapi.controller.ApplicationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CoinExchangeApiApplicationTests {
    @Autowired
    private ApplicationController applicationController;

    @Test
    void contextLoads() {
        assertThat(applicationController).isNotNull();
    }
}

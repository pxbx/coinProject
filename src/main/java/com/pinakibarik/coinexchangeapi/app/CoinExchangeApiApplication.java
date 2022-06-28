package com.pinakibarik.coinexchangeapi.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author pxbx
 * @since 2022-06-27
 */
@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(value = "com.pinakibarik.coinexchangeapi")
public class CoinExchangeApiApplication {
    private static final Logger logger = LogManager.getLogger(CoinExchangeApiApplication.class);

    private static ConfigurableApplicationContext configurableApplicationContext;

    public static void main(String[] args) {
        configurableApplicationContext = SpringApplication.run(CoinExchangeApiApplication.class, args);
        logger.info("APPLICATION STARTED");
    }
}

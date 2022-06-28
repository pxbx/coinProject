package com.pinakibarik.coinexchangeapi.coinrepo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CoinRepository {
    private static final Logger logger = LogManager.getLogger(CoinRepository.class);

    private static AtomicInteger ONE;
    private static AtomicInteger FIVE;
    private static AtomicInteger TEN;
    private static AtomicInteger TWENTY_FIVE;

    private static AtomicInteger R_ONE;
    private static AtomicInteger R_FIVE;
    private static AtomicInteger R_TEN;
    private static AtomicInteger R_TWENTY_FIVE;

    private static Map<String, Double> REQUEST_IDENTIFIERS = new HashMap<>();

    @Value("${app.settings.initialCoins:100}")
    int initCoins;

    private static Double availableAmount = 0.0D;

    public static int getRemaining10() {
        return TEN.get();
    }

    public static int getRemaining25() {
        return TWENTY_FIVE.get();
    }

    public static int getRemaining01() {
        return ONE.get();
    }

    public static int getRemaining05() {
        return FIVE.get();
    }

    @PostConstruct
    public void init() {
        ONE = new AtomicInteger(initCoins);
        FIVE = new AtomicInteger(initCoins);
        TEN = new AtomicInteger(initCoins);
        TWENTY_FIVE = new AtomicInteger(initCoins);

        R_ONE = new AtomicInteger(0);
        R_FIVE = new AtomicInteger(0);
        R_TEN = new AtomicInteger(0);
        R_TWENTY_FIVE = new AtomicInteger(0);

        updateAvailableAmount();

        logger.info("REPOSITORY INITIATED");
    }

    private static void updateAvailableAmount() {
        availableAmount = ONE.get() * 0.01 + FIVE.get() * 0.05 + TEN.get() * 0.10 + TWENTY_FIVE.get() * 0.25;
        logger.info("AVAILABLE AMOUNT {}", availableAmount);
    }

    public static Double getAvailableAmount() {
        return availableAmount;
    }

    public static Double getReserveAmount(String requestId) {
        return REQUEST_IDENTIFIERS.get(requestId);
    }

    public static synchronized boolean claim(CoinRequest coinRequest) {
        REQUEST_IDENTIFIERS.remove(coinRequest.getRequestId());
        return true;
    }

    private void printCurrent() {
        logger.info("c25 > {}, c10 > {}, c05 > {}, c01 > {}, TOTAL = {}",
                TWENTY_FIVE, TEN, FIVE, ONE, availableAmount);
    }

    public static synchronized boolean reserve(CoinRequest coinRequest) {
        List<CoinRequest.Coin> coins = coinRequest.getCoins();
        REQUEST_IDENTIFIERS.put(coinRequest.getRequestId(), coinRequest.getAmount());

        if (coinRequest.getAmount() > availableAmount) {
            logger.info("NOT ENOUGH COINS");
            return false;
        }

        boolean isFail = false;
        for (int i = 0; i < coins.size(); i++) {
            int count = coins.get(i).getCount();
            if (coins.get(i).getValue() == 0.01) {
                if (ONE.get() >= count) {
                    ONE.set(ONE.get() - count);
                    R_ONE.set(count);
                } else {
                    isFail = true;
                    break;
                }
            } else if (coins.get(i).getValue() == 0.05) {
                if (FIVE.get() >= count) {
                    FIVE.set(FIVE.get() - count);
                    R_FIVE.set(count);
                } else {
                    isFail = true;
                    break;
                }
            } else if (coins.get(i).getValue() == 0.10) {
                if (TEN.get() >= count) {
                    TEN.set(TEN.get() - count);
                    R_TEN.set(count);
                } else {
                    isFail = true;
                    break;
                }
            } else if (coins.get(i).getValue() == 0.25) {
                if (TWENTY_FIVE.get() >= count) {
                    TWENTY_FIVE.set(TWENTY_FIVE.get() - count);
                    R_TWENTY_FIVE.set(count);
                } else {
                    isFail = true;
                    break;
                }
            }
        }

        if (isFail) {
            FIVE.set(FIVE.get() + R_FIVE.get());
            ONE.set(ONE.get() + R_ONE.get());
            TEN.set(TEN.get() + R_TEN.get());
            TWENTY_FIVE.set(TWENTY_FIVE.get() + R_TWENTY_FIVE.get());

            REQUEST_IDENTIFIERS.remove(coinRequest.getRequestId());
            return false;
        } else {
            updateAvailableAmount();
        }
        return true;
    }
}

package com.pinakibarik.coinexchangeapi.coinrepo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CoinRepository {
    private static final Logger logger = LogManager.getLogger(CoinRepository.class);

    private AtomicInteger ONE;
    private AtomicInteger FIVE;
    private AtomicInteger TEN;
    private AtomicInteger TWENTY_FIVE;

    private AtomicInteger R_ONE;
    private AtomicInteger R_FIVE;
    private AtomicInteger R_TEN;
    private AtomicInteger R_TWENTY_FIVE;

    private List<String> REQUEST_IDENTIFIERS = new ArrayList<>();

    @Value("${app.settings.initialCoins:100}")
    int initCoins;

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

        logger.info("REPOSITORY INITIATED");
    }

    public synchronized boolean reserve(CoinRequest coinRequest) {
        List<CoinRequest.Coin> coins = coinRequest.getCoins();
        REQUEST_IDENTIFIERS.add(coinRequest.getRequestId());

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
            } else if (coins.get(i).getValue() == 0.01) {
                if (TEN.get() >= count) {
                    TEN.set(TEN.get() - count);
                    R_TEN.set(count);
                } else {
                    isFail = true;
                    break;
                }
            } else if (coins.get(i).getValue() == 0.01) {
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
        }
        return true;
    }
}

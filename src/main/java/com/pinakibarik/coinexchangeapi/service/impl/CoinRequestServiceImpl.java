package com.pinakibarik.coinexchangeapi.service.impl;

import com.pinakibarik.coinexchangeapi.coinrepo.CoinRepository;
import com.pinakibarik.coinexchangeapi.coinrepo.CoinRequest;
import com.pinakibarik.coinexchangeapi.context.RequestContext;
import com.pinakibarik.coinexchangeapi.exception.ApplicationException;
import com.pinakibarik.coinexchangeapi.io.response.CoinSegments;
import com.pinakibarik.coinexchangeapi.service.CoinRequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinRequestServiceImpl implements CoinRequestService {
    private static final Logger logger = LogManager.getLogger(CoinRequestServiceImpl.class);

    @Autowired
    RequestContext requestContext;

    @Override
    public CoinRequest getCoinRequest(int amount) throws ApplicationException {
        List<CoinRequest.Coin> coins = getCoinSegments(amount);
        CoinRequest coinRequest = new CoinRequest(requestContext.getRequestId());
        coinRequest.addCoins(coins);

        return coinRequest;
    }

    private List<CoinRequest.Coin> getCoinSegments(int amount) throws ApplicationException {
        Double availableAmount = CoinRepository.getAvailableAmount();
        if (availableAmount < amount)
            throw new ApplicationException("0007");
        else {
            int retry = 3;
            int iteration = 0;
            List<CoinRequest.Coin> coinSegments = new ArrayList<>();

            // RIGHT NOW - DO/WHILE IS UNNECESSARY
            // HOWEVER, IN FUTURE, MORE COMPLEX 'CHECK' LOGIC CAN BE IMPLEMENTED WITH RETRY
            do {
                coinSegments.clear();
                iteration++;

                double damount = amount;
                double n25 = amount / 0.25;
                if (CoinRepository.getRemaining25() < ((Double) n25).intValue())
                    n25 = CoinRepository.getRemaining25();
                damount = damount - 0.25 * n25;

                double n10 = damount / 0.10;
                if (CoinRepository.getRemaining10() < ((Double) n10).intValue())
                    n10 = CoinRepository.getRemaining10();
                damount = damount - 0.10 * n10;

                double n05 = damount / 0.05;
                if (CoinRepository.getRemaining05() < ((Double) n05).intValue())
                    n05 = CoinRepository.getRemaining05();
                damount = damount - 0.05 * n05;

                double n01 = damount / 0.01;
                if (CoinRepository.getRemaining01() < ((Double) n01).intValue())
                    n25 = CoinRepository.getRemaining01();
                damount = damount - 0.01 * n01;

                logger.info("CALCULATED FOR {} > {}, {}, {}, {}", amount, n25, n10, n05, n01);

                coinSegments.add(new CoinRequest.Coin(0.25D, ((Double) n25).intValue()));
                coinSegments.add(new CoinRequest.Coin(0.10D, ((Double) n10).intValue()));
                coinSegments.add(new CoinRequest.Coin(0.05D, ((Double) n05).intValue()));
                coinSegments.add(new CoinRequest.Coin(0.01D, ((Double) n01).intValue()));

                if (damount == 0.0)
                    break;
            } while (iteration < 3);

            return coinSegments;
        }
    }
}

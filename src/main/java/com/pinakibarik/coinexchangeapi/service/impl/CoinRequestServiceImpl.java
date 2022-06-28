package com.pinakibarik.coinexchangeapi.service.impl;

import com.pinakibarik.coinexchangeapi.coinrepo.CoinRequest;
import com.pinakibarik.coinexchangeapi.context.RequestContext;
import com.pinakibarik.coinexchangeapi.service.CoinRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinRequestServiceImpl implements CoinRequestService {
    @Autowired
    RequestContext requestContext;

    @Override
    public CoinRequest getCoinRequest(int amount) {
        CoinRequest request = new CoinRequest(requestContext.getRequestId());



        return null;
    }
}

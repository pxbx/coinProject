package com.pinakibarik.coinexchangeapi.service;

import com.pinakibarik.coinexchangeapi.coinrepo.CoinRequest;

public interface CoinRequestService {
    CoinRequest getCoinRequest(int amount);
}

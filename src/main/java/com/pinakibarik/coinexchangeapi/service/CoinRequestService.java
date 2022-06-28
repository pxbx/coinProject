package com.pinakibarik.coinexchangeapi.service;

import com.pinakibarik.coinexchangeapi.coinrepo.CoinRequest;
import com.pinakibarik.coinexchangeapi.exception.ApplicationException;

public interface CoinRequestService {
    CoinRequest getCoinRequest(int amount) throws ApplicationException;
}

package com.pinakibarik.coinexchangeapi.service;

import com.pinakibarik.coinexchangeapi.exception.ApplicationException;
import com.pinakibarik.coinexchangeapi.io.request.RequestObject;

public interface ValidationService {
    RequestObject validateRequest(String payload) throws ApplicationException;
}

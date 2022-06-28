package com.pinakibarik.coinexchangeapi.service;

import com.pinakibarik.coinexchangeapi.exception.ApplicationException;
import com.pinakibarik.coinexchangeapi.io.response.ResponseObject;

public interface RequestProcessService {
    String processRequest(String payload) throws ApplicationException;
}

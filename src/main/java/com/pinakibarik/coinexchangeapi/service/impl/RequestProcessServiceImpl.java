package com.pinakibarik.coinexchangeapi.service.impl;

import com.pinakibarik.coinexchangeapi.context.RequestContext;
import com.pinakibarik.coinexchangeapi.exception.ApplicationException;
import com.pinakibarik.coinexchangeapi.io.request.RequestObject;
import com.pinakibarik.coinexchangeapi.io.response.CoinSegments;
import com.pinakibarik.coinexchangeapi.io.response.ResponseObject;
import com.pinakibarik.coinexchangeapi.service.RequestProcessService;
import com.pinakibarik.coinexchangeapi.service.ValidationService;
import com.pinakibarik.coinexchangeapi.utils.JsonUtility;
import com.pinakibarik.coinexchangeapi.utils.MessageUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestProcessServiceImpl implements RequestProcessService {
    private static final Logger logger = LogManager.getLogger(RequestProcessServiceImpl.class);

    @Autowired
    RequestContext requestContext;
    @Autowired
    ValidationService validateService;

    @Override
    public String processRequest(String payload) throws ApplicationException {
        RequestObject requestObject = validateService.validateRequest(payload);
        logger.info("REQUEST WAS VALIDATED");

        int billAmount = requestObject.getAmount();
        logger.info("BILL AMOUNT IS {}", billAmount);

        if (billAmount == 0) {
            return createResponse("0000", new ArrayList<>());
        } else {
            return createResponse("9999", new ArrayList<>());
        }
    }

    private String createResponse(String responseCode, List<CoinSegments> coinSegmentsList) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setRequestId(requestContext.getRequestId());
        responseObject.setCoinSegments(coinSegmentsList);
        responseObject.setResponseCode(responseCode);
        responseObject.setResponseMessage(MessageUtility.getMessage(responseCode));

        return JsonUtility.serialize(responseObject);
    }
}

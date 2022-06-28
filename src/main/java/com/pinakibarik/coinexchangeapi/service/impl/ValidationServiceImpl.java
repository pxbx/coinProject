package com.pinakibarik.coinexchangeapi.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.pinakibarik.coinexchangeapi.exception.ApplicationException;
import com.pinakibarik.coinexchangeapi.exception.InitializationException;
import com.pinakibarik.coinexchangeapi.io.request.RequestObject;
import com.pinakibarik.coinexchangeapi.service.ValidationService;
import com.pinakibarik.coinexchangeapi.utils.JsonUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ValidationServiceImpl implements ValidationService {
    private static final Logger logger = LogManager.getLogger(ValidationServiceImpl.class);

    @Value("${app.settings.bills.validBills:0}")
    String validBills;

    private static List<Integer> VALID_BILLS = new ArrayList<>();

    @PostConstruct
    public void init() {
        Arrays.asList(validBills.split(",", -1))
                .stream().forEach(
                        bill -> {
                            try {
                                VALID_BILLS.add(Integer.parseInt(bill.trim()));
                            } catch (Exception ex) {
                                throw new InitializationException();
                            }
                        }
                );
        VALID_BILLS.add(0);
        logger.info("VALID BILLS LOADED {}", VALID_BILLS);
    }

    public RequestObject validateRequest(String payload) throws ApplicationException {
        RequestObject requestObject = new RequestObject();

        try {
            Map<DeserializationFeature, Boolean> features = new HashMap<>();
            features.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            features.put(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
            requestObject = JsonUtility.deserialize(payload, RequestObject.class, features);
        } catch (Exception ex) {
            throw new ApplicationException("0004");
        }

        Integer amount = requestObject.getAmount();
        if (amount == null)
            throw new ApplicationException("0004");
        if (amount < 0)
            throw new ApplicationException("0006");

        if (!VALID_BILLS.contains(amount))
            throw new ApplicationException("0005");

        return requestObject;
    }
}

package com.pinakibarik.coinexchangeapi.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.pinakibarik.coinexchangeapi.coinrepo.CoinRepository;
import com.pinakibarik.coinexchangeapi.coinrepo.CoinRequest;
import com.pinakibarik.coinexchangeapi.context.RequestContext;
import com.pinakibarik.coinexchangeapi.exception.ApplicationException;
import com.pinakibarik.coinexchangeapi.io.request.RequestObject;
import com.pinakibarik.coinexchangeapi.service.CoinRequestService;
import com.pinakibarik.coinexchangeapi.service.RequestProcessService;
import com.pinakibarik.coinexchangeapi.service.ValidationService;
import com.pinakibarik.coinexchangeapi.utils.JsonUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockStatic;

@RunWith(SpringJUnit4ClassRunner.class)
public class RequestProcessServiceImplTest {
    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public RequestProcessService requestProcessService() {
            return new RequestProcessServiceImpl();
        }
    }

    @MockBean
    RequestContext requestContext;

    @MockBean
    ValidationService validateService;

    @MockBean
    CoinRequestService coinRequestService;

    @Autowired
    RequestProcessService requestProcessService;

    @Before
    public void init() {
        requestContext = new RequestContext(UUID.randomUUID());
    }

    @Test(expected = ApplicationException.class)
    public void processRequest_genericValidationException() {
        Mockito.when(validateService.validateRequest(Mockito.anyString()))
                .thenThrow(new ApplicationException("9999"));
        // Expecting exception - so setting any string - does not matter
        requestProcessService.processRequest(new String());
    }

    @Test(expected = Test.None.class)
    public void processRequest_noValidationException_Amount_Zero() throws UnrecognizedPropertyException, InvalidFormatException {
        Mockito.when(validateService.validateRequest(Mockito.anyString()))
                .thenReturn(deserialize(GOOD_PAYLOAD_0));
        // Expecting exception - so setting any string - does not matter
        String response = requestProcessService.processRequest(new String());
        assertTrue(response.contains("0000"));
    }

    @Test(expected = Test.None.class)
    public void processRequest_noValidationException_Amount_NonZero_BelowAvailable() throws UnrecognizedPropertyException, InvalidFormatException {
        Mockito.when(validateService.validateRequest(Mockito.anyString()))
                .thenReturn(deserialize(GOOD_PAYLOAD_100));
        Mockito.when(coinRequestService.getCoinRequest(Mockito.anyInt()))
                .thenReturn(new CoinRequest(requestContext.getRequestId()));

        try (MockedStatic<CoinRepository> mockedStatic = Mockito.mockStatic(CoinRepository.class)) {
            mockedStatic.when(() -> CoinRepository.getAvailableAmount()).thenReturn(10.0D);
            try {
                // Expecting exception - so setting any string - does not matter
                requestProcessService.processRequest(GOOD_PAYLOAD_100);
            } catch (Exception ex) {
                assertTrue(ex instanceof ApplicationException);
                assertTrue("0007".equalsIgnoreCase(((ApplicationException) ex).getErrorCode()));
            }
        }
    }

    private static String GOOD_PAYLOAD_0 = " {    \n" +
            "    \"coinExchangeRequest\": {    \n" +
            "        \"billAmount\":       \"0\"\n" +
            "    }  \n" +
            " }";
    private static String GOOD_PAYLOAD_100 = " {    \n" +
            "    \"coinExchangeRequest\": {    \n" +
            "        \"billAmount\":       \"100\"\n" +
            "    }  \n" +
            " }";

    private static RequestObject deserialize(String payload) throws UnrecognizedPropertyException, InvalidFormatException {
        Map<DeserializationFeature, Boolean> features = new HashMap<>();
        features.put(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return JsonUtility.deserialize(payload, RequestObject.class, features);
    }
}
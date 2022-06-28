package com.pinakibarik.coinexchangeapi.controller;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.pinakibarik.coinexchangeapi.context.RequestContext;
import com.pinakibarik.coinexchangeapi.exception.ApplicationException;
import com.pinakibarik.coinexchangeapi.io.response.ResponseObject;
import com.pinakibarik.coinexchangeapi.utils.JsonUtility;
import com.pinakibarik.coinexchangeapi.utils.MessageUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pxbx
 * @since 2022-06-28
 */
@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationControllerAdvise {
    private static final Logger logger = LogManager.getLogger(ApplicationControllerAdvise.class);

    @Autowired
    RequestContext requestContext;

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // SOME STANDARD EXAMPLES
        if (ex instanceof HttpMediaTypeNotSupportedException) {
            return new ResponseEntity<>(createErrorResponse("0001"), headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            return new ResponseEntity<>(createErrorResponse("0002"), headers, HttpStatus.METHOD_NOT_ALLOWED);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return new ResponseEntity<>(createErrorResponse("0003"), headers, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof ApplicationException) {
            return new ResponseEntity<>(
                    createErrorResponse(((ApplicationException) ex).getErrorCode()), headers, HttpStatus.OK);
        } else {
            logger.error("UNEXPECTED EXCEPTION", ex);
            return new ResponseEntity<>(createErrorResponse("9999"), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param responseCode
     * @return
     */
    private String createErrorResponse(String responseCode) {
        return createErrorResponse(responseCode, null);
    }

    /**
     * @param responseCode
     * @param exception
     * @return
     */
    private String createErrorResponse(String responseCode, Exception exception) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setRequestId(requestContext.getRequestId());

        Map<SerializationFeature, Boolean> features = new HashMap<>();
        features.put(SerializationFeature.WRAP_ROOT_VALUE, true);

        if (StringUtils.isNotBlank(responseCode)) {
            responseObject.setResponseCode(responseCode);
            responseObject.setResponseMessage(MessageUtility.getMessage(responseCode));
        } else {
            responseObject.setResponseCode(responseCode);
            if (exception != null)
                responseObject.setResponseMessage(exception.toString());
            else
                responseObject.setResponseMessage(MessageUtility.getMessage("9999"));
        }

        return JsonUtility.serialize(responseObject, features);
    }
}

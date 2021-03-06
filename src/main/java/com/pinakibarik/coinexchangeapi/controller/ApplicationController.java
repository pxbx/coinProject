package com.pinakibarik.coinexchangeapi.controller;

import com.pinakibarik.coinexchangeapi.context.RequestContext;
import com.pinakibarik.coinexchangeapi.service.RequestProcessService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pxbx
 * @since 2022-06-28
 */
@RestController
@RequestMapping("${key.server.context.root}")
public class ApplicationController {
    private static final Logger logger = LogManager.getLogger(ApplicationController.class);

    @Autowired
    RequestContext requestContext;

    @Autowired
    RequestProcessService requestProcessService;

    /**
     * @param payload
     * @return
     */
    @PostMapping(
            value = "/getCoinExchange",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getCoinExchange(@RequestBody String payload) {
        logger.info("RAW REQUEST {}", payload);
        logger.info("REQUEST CONTEXT INITIALIZED - REQUEST ID {}", requestContext);

        String response = requestProcessService.processRequest(payload);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

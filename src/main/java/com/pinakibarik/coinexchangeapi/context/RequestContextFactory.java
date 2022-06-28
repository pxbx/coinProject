package com.pinakibarik.coinexchangeapi.context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Configuration
public class RequestContextFactory {
    private static final Logger logger = LogManager.getLogger(RequestContextFactory.class);

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
    public UUID requestUUID() {
        return UUID.randomUUID();
    }

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RequestContext requestContext(final HttpServletRequest httpServletRequest) {
        RequestContext requestContext = new RequestContext(requestUUID());
        requestContext.setEndPoint(httpServletRequest.getRequestURI()
                .substring(httpServletRequest.getRequestURI().lastIndexOf("/")));
        logger.info("[CONTEXT] CONTEXT WAS INITIALIZED {}", requestContext);

        return requestContext;
    }
}

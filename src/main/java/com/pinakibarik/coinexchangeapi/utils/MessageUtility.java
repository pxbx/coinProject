package com.pinakibarik.coinexchangeapi.utils;

import com.pinakibarik.coinexchangeapi.conditions.MessageLoaderCondition;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Component
@Conditional(MessageLoaderCondition.class)
public class MessageUtility {
    private static final Logger logger = LogManager.getLogger(MessageUtility.class);

    private static Map<String, String> MESSAGES = new HashMap<>();

    @Value("${app.settings.properties.messageKeyProperty}")
    String file;

    public static String getMessage(String key) {
        String string = MESSAGES.get(key);
        if (StringUtils.isNotBlank(string))
            return string;
        else
            return StringUtils.EMPTY;
    }

    @PostConstruct
    public void loadProperties() {
        logger.info("LOADING PROPERTIES FROM {}", file);

        if (StringUtils.isNotBlank(file)) {
            try {
                PropertiesFactoryBean bean = new PropertiesFactoryBean();
                bean.setLocation(new ClassPathResource(file));
                bean.afterPropertiesSet();

                Properties messages = bean.getObject();
                Set<Object> keys = messages.keySet();

                keys.forEach(
                        key -> MESSAGES.put(key.toString(), messages.getProperty(key.toString()))
                );

                logger.info("MESSAGES LOADED {}", MESSAGES);
            } catch (Exception ex) {
                logger.warn("UNABLE TO LOAD PROPERTIES");
            }
        }
    }
}

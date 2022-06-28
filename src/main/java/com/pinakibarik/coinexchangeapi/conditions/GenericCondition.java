package com.pinakibarik.coinexchangeapi.conditions;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericCondition implements Condition {
    private String property;

    public GenericCondition(String property) {
        this.property = property;
    }

    private static List<String> YES_VALUES = new ArrayList() {{
        add("true");
        add("t");
        add("yes");
        add("y");
    }};

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (StringUtils.isBlank(property))
            return false;

        String value = context.getEnvironment().getProperty(property.trim());

        if (StringUtils.isBlank(value))
            return false;

        if (YES_VALUES.contains(value.toLowerCase()))
            return true;

        return false;
    }
}

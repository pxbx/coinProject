package com.pinakibarik.coinexchangeapi.conditions;

public class MessageLoaderCondition extends GenericCondition {
    private static final String PROPERTY = "app.settings.loadMessageKeys";

    public MessageLoaderCondition() {
        super(PROPERTY);
    }
}

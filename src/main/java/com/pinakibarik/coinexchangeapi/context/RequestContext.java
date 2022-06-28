package com.pinakibarik.coinexchangeapi.context;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class RequestContext {
    private UUID uuid;
    private Map<String, Object> contextObjects;
    private String endPoint;

    private RequestContext() {
    }

    public String getRequestId() {
        return uuid.toString();
    }

    public RequestContext(UUID requestUUID) {
        this.uuid = requestUUID;
    }

    @Override
    public String toString() {
        return "RequestContext{" +
                "uuid=" + uuid.toString() +
                ", endPoint='" + endPoint + '\'' +
                '}';
    }
}

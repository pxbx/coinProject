package com.pinakibarik.coinexchangeapi.io.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonRootName(value = "coinExchangeResponse")
public class ResponseObject {
    private String requestId;

    @JsonProperty("coinSegments")
    private List<CoinSegments> coinSegments = new ArrayList<>();

    private String responseCode;
    private String responseMessage;
}

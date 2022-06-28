package com.pinakibarik.coinexchangeapi.io.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonRootName(value = "coinExchangeRequest")
public class RequestObject {
    @JsonProperty("billAmount")
    private Integer amount;
}

package com.pinakibarik.coinexchangeapi.io.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinSegments {
    Double denomination;
    int quantity;

    public CoinSegments(Double denomination, int quantity){
        this.quantity = quantity;
        this.denomination = denomination;
    }
}

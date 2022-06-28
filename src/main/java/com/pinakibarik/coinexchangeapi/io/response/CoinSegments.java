package com.pinakibarik.coinexchangeapi.io.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinSegments {
    BigDecimal denomination;
    int quantity;
}

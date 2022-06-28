package com.pinakibarik.coinexchangeapi.coinrepo;

import lombok.Data;

import java.util.List;

@Data
public class CoinRequest {
    private List<Coin> coins;
    private String requestId;

    private CoinRequest() {
    }

    public CoinRequest(String requestId) {
        this.requestId = requestId;
    }

    @Data
    static class Coin {
        private double value;
        private int count;
    }
}

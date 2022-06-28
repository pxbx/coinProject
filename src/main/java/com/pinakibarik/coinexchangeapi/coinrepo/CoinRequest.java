package com.pinakibarik.coinexchangeapi.coinrepo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class CoinRequest {
    private List<Coin> coins = new ArrayList<>();
    private String requestId;

    @Setter(AccessLevel.NONE)
    private Double amount = 0.0D;

    private CoinRequest() {
    }

    public CoinRequest(String requestId) {
        this.requestId = requestId;
    }

    private void setCoins(List<Coin> coins) {
    }

    public void addCoins(List<Coin> coins) {
        this.coins.addAll(coins);
        for (Coin coin : coins) {
            amount = amount + coin.count * coin.value;
        }
    }

    @Data
    public static class Coin {
        private Coin() {
        }

        public Coin(double value, int count) {
            this.count = count;
            this.value = value;
        }

        private double value;
        private int count;
    }
}

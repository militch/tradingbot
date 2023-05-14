package com.github.militch.tradingbot.restapi.core;

public enum TradingSymbol {
    BTCUSDT("BTCUSDT", 8);

    private final String symbol;
    private final int decimals;

    TradingSymbol(String symbol, int decimals) {
        this.symbol = symbol;
        this.decimals = decimals;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getDecimals() {
        return decimals;
    }
}

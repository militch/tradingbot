package com.github.militch.tradingbot.restapi.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class NumberUtil {

    public static BigInteger scalePrice(String price, int decimals){
        if (price == null) return null;
        BigDecimal priceObj = new BigDecimal(price);
        BigDecimal cover = BigDecimal.TEN.pow(decimals);
        priceObj = priceObj.multiply(cover);
        return priceObj.toBigInteger();
    }
    public static BigDecimal formatPrice(BigInteger price, int decimals){
        if (price == null) return null;
        BigDecimal priceObj = new BigDecimal(price);
        BigDecimal cover = BigDecimal.TEN.pow(decimals);
        priceObj = priceObj.divide(cover, 2, RoundingMode.HALF_UP);
        return priceObj;
    }
}

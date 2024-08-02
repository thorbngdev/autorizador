package com.challenge.autorizador.utils;

import java.math.BigDecimal;

public class TransactionUtils {

    public static boolean hasBalance(BigDecimal accountAmount, BigDecimal totalAmount) {
        return accountAmount.compareTo(totalAmount) >= 0;
    }

    public static boolean isNumericMcc(String mcc) {
        if (mcc == null || mcc.isEmpty()) {
            return false;
        }

        for (char c : mcc.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    public static String normalizeMerchantName(String merchantName) {
        if (merchantName == null) {
            return null;
        }
        return merchantName.trim().replaceAll("\\s+", " ");
    }
}

package com.challenge.autorizador.enumerate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum BalanceType {
    FOOD("5411", "5412"),
    MEAL("5811", "5812"),
    CASH();

    private final Set<String> mccCodes;

    BalanceType(String... mccCodes) {
        this.mccCodes = new HashSet<>(Arrays.asList(mccCodes));
    }

    public static BalanceType fromMcc(String mcc) {
        return Arrays.stream(values())
                .filter(balanceType -> balanceType.mccCodes.contains(mcc))
                .findFirst()
                .orElse(CASH);
    }
}

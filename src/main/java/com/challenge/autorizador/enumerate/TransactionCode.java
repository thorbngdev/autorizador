package com.challenge.autorizador.enumerate;

public enum TransactionCode {
    APPROVED("00"),
    REJECTED("07"),
    INSUFFICIENT_BALANCE("51");

    private String code;

    TransactionCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}

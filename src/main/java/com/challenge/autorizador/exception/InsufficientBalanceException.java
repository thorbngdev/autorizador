package com.challenge.autorizador.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InsufficientBalanceException extends RuntimeException {

    private String accountId;

    public InsufficientBalanceException(String accountId) {
        super(String.format("Insufficient balance for accountId: %s", accountId));
        this.accountId = accountId;
    }

}

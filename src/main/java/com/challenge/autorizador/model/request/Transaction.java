package com.challenge.autorizador.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private String account;

    private BigDecimal totalAmount;

    private String mcc;

    private String merchant;

}

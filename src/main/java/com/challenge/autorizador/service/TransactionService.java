package com.challenge.autorizador.service;

import com.challenge.autorizador.model.request.Transaction;
import com.challenge.autorizador.model.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse authorize(Transaction transaction);

}

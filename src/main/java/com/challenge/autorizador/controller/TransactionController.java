package com.challenge.autorizador.controller;

import com.challenge.autorizador.annotations.TransactionApiV1;
import com.challenge.autorizador.model.request.Transaction;
import com.challenge.autorizador.model.response.TransactionResponse;
import com.challenge.autorizador.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@TransactionApiV1
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/authorize")
    public ResponseEntity<TransactionResponse> authorizeTransaction(@RequestBody Transaction transaction) {
        TransactionResponse transactionResponse = transactionService.authorize(transaction);

        return ResponseEntity.ok(transactionResponse);
    }
}

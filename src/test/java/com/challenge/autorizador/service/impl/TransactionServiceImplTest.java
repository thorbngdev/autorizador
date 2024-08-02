package com.challenge.autorizador.service.impl;

import com.challenge.autorizador.enumerate.TransactionCode;
import com.challenge.autorizador.model.entity.Account;
import com.challenge.autorizador.model.entity.Merchant;
import com.challenge.autorizador.model.request.Transaction;
import com.challenge.autorizador.model.response.TransactionResponse;
import com.challenge.autorizador.repository.AccountRepository;
import com.challenge.autorizador.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void shouldAuthorizeTransactionWhenAccountAndMerchantAreValid() {
        Account account = new Account("100", BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00), BigDecimal.valueOf(1300.00));
        Merchant merchant = new Merchant(1L, "PADARIA DO ZE SAO PAULO BR", "5411");
        Transaction transaction = new Transaction("100", BigDecimal.valueOf(50.00), "5411", "PADARIA DO ZE SAO PAULO BR");

        when(accountRepository.findById("100")).thenReturn(Optional.of(account));
        when(merchantRepository.findByMerchantName("PADARIA DO ZE SAO PAULO BR")).thenReturn(Optional.of(merchant));

        TransactionResponse response = transactionService.authorize(transaction);

        assertEquals(TransactionCode.APPROVED.getCode(), response.getCode());
        verify(accountRepository).save(account);
    }

    @Test
    @Transactional
    public void shouldRejectTransactionWhenAccountIsNotFound() {
        Transaction transaction = new Transaction("999", BigDecimal.valueOf(50.00), "5411", "PADARIA DO ZE SAO PAULO BR");

        when(accountRepository.findById("999")).thenReturn(Optional.empty());

        TransactionResponse response = transactionService.authorize(transaction);

        assertEquals(TransactionCode.REJECTED.getCode(), response.getCode());
    }

    @Test
    @Transactional
    public void shouldRejectTransactionWhenMerchantIsNotFound() {
        Account account = new Account("100", BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00), BigDecimal.valueOf(1300.00));
        Transaction transaction = new Transaction("100", BigDecimal.valueOf(50.00), "5411", "MERCHANT CAJU KAKAKA");

        when(accountRepository.findById("100")).thenReturn(Optional.of(account));
        when(merchantRepository.findByMerchantName("UNKNOWN MERCHANT")).thenReturn(Optional.empty());

        TransactionResponse response = transactionService.authorize(transaction);

        assertEquals(TransactionCode.REJECTED.getCode(), response.getCode());
    }

    @Test
    @Transactional
    public void shouldHandleInsufficientBalanceException() {
        Account account = new Account("100", BigDecimal.valueOf(50.00), BigDecimal.valueOf(50.00), BigDecimal.valueOf(0.00));
        Merchant merchant = new Merchant(1L, "RESTAURANTE XYZ CURITIBA PR", "5811");
        Transaction transaction = new Transaction("100", BigDecimal.valueOf(100.00), "5811", "RESTAURANTE XYZ CURITIBA PR");

        when(accountRepository.findById("100")).thenReturn(Optional.of(account));
        when(merchantRepository.findByMerchantName("RESTAURANTE XYZ CURITIBA PR")).thenReturn(Optional.of(merchant));

        TransactionResponse response = transactionService.authorize(transaction);

        assertEquals(TransactionCode.INSUFFICIENT_BALANCE.getCode(), response.getCode());
    }

    @Test
    @Transactional
    public void shouldFallbackToCashWhenFoodBalanceIsInsufficient() {
        Account account = new Account("100", BigDecimal.valueOf(50.00), BigDecimal.valueOf(0.00), BigDecimal.valueOf(200.00));
        Merchant merchant = new Merchant(1L, "PADARIA DO ZE SAO PAULO BR", "5411");
        Transaction transaction = new Transaction("100", BigDecimal.valueOf(100.00), "5411", "PADARIA DO ZE SAO PAULO BR");

        when(accountRepository.findById("100")).thenReturn(Optional.of(account));
        when(merchantRepository.findByMerchantName("PADARIA DO ZE SAO PAULO BR")).thenReturn(Optional.of(merchant));

        TransactionResponse response = transactionService.authorize(transaction);

        assertEquals(TransactionCode.APPROVED.getCode(), response.getCode());
        assertEquals(BigDecimal.valueOf(100.00), account.getCashAmount());
        verify(accountRepository).save(account);
    }
}


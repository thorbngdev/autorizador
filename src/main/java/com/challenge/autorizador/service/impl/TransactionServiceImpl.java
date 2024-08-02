package com.challenge.autorizador.service.impl;

import com.challenge.autorizador.enumerate.BalanceType;
import com.challenge.autorizador.enumerate.TransactionCode;
import com.challenge.autorizador.exception.InsufficientBalanceException;
import com.challenge.autorizador.model.entity.Account;
import com.challenge.autorizador.model.entity.Merchant;
import com.challenge.autorizador.model.request.Transaction;
import com.challenge.autorizador.model.response.TransactionResponse;
import com.challenge.autorizador.repository.AccountRepository;
import com.challenge.autorizador.repository.MerchantRepository;
import com.challenge.autorizador.service.TransactionService;
import com.challenge.autorizador.utils.TransactionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LogManager.getLogger(TransactionServiceImpl.class);

    private final AccountRepository accountRepository;

    private final MerchantRepository merchantRepository;

    @Autowired
    TransactionServiceImpl(MerchantRepository merchantRepository, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.merchantRepository = merchantRepository;
    }

    @Transactional
    @Override
    public TransactionResponse authorize(Transaction transaction) {

        Optional<Account> account = accountRepository
                .findById(transaction.getAccount());
        Optional<Merchant> merchant = merchantRepository
                .findByMerchantName(TransactionUtils.normalizeMerchantName(transaction.getMerchant()));

        if (account.isPresent() && merchant.isPresent()) {
            try {
                String mcc = transaction.getMcc();

                if (!TransactionUtils.isNumericMcc(mcc)) {
                    mcc = merchant.get().getMcc();
                }

                processTransaction(
                        account.get(),
                        transaction.getTotalAmount(),
                        BalanceType.fromMcc(mcc)
                );

                accountRepository.save(account.get());
            } catch (InsufficientBalanceException exception) {
                logger.error(exception.getMessage());
                return new TransactionResponse(TransactionCode.INSUFFICIENT_BALANCE.getCode());
            }

            return new TransactionResponse(TransactionCode.APPROVED.getCode());
        }

        return new TransactionResponse(TransactionCode.REJECTED.getCode());
    }

    private void processTransaction(Account account, BigDecimal totalAmount, BalanceType balanceType) {

        switch (balanceType) {
            case CASH -> {
                BigDecimal amount = account.getCashAmount();
                if (TransactionUtils.hasBalance(amount, totalAmount)) {
                    account.setCashAmount(amount.subtract(totalAmount));
                } else {
                    throw new InsufficientBalanceException(account.getId());
                }
            }

            case FOOD -> {
                BigDecimal amount = account.getFoodAmount();
                if (TransactionUtils.hasBalance(amount, totalAmount)) {
                    account.setFoodAmount(amount.subtract(totalAmount));
                } else {
                    processTransaction(account, totalAmount, BalanceType.CASH);
                }
            }

            case MEAL -> {
                BigDecimal amount = account.getMealAmount();
                if (TransactionUtils.hasBalance(amount, totalAmount)) {
                    account.setMealAmount(amount.subtract(totalAmount));
                } else {
                    processTransaction(account, totalAmount, BalanceType.CASH);
                }
            }
        }

    }

}

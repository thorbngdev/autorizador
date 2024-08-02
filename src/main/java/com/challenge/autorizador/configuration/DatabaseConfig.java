package com.challenge.autorizador.configuration;

import com.challenge.autorizador.model.entity.Account;
import com.challenge.autorizador.model.entity.Merchant;
import com.challenge.autorizador.repository.AccountRepository;
import com.challenge.autorizador.repository.MerchantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DatabaseConfig {

    @Bean
    CommandLineRunner initDatabase(AccountRepository accountRepository, MerchantRepository merchantRepository) {
        return args -> {
            List<Account> accounts = List.of(
                    new Account("100", BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00), BigDecimal.valueOf(1300.00)),
                    new Account("101", BigDecimal.valueOf(150.00), BigDecimal.valueOf(2250.00), BigDecimal.valueOf(350.00)),
                    new Account("102", BigDecimal.valueOf(1200.00), BigDecimal.valueOf(3300.00), BigDecimal.valueOf(4400.00))
            );
            accountRepository.saveAll(accounts);

            List<Merchant> merchants = List.of(
                    new Merchant(null, "PADARIA DO ZE SAO PAULO BR", "5411"), // FOOD
                    new Merchant(null, "SUPERMERCADO ABC RIO DE JANEIRO RJ", "5412"), // FOOD
                    new Merchant(null, "RESTAURANTE XYZ CURITIBA PR", "5811"), // MEAL
                    new Merchant(null, "LOJA DE ELETRONICOS SAO PAULO SP", "5812"), //MEAL
                    new Merchant(null, "LOJA DE FRUTAS SAO JOSE DOS CAMPOS SP", "8900") //CASH
            );
            merchantRepository.saveAll(merchants);
        };
    }
}

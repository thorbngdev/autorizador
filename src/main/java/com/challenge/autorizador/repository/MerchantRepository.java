package com.challenge.autorizador.repository;

import com.challenge.autorizador.model.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByMerchantName(String merchantName);

}

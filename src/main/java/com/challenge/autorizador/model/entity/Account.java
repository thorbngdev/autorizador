package com.challenge.autorizador.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private String id;

    private BigDecimal mealAmount;

    private BigDecimal foodAmount;

    private BigDecimal cashAmount;

}

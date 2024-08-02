package com.challenge.autorizador.utils;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionUtilsTest {

    @Test
    public void testHasBalance() {
        BigDecimal accountAmount = new BigDecimal("100.00");
        BigDecimal totalAmount = new BigDecimal("50.00");
        assertTrue(TransactionUtils.hasBalance(accountAmount, totalAmount));

        totalAmount = new BigDecimal("150.00");
        assertFalse(TransactionUtils.hasBalance(accountAmount, totalAmount));
    }

    @Test
    public void testIsNumericMcc() {
        assertTrue(TransactionUtils.isNumericMcc("5811"));
        assertFalse(TransactionUtils.isNumericMcc("CAJU123X"));
        assertFalse(TransactionUtils.isNumericMcc(""));
        assertFalse(TransactionUtils.isNumericMcc(null));
    }

    @Test
    public void testNormalizeMerchantName() {
        assertEquals("CAJU SAO PAULO BR", TransactionUtils.normalizeMerchantName("CAJU       SAO PAULO BR"));
        assertEquals("TESTING", TransactionUtils.normalizeMerchantName("   TESTING     "));
        assertEquals("NO SPACESSS", TransactionUtils.normalizeMerchantName("NO SPACESSS"));
        assertNull(TransactionUtils.normalizeMerchantName(null));
    }
}

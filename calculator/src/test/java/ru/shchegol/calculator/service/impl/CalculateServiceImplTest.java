package ru.shchegol.calculator.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;
@ExtendWith(MockitoExtension.class)
class CalculateServiceImplTest {

    @InjectMocks
    private CalculateServiceImpl calculateService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(calculateService, "INSURANCE_COST", new BigDecimal("10000"));
    }

    @Test
    void calculateAnnuityMonthlyPayment() {
        BigDecimal principal = new BigDecimal("100000");
        BigDecimal rate = new BigDecimal("0.0125");
        int term = 12;
        BigDecimal expected = new BigDecimal("9013.98");

        BigDecimal result = calculateService.calculateAnnuityMonthlyPayment(principal, rate, term);

        assertEquals(expected, result);
    }

    @Test
    void calculateTotalAmount() {
        BigDecimal monthlyPayment = new BigDecimal("1");
        int term = 10;
        BigDecimal expected = new BigDecimal("10");

        BigDecimal result = calculateService.calculateTotalAmount(monthlyPayment, term);

        assertEquals(expected, result);
    }

    @Test
    void calculatePrincipal_withInsurance() {
        BigDecimal amount = new BigDecimal("100000");
        boolean isInsuranceEnabled = true;
        BigDecimal expected = new BigDecimal("110000");

        BigDecimal result = calculateService.calculatePrincipal(amount, isInsuranceEnabled);

        assertEquals(expected, result);
    }

    @Test
    void calculatePrincipal_withoutInsurance() {
        BigDecimal amount = new BigDecimal("10000");
        boolean isInsuranceEnabled = false;
        BigDecimal expected = amount;

        BigDecimal result = calculateService.calculatePrincipal(amount, isInsuranceEnabled);

        assertEquals(expected, result);
    }

    @Test
    void calculateMonthlyPayment() {
        BigDecimal principal = new BigDecimal("100000");
        BigDecimal rate = new BigDecimal("15");
        int term = 12;
        BigDecimal expected = new BigDecimal("9013.98");

        BigDecimal result = calculateService.calculateMonthlyPayment(principal, rate, term);

        assertEquals(expected, result);
    }
}
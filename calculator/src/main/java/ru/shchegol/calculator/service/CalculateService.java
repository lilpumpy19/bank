package ru.shchegol.calculator.service;

import java.math.BigDecimal;

public interface CalculateService {

    BigDecimal calculateAnnuityMonthlyPayment(BigDecimal principal, BigDecimal rate, int term);

    BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, int term);

    BigDecimal calculatePrincipal(BigDecimal amount, boolean isInsuranceEnabled);

    BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal rate, int term);

}

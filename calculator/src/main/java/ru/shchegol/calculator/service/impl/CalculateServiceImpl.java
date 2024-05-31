package ru.shchegol.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shchegol.calculator.service.CalculateService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CalculateServiceImpl implements CalculateService {

    @Value("${insurance.cost}")
    private BigDecimal INSURANCE_COST;
    @Override
    public BigDecimal calculateAnnuityMonthlyPayment(BigDecimal principal, BigDecimal rate, int term) {
        BigDecimal onePlusRatePowerTerm = rate.add(BigDecimal.ONE)
                .pow(term, new MathContext(10, RoundingMode.HALF_UP));
        BigDecimal numerator = principal.multiply(rate).multiply(onePlusRatePowerTerm);
        BigDecimal denominator = onePlusRatePowerTerm.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, int term) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }
    @Override
    public BigDecimal calculatePrincipal(BigDecimal amount, boolean isInsuranceEnabled) {
        return isInsuranceEnabled ? amount.add(INSURANCE_COST) : amount;
    }
    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal rate, int term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        return this.calculateAnnuityMonthlyPayment(principal, monthlyRate, term);
    }
}

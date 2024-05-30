package ru.shchegol.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.shchegol.calculator.dto.LoanOfferDto;
import ru.shchegol.calculator.dto.LoanStatementRequestDto;
import ru.shchegol.calculator.service.CalculatorOfferService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculatorOfferServiceImpl implements CalculatorOfferService {

    @Value("${base.rate}")
    private BigDecimal BASE_RATE;
    @Value("${insurance.cost}")
    private BigDecimal INSURANCE_COST;
    @Value("${insurance.discount}")
    private BigDecimal INSURANCE_DISCOUNT;
    @Value("${salary.client.discount}")
    private BigDecimal SALARY_CLIENT_DISCOUNT;

    @Override
    public ResponseEntity<List<LoanOfferDto>> calcLoanOffers(LoanStatementRequestDto loanStatement) {
        List<LoanOfferDto> offers = createOffers(loanStatement);
        return ResponseEntity.ok(offers);
    }

    private List<LoanOfferDto> createOffers(LoanStatementRequestDto loanStatement) {
        List<LoanOfferDto> offers = new ArrayList<>();
        offers.add(createOffer(loanStatement, true, true));
        offers.add(createOffer(loanStatement, true, false));
        offers.add(createOffer(loanStatement, false, true));
        offers.add(createOffer(loanStatement, false, false));
        return offers;
    }

    private LoanOfferDto createOffer(LoanStatementRequestDto loanStatement, boolean insurance, boolean salaryClient) {
        LoanOfferDto offer = new LoanOfferDto();
        offer.setStatementId(UUID.randomUUID());
        offer.setInsuranceEnabled(insurance);
        offer.setSalaryClient(salaryClient);
        offer.setTerm(loanStatement.getTerm());
        offer.setRequestedAmount(loanStatement.getAmount());
        offer.setRate(calculateRate(salaryClient, insurance));

        BigDecimal principal = calculatePrincipal(loanStatement.getAmount(), insurance);
        BigDecimal monthlyPayment = calculateMonthlyPayment(principal, offer.getRate(), loanStatement.getTerm());
        BigDecimal totalAmount = calculateTotalAmount(monthlyPayment, loanStatement.getTerm());

        offer.setTotalAmount(totalAmount);
        offer.setMonthlyPayment(monthlyPayment);

        return offer;
    }

    private BigDecimal calculateRate(boolean salaryClient, boolean insurance) {
        BigDecimal rate = BASE_RATE;
        if (salaryClient) rate = rate.subtract(SALARY_CLIENT_DISCOUNT);
        if (insurance) rate = rate.subtract(INSURANCE_DISCOUNT);
        return rate;
    }

    private BigDecimal calculatePrincipal(BigDecimal amount, boolean insurance) {
        if (insurance) {
            return amount.add(INSURANCE_COST);
        }
        return amount;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRate, int term) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        return calculateAnnuityMonthlyPayment(principal, monthlyRate, term);
    }

    private BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, int term) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }

    private BigDecimal calculateAnnuityMonthlyPayment(BigDecimal principal, BigDecimal monthlyRate, int term) {
        BigDecimal onePlusRatePowerTerm = monthlyRate.add(BigDecimal.ONE)
                .pow(term, new MathContext(10, RoundingMode.HALF_UP));
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRatePowerTerm);
        BigDecimal denominator = onePlusRatePowerTerm.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}

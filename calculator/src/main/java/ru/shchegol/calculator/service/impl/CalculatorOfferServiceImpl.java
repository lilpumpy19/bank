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
import java.time.LocalDate;
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
        offers.add(this.createOffer(loanStatement,true,true));
        offers.add(this.createOffer(loanStatement,true,false));
        offers.add(this.createOffer(loanStatement,false,true));
        offers.add(this.createOffer(loanStatement,false,false));
        return offers;
    }

    private LoanOfferDto createOffer(LoanStatementRequestDto loanStatement,
                                     boolean insurance, boolean salaryClient) {
        LoanOfferDto offer = new LoanOfferDto();
        offer.setStatementId(UUID.randomUUID());
        offer.setInsuranceEnabled(insurance);
        offer.setSalaryClient(salaryClient);
        offer.setTerm(loanStatement.getTerm());
        offer.setRequestedAmount(loanStatement.getAmount());
        offer.setRate(this.calculateRate(salaryClient, insurance));

        BigDecimal rate = offer.getRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal principal = loanStatement.getAmount();
        if (insurance) {
            principal = principal.add(INSURANCE_COST);
        }

        BigDecimal monthlyPayment = calculateAnnuityMonthlyPayment(principal, monthlyRate, loanStatement.getTerm());
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(loanStatement.getTerm()));

        offer.setTotalAmount(totalAmount);
        offer.setMonthlyPayment(monthlyPayment);

        return offer;
    }

    private BigDecimal calculateRate(boolean salaryClient, boolean insurance) {
        BigDecimal baseRate = BASE_RATE;

        if (salaryClient) {
            baseRate = baseRate.subtract(SALARY_CLIENT_DISCOUNT);
        }

        if (insurance) {
            baseRate = baseRate.subtract(INSURANCE_DISCOUNT);
        }

        return baseRate;
    }

    private BigDecimal calculateAnnuityMonthlyPayment(BigDecimal principal, BigDecimal monthlyRate, int term) {
        BigDecimal onePlusRatePowerTerm = monthlyRate.add(BigDecimal.ONE)
                .pow(term, new MathContext(10, RoundingMode.HALF_UP));
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRatePowerTerm);
        BigDecimal denominator = onePlusRatePowerTerm.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}

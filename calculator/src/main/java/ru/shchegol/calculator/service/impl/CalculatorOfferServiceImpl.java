package ru.shchegol.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.calculator.service.CalculateService;
import ru.shchegol.calculator.service.CalculatorOfferService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculatorOfferServiceImpl implements CalculatorOfferService {

    private final CalculateService calculateService;

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

        BigDecimal principal = calculateService.calculatePrincipal(loanStatement.getAmount(), insurance);
        BigDecimal monthlyPayment = calculateService.
                calculateMonthlyPayment(principal, offer.getRate(), loanStatement.getTerm());
        BigDecimal totalAmount = calculateService.calculateTotalAmount(monthlyPayment, loanStatement.getTerm());

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




}

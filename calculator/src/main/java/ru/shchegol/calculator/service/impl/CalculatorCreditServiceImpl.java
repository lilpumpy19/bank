package ru.shchegol.calculator.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.shchegol.calculator.dto.CreditDto;
import ru.shchegol.calculator.dto.EmploymentDto;
import ru.shchegol.calculator.dto.PaymentScheduleElementDto;
import ru.shchegol.calculator.dto.ScoringDataDto;
import ru.shchegol.calculator.dto.dependencies.Gender;
import ru.shchegol.calculator.dto.dependencies.MaritalStatus;
import ru.shchegol.calculator.service.CalculatorCreditService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class CalculatorCreditServiceImpl implements CalculatorCreditService {
    private CreditDto credit;
    @Value("${base.rate}")
    private BigDecimal BASE_RATE;
    @Value("${insurance.cost}")
    private BigDecimal INSURANCE_COST;
    @Value("${insurance.discount}")
    private BigDecimal INSURANCE_DISCOUNT;
    @Value("${salary.client.discount}")
    private BigDecimal SALARY_CLIENT_DISCOUNT;

    @Override
    public ResponseEntity<CreditDto> calcCredit(ScoringDataDto scoringData) {
        this.createCredit(scoringData);
        return ResponseEntity.ok(credit);
    }



    private void createCredit(ScoringDataDto scoringData) {
        credit = new CreditDto(BASE_RATE);
        credit.setTerm(scoringData.getTerm());
        credit.setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled());
        credit.setIsSalaryClient(scoringData.getIsSalaryClient());

        BigDecimal principal = scoringData.getAmount();
        if (scoringData.getIsInsuranceEnabled()) {
            principal = principal.add(INSURANCE_COST);
        }
        this.checkInsurance(scoringData);
        this.checkIsSalaryClient(scoringData);
        this.checkMarialStatus(scoringData);
        this.checkEmployment(scoringData);
        this.checkAge(scoringData);
        this.checkGender(scoringData);

        BigDecimal rate = credit.getRate().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        BigDecimal monthlyPayment = calculateAnnuityMonthlyPayment(principal, monthlyRate, credit.getTerm());
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(credit.getTerm()));

        credit.setMonthlyPayment(monthlyPayment);
        credit.setAmount(totalAmount);
        credit.setPsk(totalAmount.divide(scoringData.getAmount(),2, RoundingMode.HALF_UP).
                multiply(BigDecimal.valueOf(100)).subtract(BigDecimal.valueOf(100)));


        BigDecimal remainingDebt = principal;
        for (int i = 1; i <= scoringData.getTerm(); i++) {

            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_UP);

            credit.addPaymentScheduleElement(new PaymentScheduleElementDto(i, LocalDate.now().plusMonths(i),
                    monthlyPayment, interestPayment, debtPayment, remainingDebt));
        }

    }

    private BigDecimal calculateAnnuityMonthlyPayment(BigDecimal principal, BigDecimal monthlyRate, int term) {
        BigDecimal onePlusRatePowerTerm = monthlyRate.add(BigDecimal.ONE)
                .pow(term, new MathContext(10, RoundingMode.HALF_UP));
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRatePowerTerm);
        BigDecimal denominator = onePlusRatePowerTerm.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
    private void checkIsSalaryClient(ScoringDataDto scoringData) {
        if (scoringData.getIsSalaryClient()) {
            credit.setRate(SALARY_CLIENT_DISCOUNT.negate());
        }
    }

    private void checkInsurance(ScoringDataDto scoringData) {
        if (scoringData.getIsInsuranceEnabled()) {
            credit.setRate(INSURANCE_DISCOUNT.negate());
        }
    }
    private void checkMarialStatus(ScoringDataDto scoringData) {
        MaritalStatus maritalStatus = scoringData.getMaritalStatus();
        switch (maritalStatus) {
            case SINGLE -> credit.setRate(BigDecimal.valueOf(1));
            case MARRIED -> credit.setRate(BigDecimal.valueOf(-3));
        }
    }

    private void checkEmployment(ScoringDataDto scoringData) {
        EmploymentDto employment = scoringData.getEmployment();

        switch (employment.getEmploymentStatus()) {
            // todo Придумать логику отказа case UNEMPLOYED
            case SELF_EMPLOYED -> credit.setRate(BigDecimal.valueOf(1));
            case BUSINESS_OWNER -> credit.setRate(BigDecimal.valueOf(2));

        }

        switch (employment.getPosition()){
            case MANAGER -> credit.setRate(BigDecimal.valueOf(-2));
            case TOP_MANAGER -> credit.setRate(BigDecimal.valueOf(-3));
        }


    }

    private void checkAge(ScoringDataDto scoringData) {
        int age = LocalDate.now().getYear() - scoringData.getBirthdate().getYear();
        if (age < 18|| age > 65) {
            //todo Придумать логику отказа
        }
    }

    private void checkGender(ScoringDataDto scoringData) {
        int age = LocalDate.now().getYear() - scoringData.getBirthdate().getYear();
        Gender gender = scoringData.getGender();
        if (age >32 && age < 60 && gender==Gender.FEMALE) {
            credit.setRate(BigDecimal.valueOf(-3));
        }
        if (age >30 && age < 55 && gender==Gender.MALE) {
            credit.setRate(BigDecimal.valueOf(-3));
        }
        if (gender==Gender.NON_BINARY){
            credit.setRate(BigDecimal.valueOf(7));
        }
    }
}

package ru.shchegol.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.shchegol.dto.CreditDto;
import ru.shchegol.dto.EmploymentDto;
import ru.shchegol.dto.PaymentScheduleElementDto;
import ru.shchegol.dto.ScoringDataDto;
import ru.shchegol.dto.enums.Gender;
import ru.shchegol.dto.enums.MaritalStatus;
import ru.shchegol.calculator.exception.CreditRefusalException;
import ru.shchegol.calculator.service.CalculateService;
import ru.shchegol.calculator.service.CalculatorCreditService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class CalculatorCreditServiceImpl implements CalculatorCreditService {

    private CreditDto credit;
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
    public ResponseEntity<CreditDto> calcCredit(ScoringDataDto scoringData) {
        createCredit(scoringData);
        return ResponseEntity.ok(credit);
    }

    private void createCredit(ScoringDataDto scoringData) {

        credit = new CreditDto(BASE_RATE);

        credit.setTerm(scoringData.getTerm());
        credit.setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled());
        credit.setIsSalaryClient(scoringData.getIsSalaryClient());

        applyScoringAdjustments(scoringData);

        BigDecimal principal = calculateService.
                calculatePrincipal(scoringData.getAmount(), scoringData.getIsInsuranceEnabled());

        BigDecimal rate = credit.getRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);

        BigDecimal monthlyPayment = calculateService.
                calculateMonthlyPayment(principal, credit.getRate(), credit.getTerm());

        BigDecimal totalAmount = calculateService.calculateTotalAmount(monthlyPayment, credit.getTerm());

        credit.setMonthlyPayment(monthlyPayment);
        credit.setAmount(totalAmount);
        credit.setPsk(calculatePSK(totalAmount, scoringData.getAmount()));

        generatePaymentSchedule(scoringData, principal, rate, monthlyPayment);
    }


    private BigDecimal calculatePSK(BigDecimal totalAmount, BigDecimal amount) {
        return totalAmount.divide(amount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .subtract(BigDecimal.valueOf(100));
    }

    private void generatePaymentSchedule(ScoringDataDto scoringData, BigDecimal principal,
                                         BigDecimal rate, BigDecimal monthlyPayment) {
        BigDecimal remainingDebt = principal;
        for (int i = 1; i <= scoringData.getTerm(); i++) {
            BigDecimal interestPayment = remainingDebt.multiply(rate).setScale(4, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment).setScale(4, RoundingMode.HALF_UP);
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(4, RoundingMode.HALF_UP);

            credit.addPaymentScheduleElement(new PaymentScheduleElementDto(
                    i, LocalDate.now().plusMonths(i), monthlyPayment, interestPayment, debtPayment, remainingDebt));
        }
    }

    private void applyScoringAdjustments(ScoringDataDto scoringData) {
        checkSalary(scoringData);
        checkWorkExperience(scoringData.getEmployment());
        checkIsSalaryClient(scoringData);
        checkInsurance(scoringData);
        checkMaritalStatus(scoringData);
        checkEmployment(scoringData);
        checkAge(scoringData);
        checkGender(scoringData);
    }

    private void checkWorkExperience(EmploymentDto employment) {
        if (employment.getWorkExperienceCurrent() < 3 || employment.getWorkExperienceTotal() < 18) {
            throw new CreditRefusalException("Refusal due to negative work experience");
        }


    }

    private void checkSalary(ScoringDataDto scoringData) {
        BigDecimal salary = scoringData.getEmployment().getSalary();
        BigDecimal amount = scoringData.getAmount();
        BigDecimal threshold = salary.multiply(BigDecimal.valueOf(25));

        if (threshold.compareTo(amount) < 0) {
            throw new CreditRefusalException("Refusal due to insufficient salary");
        }
    }


    private void checkIsSalaryClient(ScoringDataDto scoringData) {
        if (scoringData.getIsSalaryClient()) {
            credit.changeRate(SALARY_CLIENT_DISCOUNT.negate());
        }
    }

    private void checkInsurance(ScoringDataDto scoringData) {
        if (scoringData.getIsInsuranceEnabled()) {
            credit.changeRate(INSURANCE_DISCOUNT.negate());
        }
    }

    private void checkMaritalStatus(ScoringDataDto scoringData) {
        MaritalStatus maritalStatus = scoringData.getMaritalStatus();
        switch (maritalStatus) {
            case SINGLE -> credit.changeRate(BigDecimal.valueOf(1));
            case MARRIED -> credit.changeRate(BigDecimal.valueOf(-3));
        }
    }

    private void checkEmployment(ScoringDataDto scoringData) {
        EmploymentDto employment = scoringData.getEmployment();

        switch (employment.getEmploymentStatus()) {
            case UNEMPLOYED -> throw new CreditRefusalException("Refusal due to unemployed");
            case SELF_EMPLOYED -> credit.changeRate(BigDecimal.valueOf(1));
            case BUSINESS_OWNER -> credit.changeRate(BigDecimal.valueOf(2));
        }

        switch (employment.getPosition()) {
            case MANAGER -> credit.changeRate(BigDecimal.valueOf(-2));
            case TOP_MANAGER -> credit.changeRate(BigDecimal.valueOf(-3));
        }
    }

    private void checkAge(ScoringDataDto scoringData) {
        int age = LocalDate.now().getYear() - scoringData.getBirthdate().getYear();
        if (age < 18 || age > 65) {
            throw new CreditRefusalException("Refusal due to age");
        }
    }

    private void checkGender(ScoringDataDto scoringData) {
        int age = LocalDate.now().getYear() - scoringData.getBirthdate().getYear();
        Gender gender = scoringData.getGender();
        if (age > 32 && age < 60 && gender == Gender.FEMALE) {
            credit.changeRate(BigDecimal.valueOf(-3));
        } else if (age > 30 && age < 55 && gender == Gender.MALE) {
            credit.changeRate(BigDecimal.valueOf(-3));
        } else if (gender == Gender.NON_BINARY) {
            credit.changeRate(BigDecimal.valueOf(7));
        }
    }
}

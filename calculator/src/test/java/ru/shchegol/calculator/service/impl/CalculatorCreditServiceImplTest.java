package ru.shchegol.calculator.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shchegol.dto.enums.EmploymentStatus;
import ru.shchegol.dto.enums.Gender;
import ru.shchegol.dto.enums.MaritalStatus;
import ru.shchegol.dto.enums.Position;
import ru.shchegol.calculator.exception.CreditRefusalException;
import ru.shchegol.calculator.service.CalculateService;
import ru.shchegol.dto.CreditDto;
import ru.shchegol.dto.EmploymentDto;
import ru.shchegol.dto.PaymentScheduleElementDto;
import ru.shchegol.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorCreditServiceImplTest {
    @Mock
    CalculateService calculateService;


    @InjectMocks
    CalculatorCreditServiceImpl calculatorCreditService;

    ScoringDataDto scoringData = new ScoringDataDto();
    EmploymentDto employment = new EmploymentDto();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(calculatorCreditService, "BASE_RATE", new BigDecimal("15"));
        ReflectionTestUtils.setField(calculatorCreditService, "INSURANCE_COST", new BigDecimal("10000"));
        ReflectionTestUtils.setField(calculatorCreditService, "INSURANCE_DISCOUNT", new BigDecimal("3"));
        ReflectionTestUtils.setField(calculatorCreditService, "SALARY_CLIENT_DISCOUNT", new BigDecimal("1"));

        scoringData.setAmount(new BigDecimal(100000));
        scoringData.setTerm(12);
        scoringData.setFirstName("John");
        scoringData.setLastName("Doe");
        scoringData.setMiddleName("Mss");
        scoringData.setGender(Gender.MALE);
        scoringData.setBirthdate(LocalDate.now().minusYears(19));
        scoringData.setPassportSeries("1234");
        scoringData.setPassportNumber("123456");
        scoringData.setPassportIssueDate(LocalDate.now().minusMonths(1));
        scoringData.setPassportIssueBranch("МВД г. Москва");
        scoringData.setMaritalStatus(MaritalStatus.SINGLE);
        scoringData.setDependentAmount(0);

        employment.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employment.setEmployerINN("1234567890");
        employment.setSalary(new BigDecimal(50000));
        employment.setPosition(Position.EMPLOYEE);
        employment.setWorkExperienceTotal(100);
        employment.setWorkExperienceCurrent(20);
        scoringData.setEmployment(employment);
        scoringData.setAccountNumber("1234567890");
        scoringData.setIsInsuranceEnabled(true);
        scoringData.setIsSalaryClient(true);
    }

    @Test
    void calcCredit() {


        when(calculateService.calculatePrincipal(any(BigDecimal.class), any(Boolean.class)))
                .thenReturn(new BigDecimal("10000"));
        when(calculateService.calculateMonthlyPayment(any(BigDecimal.class), any(BigDecimal.class), any(Integer.class)))
                .thenReturn(new BigDecimal("8500.00"));
        when(calculateService.calculateTotalAmount(any(BigDecimal.class), any(Integer.class)))
                .thenReturn(new BigDecimal("100000"));


        ResponseEntity<CreditDto> response = calculatorCreditService.calcCredit(scoringData);


        CreditDto creditDto = response.getBody();
        assertEquals(scoringData.getTerm(), creditDto.getTerm());
        assertEquals(scoringData.getAmount(), creditDto.getAmount());
        assertEquals(scoringData.getIsInsuranceEnabled(), creditDto.getIsInsuranceEnabled());
        assertEquals(scoringData.getIsSalaryClient(), creditDto.getIsSalaryClient());
        assertEquals(new BigDecimal("8500.00"), creditDto.getMonthlyPayment());
        assertEquals(new BigDecimal("100000"), creditDto.getAmount());


        List<PaymentScheduleElementDto> paymentSchedule = creditDto.getPaymentSchedule();
        assertEquals(scoringData.getTerm(), paymentSchedule.size());
    }

    @Test
    void creditRefusal() {

        scoringData.setBirthdate(LocalDate.now().minusYears(17));
        assertThrows(CreditRefusalException.class, () -> calculatorCreditService.calcCredit(scoringData));
        scoringData.setBirthdate(LocalDate.now().minusYears(19));

        employment.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        assertThrows(CreditRefusalException.class, () -> calculatorCreditService.calcCredit(scoringData));
        employment.setEmploymentStatus(EmploymentStatus.EMPLOYED);

        scoringData.setAmount(new BigDecimal("500000000"));
        assertThrows(CreditRefusalException.class, () -> calculatorCreditService.calcCredit(scoringData));
        scoringData.setAmount(new BigDecimal("100000"));

        employment.setWorkExperienceCurrent(2);
        assertThrows(CreditRefusalException.class, () -> calculatorCreditService.calcCredit(scoringData));
        employment.setWorkExperienceCurrent(3);

        employment.setWorkExperienceTotal(17);
        assertThrows(CreditRefusalException.class, () -> calculatorCreditService.calcCredit(scoringData));
        employment.setWorkExperienceTotal(18);

    }
}


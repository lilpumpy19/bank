package ru.shchegol.calculator.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.calculator.service.CalculateService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CalculatorOfferServiceImplTest {
    @Mock
    private CalculateService calculateService;

    @Mock
    LoanOfferDto loanOfferDto;

    @InjectMocks
    private CalculatorOfferServiceImpl calculatorOfferService;
    LoanStatementRequestDto loanStatement = new LoanStatementRequestDto();


    @BeforeEach
    void setUp() {
        loanStatement.setAmount(new BigDecimal("100000"));
        loanStatement.setTerm(12);
        loanStatement.setFirstName("John");
        loanStatement.setLastName("Doe");
        loanStatement.setMiddleName("Mss");
        loanStatement.setEmail("john.doe@example.com");
        loanStatement.setBirthdate(LocalDate.parse("1990-01-01"));
        loanStatement.setPassportSeries("1234");
        loanStatement.setPassportNumber("123456");

        ReflectionTestUtils.setField(calculatorOfferService, "BASE_RATE", new BigDecimal("15"));
        ReflectionTestUtils.setField(calculatorOfferService, "INSURANCE_COST", new BigDecimal("10000"));
        ReflectionTestUtils.setField(calculatorOfferService, "INSURANCE_DISCOUNT", new BigDecimal("3"));
        ReflectionTestUtils.setField(calculatorOfferService, "SALARY_CLIENT_DISCOUNT", new BigDecimal("1"));
    }

    @Test
    void calcLoanOffers() {
        ResponseEntity<List<LoanOfferDto>> result = calculatorOfferService.calcLoanOffers(loanStatement);
        assertEquals(4, result.getBody().size());

        List<BigDecimal> rates = List.of(
                new BigDecimal("11"),
                new BigDecimal("12"),
                new BigDecimal("14"),
                new BigDecimal("15")
        );

        for (int i = 0; i < result.getBody().size(); i++) {
            assertEquals(rates.get(i), result.getBody().get(i).getRate());
        }

    }
}
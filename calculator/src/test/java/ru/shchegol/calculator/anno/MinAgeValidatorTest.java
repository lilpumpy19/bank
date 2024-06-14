package ru.shchegol.calculator.anno;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shchegol.dto.LoanStatementRequestDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MinAgeValidatorTest {
    LoanStatementRequestDto loanStatement = new LoanStatementRequestDto();
    private Validator validator;
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
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void isValid() {

        loanStatement.setBirthdate(LocalDate.now().minusYears(20));
        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(loanStatement);
        assertTrue(violations.isEmpty());
    }

    @Test
    void isNotValid() {
        loanStatement.setBirthdate(LocalDate.now().minusYears(15));

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(loanStatement);
        assertFalse(violations.isEmpty());
        assertEquals("age must be greater than 18", violations.iterator().next().getMessage());
    }

}
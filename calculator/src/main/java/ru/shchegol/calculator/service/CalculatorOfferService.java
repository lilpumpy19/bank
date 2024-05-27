package ru.shchegol.calculator.service;

import org.springframework.http.ResponseEntity;
import ru.shchegol.calculator.dto.LoanOfferDto;
import ru.shchegol.calculator.dto.LoanStatementRequestDto;

import java.util.List;

public interface CalculatorOfferService {
    ResponseEntity<List<LoanOfferDto>> calcLoanOffers(LoanStatementRequestDto loanStatement);

}

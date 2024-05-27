package ru.shchegol.calculator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shchegol.calculator.dto.CreditDto;
import ru.shchegol.calculator.dto.LoanOfferDto;
import ru.shchegol.calculator.dto.LoanStatementRequestDto;
import ru.shchegol.calculator.dto.ScoringDataDto;
import ru.shchegol.calculator.service.CalculatorOfferService;

import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController {
    private final CalculatorOfferService calculatorService;
//    @PostMapping("/calc")
//    public ResponseEntity<CreditDto> calc(@RequestBody ScoringDataDto scoringData) {
//        return calculatorService.calcCredit(scoringData);
//    }

    @PostMapping("/offers")
    public  ResponseEntity<List<LoanOfferDto>> offers(@RequestBody LoanStatementRequestDto loanStatement) {
        return calculatorService.calcLoanOffers(loanStatement);
    }
}

package ru.shchegol.calculator.service;

import org.springframework.http.ResponseEntity;
import ru.shchegol.calculator.dto.CreditDto;
import ru.shchegol.calculator.dto.ScoringDataDto;

public interface CalculatorCreditService {

    ResponseEntity<CreditDto> calcCredit(ScoringDataDto scoringData);
}

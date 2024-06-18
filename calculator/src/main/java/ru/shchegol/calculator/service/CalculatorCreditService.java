package ru.shchegol.calculator.service;

import org.springframework.http.ResponseEntity;
import ru.shchegol.dto.CreditDto;
import ru.shchegol.dto.ScoringDataDto;

public interface CalculatorCreditService {

    ResponseEntity<CreditDto> calcCredit(ScoringDataDto scoringData);
}

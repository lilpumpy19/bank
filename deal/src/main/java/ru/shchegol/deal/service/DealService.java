package ru.shchegol.deal.service;

import ru.shchegol.deal.dto.FinishRegistrationRequestDto;
import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.deal.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {

    List<LoanOfferDto> calculateLoanConditions(LoanStatementRequestDto request);

    void selectLoanOffer(LoanOfferDto offer);

    void finishRegistrationAndCalculate(String statementId, FinishRegistrationRequestDto request);
}

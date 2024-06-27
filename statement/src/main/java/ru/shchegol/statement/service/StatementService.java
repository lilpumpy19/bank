package ru.shchegol.statement.service;

import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;

import java.util.List;

public interface StatementService {

    List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatement);

    void selectOffer(LoanOfferDto loanOffer);
}

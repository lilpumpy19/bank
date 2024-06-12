package ru.shchegol.deal.service;

import ru.shchegol.deal.dto.CreditDto;
import ru.shchegol.deal.dto.FinishRegistrationRequestDto;
import ru.shchegol.deal.dto.LoanStatementRequestDto;
import ru.shchegol.deal.dto.ScoringDataDto;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Credit;
import ru.shchegol.deal.entity.Statement;

public interface FactorySercice {
    Client createClient(LoanStatementRequestDto request);

    Statement createStatement(LoanStatementRequestDto request, Client client);

    ScoringDataDto createScoringData(FinishRegistrationRequestDto request, Statement statement);

    Credit createCredit(CreditDto creditDto);
}

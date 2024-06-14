package ru.shchegol.deal.service;

import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.dto.ScoringDataDto;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Credit;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.dto.CreditDto;

public interface FactorySercice {
    Client createClient(LoanStatementRequestDto request);

    Statement createStatement(LoanStatementRequestDto request, Client client);

    ScoringDataDto createScoringData(FinishRegistrationRequestDto request, Statement statement);

    Credit createCredit(CreditDto creditDto);
}

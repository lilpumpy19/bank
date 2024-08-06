package ru.shchegol.gateway.service;

import org.springframework.http.ResponseEntity;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;

import java.util.List;

public interface GatewayService {
    ResponseEntity<List<LoanOfferDto>> getLoanOffers(LoanStatementRequestDto loanStatement);

    ResponseEntity<Void> selectOffer(LoanOfferDto loanOffer);

    ResponseEntity<Void> finishRegistrationAndCalculate(String statementId, FinishRegistrationRequestDto request);

    ResponseEntity<Void> sendDocuments(String statementId);

    ResponseEntity<Void> sendSes(String statementId);

    ResponseEntity<Void> creditIssued(String statementId);

}

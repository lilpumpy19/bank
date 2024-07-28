package ru.shchegol.gateway.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.gateway.feignclient.FeignClientDeal;
import ru.shchegol.gateway.feignclient.FeignClientStatement;
import ru.shchegol.gateway.service.GatewayService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatewayServiceImpl implements GatewayService {
    private final FeignClientDeal dealClient;
    private final FeignClientStatement statementClient;
    @Override
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(LoanStatementRequestDto loanStatement) {
        return statementClient.getLoanOffers(loanStatement);
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto loanOffer) {
        return statementClient.selectOffer(loanOffer);
    }

    @Override
    public ResponseEntity<Void> finishRegistrationAndCalculate(String statementId, FinishRegistrationRequestDto request) {
        return dealClient.finishRegistrationAndCalculate(statementId, request);
    }

    @Override
    public ResponseEntity<Void> sendDocuments(String statementId) {
        return dealClient.sendDocuments(statementId);
    }

    @Override
    public ResponseEntity<Void> sendSes(String statementId) {
        return dealClient.codeDocument(statementId);
    }

    @Override
    public ResponseEntity<Void> creditIssued(String statementId) {
        return dealClient.signDocument(statementId);
    }
}

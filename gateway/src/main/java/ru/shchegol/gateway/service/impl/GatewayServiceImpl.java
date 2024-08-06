package ru.shchegol.gateway.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GatewayServiceImpl implements GatewayService {
    private final FeignClientDeal dealClient;
    private final FeignClientStatement statementClient;
    @Override
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(LoanStatementRequestDto loanStatement) {
        log.info("Request to GetLoanOffers: {}", loanStatement);
        return statementClient.getLoanOffers(loanStatement);
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDto loanOffer) {
        log.info("Request to SelectOffer: {}", loanOffer);
        return statementClient.selectOffer(loanOffer);
    }

    @Override
    public ResponseEntity<Void> finishRegistrationAndCalculate(String statementId, FinishRegistrationRequestDto request) {
        log.info("Request to FinishRegistrationAndCalculate: {}, {}", statementId, request);
        return dealClient.finishRegistrationAndCalculate(statementId, request);
    }

    @Override
    public ResponseEntity<Void> sendDocuments(String statementId) {
        log.info("Request to SendDocuments: {}", statementId);
        return dealClient.sendDocuments(statementId);
    }

    @Override
    public ResponseEntity<Void> sendSes(String statementId) {
        log.info("Request to SendSes: {}", statementId);
        return dealClient.codeDocument(statementId);
    }

    @Override
    public ResponseEntity<Void> creditIssued(String statementId) {
        log.info("Request to CreditIssued: {}", statementId);
        return dealClient.signDocument(statementId);
    }
}

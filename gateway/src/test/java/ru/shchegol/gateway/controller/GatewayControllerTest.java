package ru.shchegol.gateway.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.gateway.service.GatewayService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GatewayControllerTest {
    @InjectMocks
    private GatewayController gatewayController;
    @Mock
    private GatewayService gatewayService;
    

    @Test
    void statement_ShouldReturnOk() {
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        gatewayController.statement(request);
        verify(gatewayService, times(1)).getLoanOffers(request);
    }

    @Test
    void selectOffer_ShouldReturnOk() {
        LoanOfferDto offer = new LoanOfferDto();
        gatewayController.selectOffer(offer);
        verify(gatewayService, times(1)).selectOffer(offer);
    }

    @Test
    void finishRegistrationAndCalculate_ShouldReturnOk() {
        String statementId = UUID.randomUUID().toString();
        FinishRegistrationRequestDto request = new FinishRegistrationRequestDto();
        gatewayController.finishRegistrationAndCalculate(statementId, request);
        verify(gatewayService, times(1)).finishRegistrationAndCalculate(statementId, request);
    }


    @Test
    void sendDocument_ShouldReturnOk() {
        String statementId = UUID.randomUUID().toString();
        gatewayController.sendDocument(statementId);
        verify(gatewayService, times(1)).sendDocuments(statementId);
    }

    @Test
    void signDocument_ShouldReturnOk() {
        String statementId = UUID.randomUUID().toString();
        ResponseEntity<Void> response = gatewayController.signDocument(statementId);
        verify(gatewayService, times(1)).creditIssued(statementId);
    }

    @Test
    void codeDocument_ShouldReturnOk() {
        String statementId = UUID.randomUUID().toString();
        gatewayController.codeDocument(statementId);
        verify(gatewayService, times(1)).sendSes(statementId);
    }
}
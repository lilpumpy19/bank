package ru.shchegol.gateway.service.impl;

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
import ru.shchegol.gateway.feignclient.FeignClientDeal;
import ru.shchegol.gateway.feignclient.FeignClientStatement;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GatewayServiceImplTest {
    @InjectMocks
    GatewayServiceImpl gatewayService;

    @Mock
    FeignClientDeal dealClient;

    @Mock
    FeignClientStatement statementClient;

    @BeforeEach
    void setUp() {
        gatewayService = new GatewayServiceImpl(dealClient, statementClient);
    }

    @Test
    void getLoanOffers_success() {
        when(statementClient.getLoanOffers(any())).thenReturn(ResponseEntity.ok(Arrays.asList()));
        assertDoesNotThrow(() -> gatewayService.getLoanOffers(new LoanStatementRequestDto()));
    }

    @Test
    void selectOffer_success() {
        when(statementClient.selectOffer(any())).thenReturn(ResponseEntity.ok(null));
        assertDoesNotThrow(() -> gatewayService.selectOffer(new LoanOfferDto()));
    }

    @Test
    void finishRegistrationAndCalculate_success() {
        when(dealClient.finishRegistrationAndCalculate(any(), any())).thenReturn(ResponseEntity.ok(null));
        assertDoesNotThrow(() -> gatewayService.finishRegistrationAndCalculate("1", new FinishRegistrationRequestDto()));
    }

    @Test
    void sendDocuments_success() {
        when(dealClient.sendDocuments(any())).thenReturn(ResponseEntity.ok(null));
        assertDoesNotThrow(() -> gatewayService.sendDocuments("1"));
    }

    @Test
    void sendSes_success() {
        when(dealClient.codeDocument(any())).thenReturn(ResponseEntity.ok(null));
        assertDoesNotThrow(() -> gatewayService.sendSes("1"));
    }

    @Test
    void creditIssued_success() {
        when(dealClient.signDocument(any())).thenReturn(ResponseEntity.ok(null));
        assertDoesNotThrow(() -> gatewayService.creditIssued("1"));
    }
}
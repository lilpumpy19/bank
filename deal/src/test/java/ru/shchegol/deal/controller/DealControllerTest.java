package ru.shchegol.deal.controller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.deal.service.DealService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private DealController dealController;
    @Mock
    private DealService dealService;



    @Test
    void calculateLoanConditions_ShouldReturnLoanOffers() {

        LoanStatementRequestDto request = new LoanStatementRequestDto();
        List<LoanOfferDto> loanOffers = new ArrayList<>();
        LoanOfferDto offer = new LoanOfferDto();
        offer.setStatementId(UUID.randomUUID());
        loanOffers.add(offer);

        when(dealService.calculateLoanConditions(request)).thenReturn(loanOffers);

        ResponseEntity<List<LoanOfferDto>> response = dealController.calculateLoanConditions(request);

        verify(dealService, times(1)).calculateLoanConditions(request);
        assertEquals(ResponseEntity.ok(loanOffers), response);
    }

    @Test
    void selectLoanOffer_ShouldReturnOk() {

        LoanOfferDto offer = new LoanOfferDto();
        offer.setStatementId(UUID.randomUUID());

        ResponseEntity<Void> response = dealController.selectLoanOffer(offer);

        verify(dealService, times(1)).selectLoanOffer(offer);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void finishRegistrationAndCalculate_ShouldReturnOk() {

        String statementId = UUID.randomUUID().toString();
        FinishRegistrationRequestDto request = new FinishRegistrationRequestDto();
        ResponseEntity<Void> response = dealController.finishRegistrationAndCalculate(statementId, request);

        verify(dealService, times(1)).finishRegistrationAndCalculate(statementId, request);
        assertEquals(ResponseEntity.ok().build(), response);
    }
}
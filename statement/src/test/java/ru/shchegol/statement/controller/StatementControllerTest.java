package ru.shchegol.statement.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.statement.service.StatementService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementControllerTest {
    @InjectMocks
    StatementController statementController;
    @Mock
    StatementService statementService;

    @Test
    void statement() {
        statementController.statement(new LoanStatementRequestDto());
        List<LoanOfferDto> loanOffers = Arrays.asList(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());
        when(statementService.getLoanOffers(new LoanStatementRequestDto())).thenReturn(loanOffers);
        assertEquals(ResponseEntity.ok(loanOffers), statementController.statement(new LoanStatementRequestDto()));
    }

    @Test
    void testSelectOffer() {
        statementController.selectOffer(new LoanOfferDto());
        verify(statementService, times(1)).selectOffer(any(LoanOfferDto.class));
    }
}
package ru.shchegol.deal.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.shchegol.deal.dto.*;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Credit;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.entity.enums.ApplicationStatus;
import ru.shchegol.deal.entity.enums.Gender;
import ru.shchegol.deal.entity.enums.MaritalStatus;
import ru.shchegol.deal.entity.jsonb.StatusHistory;
import ru.shchegol.deal.exception.CreditCalculationException;
import ru.shchegol.deal.exception.GetLoanOffersException;
import ru.shchegol.deal.exception.StatementNotFoundException;
import ru.shchegol.deal.repository.ClientRepository;
import ru.shchegol.deal.repository.CreditRepository;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.deal.service.FactorySercice;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StatementRepository statementRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private FactorySercice factorySercice;

    @InjectMocks
    private DealServiceImpl dealService;



    private LoanStatementRequestDto loanStatementRequestDto;
    private FinishRegistrationRequestDto finishRegistrationRequestDto;
    private Client client;
    private Statement statement;
    private CreditDto creditDto;


    @BeforeEach
    void setUp() {
        dealService = new DealServiceImpl(restTemplate, statementRepository, clientRepository, creditRepository, factorySercice);
        loanStatementRequestDto = new LoanStatementRequestDto();

        finishRegistrationRequestDto = new FinishRegistrationRequestDto();
        finishRegistrationRequestDto.setEmployment(new EmploymentDto());

        client = new Client();
        client.setId(UUID.randomUUID());

        statement = new Statement();
        statement.setClientId(client);
    }

    @Test
    void calculateLoanConditions() {
        when(factorySercice.createClient(any(LoanStatementRequestDto.class))).thenReturn(client);
        when(factorySercice.createStatement(any(LoanStatementRequestDto.class), any(Client.class))).thenReturn(statement);
        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(List.of(new LoanOfferDto())));

        List<LoanOfferDto> loanOffers = dealService.calculateLoanConditions(loanStatementRequestDto);

        verify(factorySercice, times(1)).createClient(any(LoanStatementRequestDto.class));
        verify(factorySercice, times(1)).createStatement(any(LoanStatementRequestDto.class), any(Client.class));
        assertFalse(loanOffers.isEmpty());
    }



    @Test
    void finishRegistrationAndCalculate_statementNotFound() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(StatementNotFoundException.class, () ->
                dealService.finishRegistrationAndCalculate(UUID.randomUUID().toString(), finishRegistrationRequestDto));
    }

    @Test
    void getLoanOffers_failure() {
        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.badRequest().body(creditDto));


        assertThrows(GetLoanOffersException.class, () ->
                dealService.calculateLoanConditions(loanStatementRequestDto));
    }

    @Test
    void calculateCredit_failure() {
        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(statement));
        when(restTemplate.exchange(
                anyString(),
                any(),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.badRequest().body(creditDto));


        assertThrows(CreditCalculationException.class, () ->
                dealService.finishRegistrationAndCalculate(UUID.randomUUID().toString(), finishRegistrationRequestDto));
    }
}

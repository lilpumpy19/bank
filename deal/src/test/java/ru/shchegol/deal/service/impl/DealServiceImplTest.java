package ru.shchegol.deal.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.shchegol.deal.dto.*;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.exception.CreditCalculationException;
import ru.shchegol.deal.exception.GetLoanOffersException;
import ru.shchegol.deal.exception.StatementNotFoundException;
import ru.shchegol.deal.mapper.DealMapper;
import ru.shchegol.deal.repository.ClientRepository;
import ru.shchegol.deal.repository.CreditRepository;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.deal.service.MessageService;
import ru.shchegol.dto.EmploymentDto;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanStatementRequestDto;

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
    private DealMapper dealMapper;

    @Mock
    MessageService messageService;


    @InjectMocks
    private DealServiceImpl dealService;



    private LoanStatementRequestDto loanStatementRequestDto;
    private FinishRegistrationRequestDto finishRegistrationRequestDto;
    private Client client;
    private Statement statement;
    private CreditDto creditDto;


    @BeforeEach
    void setUp() {
        dealService = new DealServiceImpl(restTemplate, statementRepository, clientRepository, creditRepository,dealMapper,messageService);
        loanStatementRequestDto = new LoanStatementRequestDto();

        finishRegistrationRequestDto = new FinishRegistrationRequestDto();
        finishRegistrationRequestDto.setEmployment(new EmploymentDto());

        client = new Client();
        client.setId(UUID.randomUUID());

        statement = new Statement();
        statement.setClientId(client);
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

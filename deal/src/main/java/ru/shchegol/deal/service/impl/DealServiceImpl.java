package ru.shchegol.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.shchegol.deal.dto.*;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Credit;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.mapper.DealMapper;
import ru.shchegol.deal.service.MessageService;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.dto.ScoringDataDto;
import ru.shchegol.dto.enums.ApplicationStatus;
import ru.shchegol.dto.enums.ChangeType;

import ru.shchegol.deal.entity.jsonb.StatusHistory;
import ru.shchegol.deal.exception.CreditCalculationException;
import ru.shchegol.deal.exception.GetLoanOffersException;
import ru.shchegol.deal.exception.StatementNotFoundException;
import ru.shchegol.deal.repository.ClientRepository;
import ru.shchegol.deal.repository.CreditRepository;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.deal.service.DealService;
import ru.shchegol.deal.dto.CreditDto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final RestTemplate restTemplate;
    private final StatementRepository statementRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final DealMapper dealMapper;
    private final MessageService messageService;

    @Value("${app.base-url}")
    private String BASE_URL;

    @Override
    public List<LoanOfferDto> calculateLoanConditions(LoanStatementRequestDto request) {
        Client client = dealMapper.toClient(request);
        log.info("client save: {}", client);
        clientRepository.save(client);
        Statement statement =dealMapper.toStatement(request,client);
        log.info("statement save: {}", statement);
        statementRepository.save(statement);
        List<LoanOfferDto> loanOffers = getLoanOffers(request);
        setStatementId(loanOffers, statement.getStatementId());

        return loanOffers;
    }

    @Override
    public void selectLoanOffer(LoanOfferDto offer) {
        Optional<Statement> optionalStatement = statementRepository.findById(offer.getStatementId());

        if (optionalStatement.isPresent()) {
            Statement statement = optionalStatement.get();
            statement.setStatus(ApplicationStatus.DOCUMENT_CREATED);
            statement.addStatusHistory(
                    new StatusHistory("document_created",
                            new Timestamp(System.currentTimeMillis()),
                            ChangeType.AUTOMATIC));
            statement.setAppliedOffer(offer);
            statementRepository.save(statement);
            messageService.finishRegistration(offer.getStatementId().toString());
        } else {
            throw new StatementNotFoundException("Failed to get statement");
        }
    }

    @Override
    public void finishRegistrationAndCalculate(String statementId, FinishRegistrationRequestDto request) {
        Optional<Statement> optionalStatement = statementRepository.findById(UUID.fromString(statementId));
        if (optionalStatement.isPresent()) {
            ScoringDataDto scoringDataDto = dealMapper.toScoringDataDto(request,optionalStatement.get());
            CreditDto creditDto = calculateCredit(scoringDataDto);
            Credit credit = dealMapper.toCredit(creditDto);

            optionalStatement.get().setStatus(ApplicationStatus.APPROVED);
            optionalStatement.get().addStatusHistory(
                    new StatusHistory("approved",
                            new Timestamp(System.currentTimeMillis()),
                            ChangeType.AUTOMATIC));
            creditRepository.save(credit);
            optionalStatement.get().setCreditId(credit);
            statementRepository.save(optionalStatement.get());
            messageService.createDocuments(statementId);
        } else {
            throw new StatementNotFoundException("Failed to get statement with id " + statementId);
        }
    }


    private List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        HttpEntity<LoanStatementRequestDto> request = new HttpEntity<>(loanStatementRequestDto);
        log.info("offers request: {}", loanStatementRequestDto);
        ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(
                BASE_URL + "offers",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<LoanOfferDto>>() {
                }
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("offers response: {}", response.getBody());
            return response.getBody();
        } else {
            throw new GetLoanOffersException("Failed to get loan offers");
        }

    }

    private void setStatementId(List<LoanOfferDto> loanOffers, UUID id) {
        for (LoanOfferDto loanOfferDto : loanOffers) {
            loanOfferDto.setStatementId(id);
        }
    }


    private CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        HttpEntity<ScoringDataDto> request = new HttpEntity<>(scoringDataDto);
        log.info("calc request: {}", scoringDataDto);
        ResponseEntity<CreditDto> response = restTemplate.exchange(
                BASE_URL + "calc",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<CreditDto>() {
                }
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("calc response: {}", response.getBody());
            return response.getBody();
        } else {
            throw new CreditCalculationException("Failed to calculate credit");
        }
    }


}

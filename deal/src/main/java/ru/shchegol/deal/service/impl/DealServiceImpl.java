package ru.shchegol.deal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.shchegol.deal.entity.enums.ApplicationStatus;
import ru.shchegol.deal.entity.enums.ChangeType;

import ru.shchegol.deal.entity.jsonb.StatusHistory;
import ru.shchegol.deal.exception.CreditCalculationException;
import ru.shchegol.deal.exception.GetLoanOffersException;
import ru.shchegol.deal.exception.StatementNotFoundException;
import ru.shchegol.deal.repository.ClientRepository;
import ru.shchegol.deal.repository.CreditRepository;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.deal.service.DealService;
import ru.shchegol.deal.service.FactorySercice;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    @Autowired
    private RestTemplate restTemplate;
    private final StatementRepository statementRepository;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final FactorySercice factorySercice;
    @Value("${app.base-url}")
    private String BASE_URL;

    @Override
    public List<LoanOfferDto> calculateLoanConditions(LoanStatementRequestDto request) {
        Client client = factorySercice.createClient(request);
        clientRepository.save(client);
        Statement statement = factorySercice.createStatement(request, client);
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
        } else {
            throw new StatementNotFoundException("Failed to get statement");
        }
    }

    @Override
    public void finishRegistrationAndCalculate(String statementId, FinishRegistrationRequestDto request) {

        Optional<Statement> optionalStatement = statementRepository.findById(UUID.fromString(statementId));
        if (optionalStatement.isPresent()) {
            ScoringDataDto scoringDataDto = factorySercice.createScoringData(request, optionalStatement.get());
            CreditDto creditDto = calculateCredit(scoringDataDto);
            Credit credit = factorySercice.createCredit(creditDto);

            optionalStatement.get().setStatus(ApplicationStatus.APPROVED);
            optionalStatement.get().addStatusHistory(
                    new StatusHistory("approved",
                            new Timestamp(System.currentTimeMillis()),
                            ChangeType.AUTOMATIC));
            creditRepository.save(credit);
            optionalStatement.get().setCreditId(credit);
            statementRepository.save(optionalStatement.get());


        } else {
            throw new StatementNotFoundException("Failed to get statement with id " + statementId);
        }
    }


    private List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        HttpEntity<LoanStatementRequestDto> request = new HttpEntity<>(loanStatementRequestDto);

        // Выполните POST-запрос
        ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(
                BASE_URL + "offers",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<LoanOfferDto>>() {
                }
        );
        if (response.getStatusCode().is2xxSuccessful()) {
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

        // Выполните POST-запрос
        ResponseEntity<CreditDto> response = restTemplate.exchange(
                BASE_URL + "calc",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<CreditDto>() {
                }
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new CreditCalculationException("Failed to calculate credit");
        }
    }


}

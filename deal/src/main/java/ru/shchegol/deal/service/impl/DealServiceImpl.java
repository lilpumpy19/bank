package ru.shchegol.deal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.shchegol.deal.entity.enums.CreditStatus;
import ru.shchegol.deal.entity.jsonb.Employment;
import ru.shchegol.deal.entity.jsonb.Passport;
import ru.shchegol.deal.entity.jsonb.StatusHistory;
import ru.shchegol.deal.repository.ClientRepository;
import ru.shchegol.deal.repository.CreditRepository;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.deal.service.DealService;

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

    @Override
    public List<LoanOfferDto> calculateLoanConditions(LoanStatementRequestDto request) {
        Client client = createClient(request);
        clientRepository.save(client);
        Statement statement = createStatement(request,client);
        statementRepository.save(statement);
        List<LoanOfferDto> loanOffers=getLoanOffers(request);
        setStatementId(loanOffers,statement.getStatementId());

        return loanOffers;
    }




    private Client createClient(LoanStatementRequestDto request) {
        Client client = new Client();
                client.setFirstName(request.getFirstName());
                client.setLastName(request.getLastName());
                client.setMiddleName(request.getMiddleName());
                client.setEmail(request.getEmail());
                client.setBirthDate(request.getBirthdate());
                client.setPassport(new Passport(request.getPassportSeries(), request.getPassportNumber()));
                client.setEmployment(new Employment());
        return client;
    }

    private Statement createStatement(LoanStatementRequestDto request, Client client) {
        Statement statement = new Statement();
        statement.setClientId(client);
        statement.setCreationDate(new Timestamp(System.currentTimeMillis()));
        statement.setStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        statement.addStatusHistory(
                new StatusHistory("prepare_documents",
                new Timestamp(System.currentTimeMillis()),
                        ChangeType.AUTOMATIC));
        return statement;
    }

    private List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        HttpEntity<LoanStatementRequestDto> request = new HttpEntity<>(loanStatementRequestDto);

        // Выполните POST-запрос
        ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(
                "http://localhost:8080/calculator/offers",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<LoanOfferDto>>() {}
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }else {
            //todo обработать исключение
            System.out.println("ERROR");
            return List.of();
        }

    }

    private void setStatementId(List<LoanOfferDto> loanOffers, UUID id) {
        for (LoanOfferDto loanOfferDto : loanOffers) {
            loanOfferDto.setStatementId(id);
        }
    }



    ///////////////////////////////////////////////////////////////////////////////////

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
        }else {
            System.out.println("ERROR");
            //todo обработать исключение
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void finishRegistrationAndCalculate(String statementId, FinishRegistrationRequestDto request) {

        Optional<Statement> optionalStatement = statementRepository.findById(UUID.fromString(statementId));
        if (optionalStatement.isPresent()) {
            ScoringDataDto scoringDataDto = createScoringData(request, optionalStatement.get());
            CreditDto creditDto = calculateCredit(scoringDataDto);
            Credit credit = createCredit(creditDto);

            optionalStatement.get().setStatus(ApplicationStatus.APPROVED);
            optionalStatement.get().addStatusHistory(
                    new StatusHistory("approved",
                            new Timestamp(System.currentTimeMillis()),
                            ChangeType.AUTOMATIC));
            creditRepository.save(credit);
            optionalStatement.get().setCreditId(credit);
            statementRepository.save(optionalStatement.get());


        }else {
            System.out.println("ERROR");
            //todo обработать исключение
        }
    }




    private ScoringDataDto createScoringData(FinishRegistrationRequestDto request, Statement statement) {
        Client client = statement.getClientId();
        LoanOfferDto loanOfferDto = statement.getAppliedOffer();
        ScoringDataDto scoringDataDto = new ScoringDataDto();

        scoringDataDto.setAmount(loanOfferDto.getRequestedAmount());
        scoringDataDto.setTerm(loanOfferDto.getTerm());
        scoringDataDto.setFirstName(client.getFirstName());
        scoringDataDto.setLastName(client.getLastName());
        scoringDataDto.setMiddleName(client.getMiddleName());
        scoringDataDto.setGender(request.getGender());
        scoringDataDto.setBirthdate(client.getBirthDate());
        scoringDataDto.setPassportSeries(client.getPassport().getSeries());
        scoringDataDto.setPassportNumber(client.getPassport().getNumber());
        scoringDataDto.setPassportIssueDate(request.getPassportIssueDate());
        scoringDataDto.setPassportIssueBranch(request.getPassportIssueBranch());
        scoringDataDto.setMaritalStatus(request.getMaritalStatus());
        scoringDataDto.setDependentAmount(request.getDependentAmount());
        scoringDataDto.setEmployment(request.getEmployment());
        scoringDataDto.setAccountNumber(request.getAccountNumber());
        scoringDataDto.setIsInsuranceEnabled(loanOfferDto.getIsInsuranceEnabled());
        scoringDataDto.setIsSalaryClient(loanOfferDto.getIsSalaryClient());

        return scoringDataDto;
    }

    private CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        HttpEntity<ScoringDataDto> request = new HttpEntity<>(scoringDataDto);

        // Выполните POST-запрос
        ResponseEntity<CreditDto> response = restTemplate.exchange(
                "http://localhost:8080/calculator/calc",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<CreditDto>() {}
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }else {
            //todo обработать исключение
            System.out.println("ERROR");
            return null;
        }
    }

    private Credit createCredit(CreditDto creditDto) {
       Credit credit = new Credit();

       credit.setAmount(creditDto.getAmount());
       credit.setTerm(creditDto.getTerm());
       credit.setMonthlyPayment(creditDto.getMonthlyPayment());
       credit.setRate(creditDto.getRate());
       credit.setPsk(creditDto.getPsk());
       credit.setPaymentSchedule(creditDto.getPaymentSchedule());
       credit.setInsuranceEnabled(creditDto.getIsInsuranceEnabled());
       credit.setSalaryClient(creditDto.getIsSalaryClient());
       credit.setCreditStatus(CreditStatus.CALCULATED);

       return credit;
    }
}

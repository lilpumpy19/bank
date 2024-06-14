package ru.shchegol.deal.service.impl;

import org.springframework.stereotype.Service;
import ru.shchegol.deal.dto.*;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Credit;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.dto.ScoringDataDto;
import ru.shchegol.dto.enums.ApplicationStatus;
import ru.shchegol.dto.enums.ChangeType;
import ru.shchegol.dto.enums.CreditStatus;
import ru.shchegol.deal.entity.jsonb.Employment;
import ru.shchegol.deal.entity.jsonb.Passport;
import ru.shchegol.deal.entity.jsonb.StatusHistory;
import ru.shchegol.deal.service.FactorySercice;
import ru.shchegol.deal.dto.CreditDto;

import java.sql.Timestamp;

@Service
public class FactoryServiceImpl implements FactorySercice {

    public Client createClient(LoanStatementRequestDto request) {
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

    public Statement createStatement(LoanStatementRequestDto request, Client client) {
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


    public ScoringDataDto createScoringData(FinishRegistrationRequestDto request, Statement statement) {
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

    public Credit createCredit(CreditDto creditDto) {
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

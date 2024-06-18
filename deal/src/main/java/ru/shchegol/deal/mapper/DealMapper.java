package ru.shchegol.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.shchegol.deal.dto.CreditDto;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Credit;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.entity.jsonb.Employment;

import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.dto.ScoringDataDto;

@Mapper(componentModel = "spring")
public interface DealMapper {

    DealMapper INSTANCE = Mappers.getMapper(DealMapper.class);


    @Mapping(source = "request.firstName", target = "firstName")
    @Mapping(source = "request.lastName", target = "lastName")
    @Mapping(source = "request.middleName", target = "middleName")
    @Mapping(source = "request.email", target = "email")
    @Mapping(source = "request.birthdate", target = "birthDate")
    @Mapping(source = "request.passportSeries", target = "passport.series")
    @Mapping(source = "request.passportNumber", target = "passport.number")
    @Mapping(target = "employment", expression = "java(new ru.shchegol.deal.entity.jsonb.Employment())")
    Client toClient(LoanStatementRequestDto request);


    @Mapping(target = "clientId", source = "client")
    @Mapping(target = "creationDate", expression = "java(new java.sql.Timestamp(System.currentTimeMillis()))")
    @Mapping(target = "status", constant = "PREPARE_DOCUMENTS")
    @Mapping(target = "statusHistory", expression = "java(java.util.Collections.singletonList(new ru.shchegol.deal.entity.jsonb.StatusHistory(\"prepare_documents\", new java.sql.Timestamp(System.currentTimeMillis()), ru.shchegol.dto.enums.ChangeType.AUTOMATIC)))")
    Statement toStatement(LoanStatementRequestDto request, Client client);


    @Mapping(target = "amount", source = "statement.appliedOffer.requestedAmount")
    @Mapping(target = "term", source = "statement.appliedOffer.term")
    @Mapping(target = "firstName", source = "statement.clientId.firstName")
    @Mapping(target = "lastName", source = "statement.clientId.lastName")
    @Mapping(target = "middleName", source = "statement.clientId.middleName")
    @Mapping(target = "birthdate", source = "statement.clientId.birthDate")
    @Mapping(target = "passportSeries", source = "statement.clientId.passport.series")
    @Mapping(target = "passportNumber", source = "statement.clientId.passport.number")
    @Mapping(target = "isInsuranceEnabled", source = "statement.appliedOffer.isInsuranceEnabled")
    @Mapping(target = "isSalaryClient", source = "statement.appliedOffer.isSalaryClient")
    @Mapping(target = "employment", source = "request.employment")
    @Mapping(target = "accountNumber", source = "request.accountNumber")
    @Mapping(target = "gender", source = "request.gender")
    @Mapping(target = "passportIssueDate", source = "request.passportIssueDate")
    @Mapping(target = "passportIssueBranch", source = "request.passportIssueBranch")
    @Mapping(target = "maritalStatus", source = "request.maritalStatus")
    @Mapping(target = "dependentAmount", source = "request.dependentAmount")
    ScoringDataDto toScoringDataDto(FinishRegistrationRequestDto request, Statement statement);


    @Mapping(target = "amount", source = "creditDto.amount")
    @Mapping(target = "term", source = "creditDto.term")
    @Mapping(target = "monthlyPayment", source = "creditDto.monthlyPayment")
    @Mapping(target = "rate", source = "creditDto.rate")
    @Mapping(target = "psk", source = "creditDto.psk")
    @Mapping(target = "paymentSchedule", source = "creditDto.paymentSchedule")
    @Mapping(target = "insuranceEnabled", source = "creditDto.isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "creditDto.isSalaryClient")
    @Mapping(target = "creditStatus", constant = "CALCULATED")
    Credit toCredit(CreditDto creditDto);
}

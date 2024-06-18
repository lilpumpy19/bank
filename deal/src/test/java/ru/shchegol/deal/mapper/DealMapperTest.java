package ru.shchegol.deal.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.shchegol.deal.dto.CreditDto;

import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Credit;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.entity.jsonb.Employment;
import ru.shchegol.deal.entity.jsonb.Passport;
import ru.shchegol.deal.entity.jsonb.StatusHistory;
import ru.shchegol.dto.EmploymentDto;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.dto.ScoringDataDto;
import ru.shchegol.dto.enums.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DealMapperTest {

    private final DealMapper dealMapper = Mappers.getMapper(DealMapper.class);

    @Test
    void LoanStatementToClient() {

        LoanStatementRequestDto request = new LoanStatementRequestDto();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setMiddleName("Michael");
        request.setEmail("john.doe@example.com");
        request.setBirthdate(LocalDate.of(1990, 1, 1));
        request.setPassportSeries("1234");
        request.setPassportNumber("567890");

        Client client = dealMapper.toClient(request);

        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("Michael", client.getMiddleName());
        assertEquals("john.doe@example.com", client.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), client.getBirthDate());
        assertEquals("1234", client.getPassport().getSeries());
        assertEquals("567890", client.getPassport().getNumber());
        assertNotNull(client.getEmployment());

    }

    @Test
    void toStatement() {
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        Client client = new Client();

        Statement statement = dealMapper.toStatement(request, client);

        assertEquals(client, statement.getClientId());
        assertNotNull(statement.getCreationDate());
        assertEquals(ApplicationStatus.PREPARE_DOCUMENTS, statement.getStatus());
        assertEquals(1, statement.getStatusHistory().size());

        StatusHistory history = statement.getStatusHistory().get(0);
        assertEquals("prepare_documents", history.getStatus());
        assertEquals(ChangeType.AUTOMATIC, history.getChangeType());
        assertNotNull(history.getTime());
    }

    @Test
    void toScoringDataDto() {

        LoanOfferDto loanOfferDto = mock(LoanOfferDto.class);
        when(loanOfferDto.getRequestedAmount()).thenReturn(new BigDecimal("100000"));
        when(loanOfferDto.getTerm()).thenReturn(12);
        when(loanOfferDto.getIsInsuranceEnabled()).thenReturn(true);
        when(loanOfferDto.getIsSalaryClient()).thenReturn(false);

        Statement statement = mock(Statement.class);
        Client client = new Client();
        client.setPassport(new Passport("1234", "567890"));
        when(statement.getClientId()).thenReturn(client);
        when(statement.getAppliedOffer()).thenReturn(loanOfferDto);



        FinishRegistrationRequestDto finishRegistrationRequestDto = new FinishRegistrationRequestDto();
        finishRegistrationRequestDto.setGender(Gender.MALE);
        finishRegistrationRequestDto.setPassportIssueDate(LocalDate.of(2023, 1, 1));
        finishRegistrationRequestDto.setPassportIssueBranch("123-456");
        finishRegistrationRequestDto.setMaritalStatus(MaritalStatus.MARRIED);
        finishRegistrationRequestDto.setDependentAmount(2);
        finishRegistrationRequestDto.setEmployment(new EmploymentDto());
        finishRegistrationRequestDto.setAccountNumber("1234567890");



        ScoringDataDto scoringDataDto = dealMapper.toScoringDataDto(finishRegistrationRequestDto, statement);


        assertEquals(new BigDecimal("100000"), scoringDataDto.getAmount());
        assertEquals(12, scoringDataDto.getTerm());
        assertEquals(Gender.MALE, scoringDataDto.getGender());
        assertEquals(LocalDate.of(2023, 1, 1), scoringDataDto.getPassportIssueDate());
        assertEquals("123-456", scoringDataDto.getPassportIssueBranch());
        assertEquals(MaritalStatus.MARRIED, scoringDataDto.getMaritalStatus());
        assertEquals(2, scoringDataDto.getDependentAmount());
        assertEquals("1234567890", scoringDataDto.getAccountNumber());
        assertTrue(scoringDataDto.getIsInsuranceEnabled());
        assertFalse(scoringDataDto.getIsSalaryClient());
    }

    @Test
    void toCredit() {
        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(new BigDecimal("100000"));
        creditDto.setTerm(12);
        creditDto.setMonthlyPayment(new BigDecimal("10000"));
        creditDto.setRate(new BigDecimal("10"));
        creditDto.setPsk(new BigDecimal("12"));
        creditDto.setPaymentSchedule(List.of());
        creditDto.setIsInsuranceEnabled(true);
        creditDto.setIsSalaryClient(false);

        Credit credit = dealMapper.toCredit(creditDto);

        assertEquals(new BigDecimal("100000"), credit.getAmount());
        assertEquals(12, credit.getTerm());
        assertEquals(new BigDecimal("10000"), credit.getMonthlyPayment());
        assertEquals(new BigDecimal("10"), credit.getRate());
        assertEquals(new BigDecimal("12"), credit.getPsk());
        assertEquals(List.of(), credit.getPaymentSchedule());
        assertTrue(credit.isInsuranceEnabled());
        assertFalse(credit.isSalaryClient());
        assertEquals(CreditStatus.CALCULATED, credit.getCreditStatus());
    }
}
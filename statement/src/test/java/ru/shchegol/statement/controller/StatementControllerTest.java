package ru.shchegol.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.statement.service.StatementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatementController.class)
class StatementControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    StatementService statementService;

    LoanOfferDto loanOfferDto;

    LoanStatementRequestDto loanStatementRequestDto;

    @BeforeEach
    void setUp() {
        loanOfferDto = new LoanOfferDto();
        loanOfferDto.setStatementId(UUID.randomUUID());
        loanOfferDto.setRequestedAmount(new BigDecimal("100000"));
        loanOfferDto.setTotalAmount(new BigDecimal("110000"));
        loanOfferDto.setTerm(12);
        loanOfferDto.setMonthlyPayment(new BigDecimal("10000"));
        loanOfferDto.setRate(new BigDecimal("10"));
        loanOfferDto.setIsInsuranceEnabled(true);
        loanOfferDto.setIsSalaryClient(true);

        loanStatementRequestDto = new LoanStatementRequestDto();
        loanStatementRequestDto.setAmount(new BigDecimal("50000"));
        loanStatementRequestDto.setTerm(12);
        loanStatementRequestDto.setFirstName("John");
        loanStatementRequestDto.setLastName("Doe");
        loanStatementRequestDto.setMiddleName("Michael");
        loanStatementRequestDto.setEmail("john.doe@example.com");
        loanStatementRequestDto.setBirthdate(LocalDate.now().minusYears(20));
        loanStatementRequestDto.setPassportSeries("1234");
        loanStatementRequestDto.setPassportNumber("567890");
    }

    @Test
    void statement() throws Exception {
        when(statementService.getLoanOffers(any(LoanStatementRequestDto.class))).thenReturn(Arrays.asList(loanOfferDto));
        mockMvc.perform(MockMvcRequestBuilders.post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanStatementRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testSelectOffer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJson(loanOfferDto)))
                .andExpect(status().isOk());
    }

    private String objectToJson(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}

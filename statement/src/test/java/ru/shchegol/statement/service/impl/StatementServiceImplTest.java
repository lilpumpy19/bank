package ru.shchegol.statement.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.statement.exception.GetLoanOffersException;
import ru.shchegol.statement.feignclient.BankFeignClient;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StatementServiceImplTest {
    @Mock
    BankFeignClient feignClient;
    @InjectMocks
    StatementServiceImpl statementService;
    @Mock
    LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        statementService = new StatementServiceImpl(feignClient);
        loanOfferDto = new LoanOfferDto();

    }

    @Test
    void getLoanOffers_failure() {
        when(feignClient.getLoanOffers(any())).thenReturn(ResponseEntity.badRequest().body(null));
        assertThrows(GetLoanOffersException.class, () -> statementService.getLoanOffers(new LoanStatementRequestDto()));
    }

    @Test
    void getLoanOffers_success() {
        when(feignClient.getLoanOffers(any())).
                thenReturn(ResponseEntity.ok(Arrays.asList(loanOfferDto, loanOfferDto, loanOfferDto, loanOfferDto)));
        assertDoesNotThrow(() -> statementService.getLoanOffers(new LoanStatementRequestDto()));
    }


}
package ru.shchegol.statement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.statement.exception.GetLoanOffersException;
import ru.shchegol.statement.exception.SelectOfferException;
import ru.shchegol.statement.feignclient.BankFeignClient;
import ru.shchegol.statement.service.StatementService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatementServiceImpl implements StatementService {
    private final BankFeignClient bankFeignClient;

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatement) {
        log.info("Request to GetLoanOffers: {}", loanStatement);
        ResponseEntity<List<LoanOfferDto>> response = bankFeignClient.getLoanOffers(loanStatement);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Response from GetLoanOffers: {}", response.getBody());
            return response.getBody();
        }else {
            throw new GetLoanOffersException("Failed to get loan offers");
        }
    }

    @Override
    public void selectOffer(LoanOfferDto loanOffer) {
        log.info("Request to SelectOffer: {}", loanOffer);
        ResponseEntity<Void> response = bankFeignClient.selectOffer(loanOffer);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new SelectOfferException("Failed to select loan offer");
        }
        log.info("Response from SelectOffer: Successful");
    }
}

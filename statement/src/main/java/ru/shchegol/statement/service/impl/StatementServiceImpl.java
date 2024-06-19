package ru.shchegol.statement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.statement.exception.GetLoanOffersException;
import ru.shchegol.statement.exception.SelectOfferException;
import ru.shchegol.statement.service.StatementService;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class StatementServiceImpl implements StatementService {
    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String BASE_URL;

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatement) {
        log.info("Request to GetLoanOffers: {}", loanStatement);
        HttpEntity<LoanStatementRequestDto> request = new HttpEntity<>(loanStatement);
        ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(
                BASE_URL + "statement",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<LoanOfferDto>>() {
                }
        );
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
        HttpEntity<LoanOfferDto> request = new HttpEntity<>(loanOffer);
        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URL + "offer/select",
                HttpMethod.POST,
                request,
                Void.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new SelectOfferException("Failed to select loan offer");
        }
        log.info("Response from SelectOffer: Successful");
    }
}

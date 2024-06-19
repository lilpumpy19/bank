package ru.shchegol.statement.service.impl;

import lombok.RequiredArgsConstructor;
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
import ru.shchegol.statement.service.StatementService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StatementServiceImpl implements StatementService {
    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String BASE_URL;

    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatement) {
        HttpEntity<LoanStatementRequestDto> request = new HttpEntity<>(loanStatement);
        ResponseEntity<List<LoanOfferDto>> response = restTemplate.exchange(
                BASE_URL + "statement",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<LoanOfferDto>>() {
                }
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }else {
            throw new GetLoanOffersException("Failed to get loan offers");
        }
    }
}

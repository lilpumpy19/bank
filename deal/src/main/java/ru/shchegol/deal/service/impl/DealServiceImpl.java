package ru.shchegol.deal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.shchegol.deal.dto.FinishRegistrationRequestDto;
import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.deal.dto.LoanStatementRequestDto;
import ru.shchegol.deal.entity.Client;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.entity.enums.ApplicationStatus;
import ru.shchegol.deal.entity.enums.ChangeType;
import ru.shchegol.deal.entity.jsonb.Employment;
import ru.shchegol.deal.entity.jsonb.Passport;
import ru.shchegol.deal.entity.jsonb.StatusHistory;
import ru.shchegol.deal.repository.ClientRepository;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.deal.service.DealService;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    @Autowired
    private RestTemplate restTemplate;
    private final StatementRepository statementRepository;
    private final ClientRepository clientRepository;

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
        statement.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        statement.addStatusHistory(
                new StatusHistory("ok",
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
            System.out.println("ERROR");
            return List.of();
        }

    }

    private void setStatementId(List<LoanOfferDto> loanOffers, UUID id) {
        for (LoanOfferDto loanOfferDto : loanOffers) {
            loanOfferDto.setStatementId(id);
        }
    }

    @Override
    public void selectLoanOffer(LoanOfferDto offer) {

    }

    @Override
    public void finishRegistrationAndCalculate(String statementId, FinishRegistrationRequestDto request) {

    }
}

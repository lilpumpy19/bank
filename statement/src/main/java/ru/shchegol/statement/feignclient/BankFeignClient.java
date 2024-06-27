package ru.shchegol.statement.feignclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "bankClient", url = "${app.base-url}")
public interface BankFeignClient {
    @PostMapping("statement")
    ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody LoanStatementRequestDto loanStatement);

    @PostMapping("offer/select")
    ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto loanOffer);
}

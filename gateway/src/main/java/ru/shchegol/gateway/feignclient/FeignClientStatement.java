package ru.shchegol.gateway.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "statementClient", url = "${app.url.statement}")
public interface FeignClientStatement {
    @PostMapping
    ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody LoanStatementRequestDto loanStatement);

    @PostMapping("/offer")
    ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto loanOffer);


}

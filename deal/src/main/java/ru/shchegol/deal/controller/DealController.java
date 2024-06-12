package ru.shchegol.deal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shchegol.deal.dto.FinishRegistrationRequestDto;
import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.deal.dto.LoanStatementRequestDto;
import ru.shchegol.deal.service.DealService;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/deal")
public class DealController {

    @Autowired
    private DealService dealService;

    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> calculateLoanConditions(@RequestBody LoanStatementRequestDto request) {
        log.info("calculateLoanConditions: {}", request);
        List<LoanOfferDto> loanOffers = dealService.calculateLoanConditions(request);
        log.info("loanOffers: {}", loanOffers);
        return ResponseEntity.ok(loanOffers);
    }

    @PostMapping("/offer/select")
    public ResponseEntity<Void> selectLoanOffer(@RequestBody LoanOfferDto offer) {
        log.info("selectLoanOffer: {}", offer);
        dealService.selectLoanOffer(offer);
        log.info("selectLoanOffer: success");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> finishRegistrationAndCalculate(@PathVariable String statementId, @RequestBody FinishRegistrationRequestDto request) {
        log.info("finishRegistrationAndCalculate: statementId={}, request={}", statementId, request);
        dealService.finishRegistrationAndCalculate(statementId, request);
        log.info("finishRegistrationAndCalculate: success");
        return ResponseEntity.ok().build();
    }
}

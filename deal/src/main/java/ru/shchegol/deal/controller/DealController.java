package ru.shchegol.deal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shchegol.deal.dto.FinishRegistrationRequestDto;
import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.deal.dto.LoanStatementRequestDto;
import ru.shchegol.deal.service.DealService;

import java.util.List;

@RestController
@RequestMapping("/deal")
public class DealController {

    @Autowired
    private DealService dealService;

    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> calculateLoanConditions(@RequestBody LoanStatementRequestDto request) {
        List<LoanOfferDto> loanOffers = dealService.calculateLoanConditions(request);
        return ResponseEntity.ok(loanOffers);
    }

    @PostMapping("/offer/select")
    public ResponseEntity<Void> selectLoanOffer(@RequestBody LoanOfferDto offer) {
        dealService.selectLoanOffer(offer);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> finishRegistrationAndCalculate(@PathVariable String statementId,
                                                               @RequestBody FinishRegistrationRequestDto request) {
        dealService.finishRegistrationAndCalculate(statementId, request);
        return ResponseEntity.ok().build();
    }
}

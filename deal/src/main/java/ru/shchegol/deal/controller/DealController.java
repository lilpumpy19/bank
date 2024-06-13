package ru.shchegol.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shchegol.deal.dto.FinishRegistrationRequestDto;
import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.deal.dto.LoanStatementRequestDto;
import ru.shchegol.deal.service.DealService;

import java.util.List;

@Tag(name = "Deal")
@Slf4j
@RestController
@RequestMapping("/deal")
public class DealController {

    @Autowired
    private DealService dealService;

    @PostMapping("/statement")
    @Operation(summary = "Calculate loan conditions",
            description = "Client and Statement are created and a list of credits is returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",examples = @ExampleObject(
                            """
                                    [
                                        {
                                            "statementId": "98f746a5-c5cb-4176-86e4-62cd104c7bb8",
                                            "requestedAmount": 50000,
                                            "totalAmount": 63727.44,
                                            "term": 12,
                                            "monthlyPayment": 5310.62,
                                            "rate": 11.0,
                                            "isInsuranceEnabled": true,
                                            "isSalaryClient": true
                                        },
                                        {
                                            "statementId": "98f746a5-c5cb-4176-86e4-62cd104c7bb8",
                                            "requestedAmount": 50000,
                                            "totalAmount": 63892.92,
                                            "term": 12,
                                            "monthlyPayment": 5324.41,
                                            "rate": 12.0,
                                            "isInsuranceEnabled": true,
                                            "isSalaryClient": false
                                        },
                                        {
                                            "statementId": "98f746a5-c5cb-4176-86e4-62cd104c7bb8",
                                            "requestedAmount": 50000,
                                            "totalAmount": 53820.00,
                                            "term": 12,
                                            "monthlyPayment": 4485.00,
                                            "rate": 14.0,
                                            "isInsuranceEnabled": false,
                                            "isSalaryClient": true
                                        },
                                        {
                                            "statementId": "98f746a5-c5cb-4176-86e4-62cd104c7bb8",
                                            "requestedAmount": 50000,
                                            "totalAmount": 54083.88,
                                            "term": 12,
                                            "monthlyPayment": 4506.99,
                                            "rate": 15,
                                            "isInsuranceEnabled": false,
                                            "isSalaryClient": false
                                        }
                                    ]
                                    """
                    ))
            ),
    })
    public ResponseEntity<List<LoanOfferDto>> calculateLoanConditions(@RequestBody LoanStatementRequestDto request) {
        log.info("calculateLoanConditions: {}", request);
        List<LoanOfferDto> loanOffers = dealService.calculateLoanConditions(request);
        log.info("loanOffers: {}", loanOffers);
        return ResponseEntity.ok(loanOffers);
    }

    @PostMapping("/offer/select")
    @Operation(summary = "Select loan offer",
            description = "The application saves the selected credit offer in the statement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",description = "Failed to get statement")
    })
    public ResponseEntity<Void> selectLoanOffer(@RequestBody LoanOfferDto offer) {
        log.info("selectLoanOffer: {}", offer);
        dealService.selectLoanOffer(offer);
        log.info("selectLoanOffer: success");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate/{statementId}")
    @Operation(summary = "Finish registration and calculate credit",
            description = "Creation and final credit calculation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",description = "Failed to get statement with id {statementId}")
    })
    public ResponseEntity<Void> finishRegistrationAndCalculate(@PathVariable String statementId,
                                                               @RequestBody FinishRegistrationRequestDto request) {
        log.info("finishRegistrationAndCalculate: statementId={}, request={}", statementId, request);
        dealService.finishRegistrationAndCalculate(statementId, request);
        log.info("finishRegistrationAndCalculate: success");
        return ResponseEntity.ok().build();
    }
}

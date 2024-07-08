package ru.shchegol.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shchegol.deal.service.MessageService;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.deal.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.deal.service.DealService;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Deal")
@Slf4j
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {


    private final DealService dealService;
    private final MessageService messageService;

    @PostMapping("/statement")
    @Operation(summary = "Calculate loan conditions",
            description = "Client and Statement are created and a list of credits is returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class),
                                    minItems = 4,
                                    maxItems = 4
                    ))
            ),
    })
    public ResponseEntity<List<LoanOfferDto>> calculateLoanConditions
            (@RequestBody @Valid LoanStatementRequestDto request) {
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
    public ResponseEntity<Void> selectLoanOffer(@RequestBody @Valid LoanOfferDto offer) {
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
                                                               @RequestBody @Valid
                                                               FinishRegistrationRequestDto request) {
        log.info("finishRegistrationAndCalculate: statementId={}, request={}", statementId, request);
        dealService.finishRegistrationAndCalculate(statementId, request);
        log.info("finishRegistrationAndCalculate: success");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/document/{statementId}/send")
    public ResponseEntity<Void> sendDocument(@PathVariable String statementId){
        messageService.sendDocuments(statementId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/document/{statementId}/sign")
    public ResponseEntity<Void> signDocument(@PathVariable String statementId){
        messageService.creditIssued(statementId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/document/{statementId}/code")
    public ResponseEntity<Void> codeDocument(@PathVariable String statementId){
        messageService.sendSes(statementId);
        return ResponseEntity.ok().build();
    }



}

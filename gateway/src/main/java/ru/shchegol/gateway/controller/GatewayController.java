package ru.shchegol.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shchegol.dto.FinishRegistrationRequestDto;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.gateway.service.GatewayService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
public class GatewayController {
    private final GatewayService gatewayService;

    @PostMapping("/statement")
    @Operation(summary = "Calculate loan offers", description = "Receives LoanStatement and sends data to calculate 4 loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class),
                                    minItems = 4,
                                    maxItems = 4
                            )))
    })
    public ResponseEntity<List<LoanOfferDto>> statement(@Valid @RequestBody LoanStatementRequestDto loanStatement) {
        return gatewayService.getLoanOffers(loanStatement);
    }

    @PostMapping("/statement/offer")
    @Operation(summary = "Select loan offer",
            description = "The service accepts data to save the selected credit offer to the deal service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",description = "Failed to select loan offer")
    })
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto loanOffer) {
        return gatewayService.selectOffer(loanOffer);
    }

    @PostMapping("/deal/calculate/{statementId}")
    @Operation(summary = "Finish registration and calculate credit",
            description = "Creation and final credit calculation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",description = "Failed to get statement with id {statementId}")
    })
    public ResponseEntity<Void> finishRegistrationAndCalculate(@PathVariable String statementId,
                                                               @RequestBody @Valid
                                                               FinishRegistrationRequestDto request) {
        return gatewayService.finishRegistrationAndCalculate(statementId, request);
    }

    @PostMapping("/deal/document/{statementId}/send")
    @Operation(summary = "Request to send documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    public ResponseEntity<Void> sendDocument(@PathVariable String statementId){
        return gatewayService.sendDocuments(statementId);
    }

    @Operation(summary = "Request to sign documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    @PostMapping("/deal/document/{statementId}/sign")
    public ResponseEntity<Void> signDocument(@PathVariable String statementId){
        return gatewayService.creditIssued(statementId);
    }

    @Operation(summary = "Request to send code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    @PostMapping("/deal/document/{statementId}/code")
    public ResponseEntity<Void> codeDocument(@PathVariable String statementId){
        return gatewayService.sendSes(statementId);
    }



}

package ru.shchegol.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shchegol.dto.CreditDto;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.dto.ScoringDataDto;
import ru.shchegol.calculator.service.CalculatorCreditService;
import ru.shchegol.calculator.service.CalculatorOfferService;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Calculator")
@Slf4j
@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController {
    private final CalculatorOfferService calculatorService;
    private final CalculatorCreditService creditService;

    @PostMapping("/calc")
    @Operation(summary = "Calculate credit", description = "Calculates credit based on scoring data and responds with a specific offer or rejection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CreditDto.class)
                            ))),
            @ApiResponse(responseCode = "400", description = "Refusal due to negative work experience",
                    content = @Content(mediaType = "application/json")),

    })
    public ResponseEntity<CreditDto> calc(@Valid @RequestBody ScoringDataDto scoringData) {
        log.info("Request: {}", scoringData);
        ResponseEntity<CreditDto> response = creditService.calcCredit(scoringData);
        log.info("Response: {}", response);
        return response;
    }

    @PostMapping("/offers")
    @Operation(summary = "Calculate loan offers", description = "Calculates loan offers based on loan statement data and issues 4 offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class),
                                    minItems = 4,
                                    maxItems = 4
                            )))
    })
    public ResponseEntity<List<LoanOfferDto>> offers(@RequestBody LoanStatementRequestDto loanStatement) {
        log.info("Request: {}", loanStatement);
        ResponseEntity<List<LoanOfferDto>> response = calculatorService.calcLoanOffers(loanStatement);
        log.info("Response: {}", response);
        return response;
    }

}

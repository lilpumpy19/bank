package ru.shchegol.statement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shchegol.dto.LoanOfferDto;
import ru.shchegol.dto.LoanStatementRequestDto;
import ru.shchegol.statement.service.StatementService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController {
    private final StatementService statementService;

    @PostMapping
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
        return ResponseEntity.ok(statementService.getLoanOffers(loanStatement));
    }

    @PostMapping("/offer")
    @Operation(summary = "Select loan offer",
            description = "The service accepts data to save the selected credit offer to the deal service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",description = "Failed to select loan offer")
    })
    private ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto loanOffer) {
        statementService.selectOffer(loanOffer);
        return ResponseEntity.ok().build();
    }


}

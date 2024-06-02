package ru.shchegol.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shchegol.calculator.dto.CreditDto;
import ru.shchegol.calculator.dto.LoanOfferDto;
import ru.shchegol.calculator.dto.LoanStatementRequestDto;
import ru.shchegol.calculator.dto.ScoringDataDto;
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
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "amount": 100000,
                                      "term": 12,
                                      "firstName": "John",
                                      "lastName": "Doe",
                                      "middleName": "Mss",
                                      "gender": "MALE",
                                      "birthdate": "1990-01-01",
                                      "passportSeries": "1234",
                                      "passportNumber": "123456",
                                      "passportIssueDate": "2010-01-01",
                                      "passportIssueBranch": "МВД г. Москва",
                                      "maritalStatus": "SINGLE",
                                      "dependentAmount": 0,
                                      "employment": {
                                        "employmentStatus": "EMPLOYED",
                                        "employerINN": "1234567890",
                                        "salary": 50000,
                                        "position": "EMPLOYEE",
                                        "workExperienceTotal": 18,
                                        "workExperienceCurrent": 3
                                      },
                                      "accountNumber": "1234567890",
                                      "isInsuranceEnabled": true,
                                      "isSalaryClient": false
                                    }"""
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "amount": 100,
                                      "term": 1,
                                      "firstName": "n",
                                      "lastName": "e",
                                      "middleName": "s",
                                      "gender": "MALE",
                                      "birthdate": "2009-01-01",
                                      "passportSeries": "124",
                                      "passportNumber": "13456",
                                      "passportIssueDate": "2030-01-01",
                                      "passportIssueBranch": "",
                                      "maritalStatus": "SINGLE",
                                      "dependentAmount": 0,
                                      "employment": {
                                        "employmentStatus": "EMPLOYED",
                                        "employerINN": "1234567890",
                                        "salary": 50000,
                                        "position": "EMPLOYEE",
                                        "workExperienceTotal": 6,
                                        "workExperienceCurrent": 1
                                      },
                                      "accountNumber": "1234567890",
                                      "isInsuranceEnabled": true,
                                      "isSalaryClient": false
                                    }
                                    """))),

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
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "amount": 100000,
                                        "term": 24,
                                        "firstName": "John",
                                        "lastName": "Doe",
                                        "middleName": "Mss",
                                        "email": "john.doe@example.com",
                                        "birthdate": "1985-01-15",
                                        "passportSeries": "1234",
                                        "passportNumber": "567890"
                                    }"""
                            ))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "amount": 1000,
                                        "term": 4,
                                        "firstName": "J",
                                        "lastName": "e",
                                        "middleName": "s",
                                        "email": "john.doeexample.com",
                                        "birthdate": "2009-01-15",
                                        "passportSeries": "123",
                                        "passportNumber": "56890"
                                    }"""
                            ))),

    })
    public ResponseEntity<List<LoanOfferDto>> offers(@Valid @RequestBody LoanStatementRequestDto loanStatement) {
        log.info("Request: {}", loanStatement);
        ResponseEntity<List<LoanOfferDto>> response = calculatorService.calcLoanOffers(loanStatement);
        log.info("Response: {}", response);
        return response;
    }

}

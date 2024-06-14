package ru.shchegol.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
                            examples = @ExampleObject(value = """
                                    {
                                        "amount": 62175.96,
                                        "term": 12,
                                        "monthlyPayment": 5181.33,
                                        "baseRate": 15,
                                        "rate": 7.0,
                                        "psk": 24.3500,
                                        "isInsuranceEnabled": true,
                                        "isSalaryClient": true,
                                        "paymentSchedule": [
                                            {
                                                "number": 1,
                                                "date": "2024-07-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 348.0000,
                                                "debtPayment": 4833.3300,
                                                "remainingDebt": 55166.6700
                                            },
                                            {
                                                "number": 2,
                                                "date": "2024-08-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 319.9667,
                                                "debtPayment": 4861.3633,
                                                "remainingDebt": 50305.3067
                                            },
                                            {
                                                "number": 3,
                                                "date": "2024-09-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 291.7708,
                                                "debtPayment": 4889.5592,
                                                "remainingDebt": 45415.7475
                                            },
                                            {
                                                "number": 4,
                                                "date": "2024-10-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 263.4113,
                                                "debtPayment": 4917.9187,
                                                "remainingDebt": 40497.8288
                                            },
                                            {
                                                "number": 5,
                                                "date": "2024-11-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 234.8874,
                                                "debtPayment": 4946.4426,
                                                "remainingDebt": 35551.3862
                                            },
                                            {
                                                "number": 6,
                                                "date": "2024-12-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 206.1980,
                                                "debtPayment": 4975.1320,
                                                "remainingDebt": 30576.2542
                                            },
                                            {
                                                "number": 7,
                                                "date": "2025-01-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 177.3423,
                                                "debtPayment": 5003.9877,
                                                "remainingDebt": 25572.2665
                                            },
                                            {
                                                "number": 8,
                                                "date": "2025-02-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 148.3191,
                                                "debtPayment": 5033.0109,
                                                "remainingDebt": 20539.2556
                                            },
                                            {
                                                "number": 9,
                                                "date": "2025-03-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 119.1277,
                                                "debtPayment": 5062.2023,
                                                "remainingDebt": 15477.0533
                                            },
                                            {
                                                "number": 10,
                                                "date": "2025-04-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 89.7669,
                                                "debtPayment": 5091.5631,
                                                "remainingDebt": 10385.4902
                                            },
                                            {
                                                "number": 11,
                                                "date": "2025-05-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 60.2358,
                                                "debtPayment": 5121.0942,
                                                "remainingDebt": 5264.3960
                                            },
                                            {
                                                "number": 12,
                                                "date": "2025-06-03",
                                                "totalPayment": 5181.33,
                                                "interestPayment": 30.5335,
                                                "debtPayment": 5150.7965,
                                                "remainingDebt": 113.5995
                                            }
                                        ]
                                    }
                                 """
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
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "statementId": "8f06330c-7350-4e69-a491-7539764a0f51",
                                            "requestedAmount": 50000,
                                            "totalAmount": 63727.44,
                                            "term": 12,
                                            "monthlyPayment": 5310.62,
                                            "rate": 11.0,
                                            "isInsuranceEnabled": true,
                                            "isSalaryClient": true
                                        },
                                        {
                                            "statementId": "8a8ff3a7-6af5-44bf-a293-0ff166580f0b",
                                            "requestedAmount": 50000,
                                            "totalAmount": 63892.92,
                                            "term": 12,
                                            "monthlyPayment": 5324.41,
                                            "rate": 12.0,
                                            "isInsuranceEnabled": true,
                                            "isSalaryClient": false
                                        },
                                        {
                                            "statementId": "6f6e3047-bca2-4c85-b821-3f64daa95230",
                                            "requestedAmount": 50000,
                                            "totalAmount": 53820.00,
                                            "term": 12,
                                            "monthlyPayment": 4485.00,
                                            "rate": 14.0,
                                            "isInsuranceEnabled": false,
                                            "isSalaryClient": true
                                        },
                                        {
                                            "statementId": "a65853d5-77db-4d72-808f-dca5c6e7ad8b",
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
                            ))),
            @ApiResponse(responseCode = "404",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value ="""
                                    {
                                       "timestamp": "2024-06-03T11:19:53.504+00:00",
                                           "status": 404,
                                           "error": "Not Found",
                                           "path": "/calculator/offers" 
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

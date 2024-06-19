package ru.shchegol.statement.controller;

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
    public ResponseEntity<List<LoanOfferDto>> statement(@Valid @RequestBody LoanStatementRequestDto loanStatement) {
        return ResponseEntity.ok(statementService.getLoanOffers(loanStatement));
    }


}

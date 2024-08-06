package ru.shchegol.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.service.AdminService;

import java.util.List;

@Tag(name = "Admin")
@RestController
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
@Slf4j
public class DealAdminController {
    private final AdminService adminService;

    @Operation(summary = "Get statement with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400",description = "Failed to get statement with id {statementId}")
    })
    @GetMapping("/statement/{statementId}")
    public ResponseEntity<Statement> getStatementWithId(@PathVariable String statementId) {
        Statement statement = adminService.getStatementWithId(statementId);
        log.info("Response from getStatementWithId: {}", statement);
        return ResponseEntity.ok(statement);
    }

    @Operation(summary = "Get all statements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    @GetMapping("/statement")
    public ResponseEntity<List<Statement>> getAllStatements() {
        List<Statement> statements = adminService.getAllStatements();
        log.info("Response from getAllStatements: {}", statements);
        return ResponseEntity.ok(statements);
    }
}

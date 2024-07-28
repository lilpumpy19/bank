package ru.shchegol.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
public class DealAdminController {
    private final AdminService adminService;
    @GetMapping("/statement/{statementId}")
    public ResponseEntity<Statement> getStatementWithId(@PathVariable String statementId) {
        Statement statement = adminService.getStatementWithId(statementId);
        return ResponseEntity.ok(statement);
    }

    @GetMapping("/statement")
    public ResponseEntity<List<Statement>> getAllStatements() {
        List<Statement> statements = adminService.getAllStatements();
        return ResponseEntity.ok(statements);
    }
}

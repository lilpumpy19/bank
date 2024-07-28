package ru.shchegol.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shchegol.deal.entity.Statement;
import ru.shchegol.deal.exception.StatementNotFoundException;
import ru.shchegol.deal.repository.StatementRepository;
import ru.shchegol.deal.service.AdminService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final StatementRepository statementRepository;

    @Override
    public Statement getStatementWithId(String statementId) {
        log.info("getStatementWithId: {}", statementId);
        return statementRepository
                .findById(UUID.fromString(statementId))
                .orElseThrow(() -> new StatementNotFoundException(statementId));
    }

    @Override
    public List<Statement> getAllStatements() {
        log.info("getAllStatements");
        return statementRepository.findAll();
    }
}

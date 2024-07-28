package ru.shchegol.deal.service;

import ru.shchegol.deal.entity.Statement;

import java.util.List;

public interface AdminService{

    Statement getStatementWithId(String statementId);

    List<Statement> getAllStatements();
}

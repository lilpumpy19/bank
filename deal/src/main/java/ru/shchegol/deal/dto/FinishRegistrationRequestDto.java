package ru.shchegol.deal.dto;

import ru.shchegol.deal.entity.enums.Gender;
import ru.shchegol.deal.entity.enums.MaritalStatus;

import java.time.LocalDate;

public class FinishRegistrationRequestDto {

    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDto employment;
    private String accountNumber;
}
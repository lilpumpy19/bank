package ru.shchegol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.shchegol.dto.enums.Gender;
import ru.shchegol.dto.enums.MaritalStatus;


import java.time.LocalDate;
@Data
public class FinishRegistrationRequestDto {

    @Schema(description = "Gender of the client", example = "MALE")
    private Gender gender;

    @Schema(description = "Marital status of the client", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @Schema(description = "Amount of dependents", example = "2")
    private Integer dependentAmount;

    @Schema(description = "Date of passport issue", example = "2020-01-01")
    private LocalDate passportIssueDate;

    @Schema(description = "Passport issue branch", example = "123-456")
    private String passportIssueBranch;

    @Schema(description = "Information about employment")
    private EmploymentDto employment;

    @Schema(description = "Account number", example = "1234567890123456")
    private String accountNumber;
}
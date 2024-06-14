package ru.shchegol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.shchegol.dto.enums.Gender;
import ru.shchegol.dto.enums.MaritalStatus;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
public class FinishRegistrationRequestDto {

    @Schema(description = "Gender of the client", example = "MALE")
    @NotNull
    private Gender gender;

    @Schema(description = "Marital status of the client", example = "MARRIED")
    @NotNull
    private MaritalStatus maritalStatus;

    @Schema(description = "Amount of dependents", example = "2")
    @Min(0)
    private Integer dependentAmount;

    @Schema(description = "Date of passport issue", example = "2020-01-01")
    @NotNull
    private LocalDate passportIssueDate;

    @Schema(description = "Passport issue branch", example = "123-456")
    @NotNull
    private String passportIssueBranch;

    @Schema(description = "Information about employment")
    @NotNull
    private EmploymentDto employment;

    @Schema(description = "Account number", example = "1234567890123456")
    @NotBlank
    private String accountNumber;
}